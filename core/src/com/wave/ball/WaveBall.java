package com.wave.ball;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import cross.CrossRate;
import cross.CrossShare;
import entities.CircleEntity;
import entities.CircleEntity.Type;
import entities.CounterWave;
import entities.MainWave;
import entities.Rotator;
import entities.ScoreBoard;
import entities.ScoreCheckpoints;
import menus.BallMenu;
import menus.Button;
import menus.MainMenu;
import menus.Menu;
import menus.ScoreMenu;
import menus.TutorialMenu;
import utils.AccelerationManager;
import utils.AssetLoader;
import utils.BackgroundAnimation;
import utils.Constants;
import utils.InputHandler;
import utils.PreferenceManager;
import utils.SoundManager;
import utils.TimeSnapshot;
import utils.TimeSnapshotStore;
import utils.WaveEquation;

public class WaveBall extends ApplicationAdapter {
	private ShapeRenderer renderer;
	private OrthographicCamera cam;
	private OrthographicCamera fixedCam;
	private float screenWidth;
	private float screenHeight;
	private MainWave wave;
	private CounterWave counterWave;
	private CounterWave counterWave2;
	private InputHandler inputHandler;
	private float camSpeedNormal;
	private float cameraX = 0.0f;
	private float cameraY = 0.0f;
	FPSLogger logger = new FPSLogger();
	private final long FALLING_DURATION_TOTAL = 500;
	private long _fallingDuration = 0;
	
	private TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	private float _ballSize;
	private float _enemySize;
	private float _waveAmplitude;
	private float _counterWaveAmplitude;
	private ScoreBoard _scoreBoard;
	private Vector2 _tmpVector;
	private boolean _assetsLoaded = false;
	private AssetLoader _assetLoader;
	
	public static final float _ballPositionFraction = 0.4f;
	private SpriteBatch _spriteBatch;
	boolean collided = false;
	public enum GameState {
		PLAYING, MENU, BALL_FALLING, SCORE_MENU, TUTORIAL, BALL_CHOOSE
	};
	private GameState _gameState = GameState.MENU;
	private MainMenu _mainMenu;
	private ScoreMenu _scoreMenu;
	private TutorialMenu _tutorialMenu;
	private BallMenu _ballMenu;
	private PreferenceManager _prefManager;
	private HashMap<GameState, Menu> _stateToMenu = new HashMap<GameState, Menu>();
	
	private BackgroundAnimation _backgroundAnimation;
	
	private ScoreCheckpoints _scoreCheckpoints;
	
	private SoundManager _soundManager;
	private CrossRate _rate;
	private CrossShare _share;
	public static final int POINTS_PER_BALL = 3;
	private float _loadingWidth, _loadingHeight;
	private float _companyWidth, _companyHeight;
	private float _alpha = 0.0f;
	private float _enemyFrequency;
	private float _enemyFrequencyFraction;
	private float _lastEnemy1 = 0.0f;
	private float _lastEnemy2 = 0.0f;
	private boolean _secondWave = false;
	public WaveBall(CrossRate rate, CrossShare share) {
		_rate = rate;
		_share = share;
	}
	
	@Override
	public void create() {
		TimeSnapshotStore.clear();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);
		fixedCam = new OrthographicCamera();
		fixedCam.setToOrtho(false, screenWidth, screenHeight);
		_spriteBatch = new SpriteBatch();
		_spriteBatch.setProjectionMatrix(cam.combined);
		camSpeedNormal = (float) screenWidth / 4.25f;
		
		_ballSize = screenWidth / 35.0f;
		_waveAmplitude = screenWidth / 10.0f;
		_enemySize = screenWidth / 50.0f;
		_counterWaveAmplitude = screenWidth / 18.0f;

		_assetLoader = new AssetLoader();
		_assetLoader.load(screenWidth, screenHeight);
		_soundManager = new SoundManager(_assetLoader);
		
		WaveEquation.initSize(screenWidth * 100, screenWidth / 1000.0f);
		AccelerationManager.initSize(screenWidth * 100, screenWidth / 100.0f);
		_scoreBoard = new ScoreBoard(screenWidth, screenHeight, _assetLoader);
		
		_prefManager = new PreferenceManager();
		
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth * _ballPositionFraction,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _scoreBoard, _scoreCheckpoints, _prefManager, _assetLoader);
		
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), 1, _assetLoader);
		counterWave2 = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), 2, _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		counterWave2.setWaveEquation(wave.getWaveEquation());
		inputHandler = new InputHandler(screenWidth, screenHeight, this);
		Gdx.input.setInputProcessor(inputHandler);
		
		_tmpVector = new Vector2();
		_scoreCheckpoints = new ScoreCheckpoints();
		_scoreCheckpoints.init(_prefManager, screenWidth, screenHeight, _assetLoader);
		
		_assetLoader.loadSplashFont();
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(_assetLoader.splashFont, "loading");
		_loadingWidth = bounds.width;
		_loadingHeight = bounds.height;
		bounds.setText(_assetLoader.splashFont, "Apple Gloss");
		_companyWidth = bounds.width;
		_companyHeight = bounds.height;
		
		_enemyFrequency = screenWidth / 1.2f;
		_enemyFrequencyFraction = MathUtils.randomTriangular(0.7f, 1.0f);
	}

	@Override
	public void render() {
		logger.log();
		if (!_assetsLoaded) {
			if (_assetLoader.update()) {
				_assetsLoaded = true;
				_assetLoader.assignAssets();
				_mainMenu = new MainMenu(screenWidth, screenHeight, (int) _prefManager.getPoints(), _prefManager, _assetLoader);
				_mainMenu.reset();
				_scoreMenu = new ScoreMenu(screenWidth, screenHeight, _prefManager, _assetLoader);
				_tutorialMenu = new TutorialMenu(screenWidth, screenHeight, _assetLoader);
				_ballMenu = new BallMenu(screenWidth, screenHeight, (int) _prefManager.getMaxBallUnlock(), _prefManager, _assetLoader);
				_stateToMenu.put(GameState.MENU, _mainMenu);
				_stateToMenu.put(GameState.SCORE_MENU, _scoreMenu);
				_stateToMenu.put(GameState.SCORE_MENU, _scoreMenu);
				_stateToMenu.put(GameState.TUTORIAL, _tutorialMenu);
				_stateToMenu.put(GameState.BALL_CHOOSE, _ballMenu);
				
				_backgroundAnimation = new BackgroundAnimation((_assetLoader.backgroundWidth - screenWidth) / 2, (_assetLoader.backgroundHeight - screenHeight) / 2);
				
				_assetLoader.ball = _assetLoader.balls[(int) _prefManager.getSelectedBall()];
				_soundManager.changeMusic(_gameState);
				System.out.println("loaded");
			} else {
				Gdx.gl.glClearColor(0.8f, 0.0f, 0.0f, 0.5f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
				_spriteBatch.begin();
				_assetLoader.splashFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				_assetLoader.splashFont.draw(_spriteBatch, "Apple Gloss", screenWidth / 2.0f - _companyWidth / 2.0f, 2 * screenHeight / 3.0f + _loadingHeight / 2.0f);
				_spriteBatch.end();
			}
			return;
		}
		_soundManager.update();
		if (_gameState == GameState.PLAYING || _gameState == GameState.BALL_FALLING) {
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			manageCameraTranslation();
			cam.update();
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			_spriteBatch.setProjectionMatrix(cam.combined);
			_spriteBatch.begin();
			drawBackground(cam);
			
			counterWave.render(_spriteBatch, cameraX);
			counterWave.update(cameraX);
			if (_secondWave) {
	 			counterWave2.render(_spriteBatch, cameraX);
				counterWave2.update(cameraX);
			}
			wave.render(_spriteBatch, cameraX);
			wave.update(cameraX, _gameState);
			if (_gameState == GameState.PLAYING) {
				checkGameEnd();
			}
			_scoreCheckpoints.render(_spriteBatch, cam.position.x - screenWidth / 2.0f);
			
			if (_gameState == GameState.BALL_FALLING) {
				_assetLoader.particleEffect.update(Gdx.graphics.getDeltaTime());
				_assetLoader.particleEffect.draw(_spriteBatch);
			}
			_spriteBatch.setProjectionMatrix(fixedCam.combined);
			_scoreBoard.update();
			_scoreBoard.render(_spriteBatch, cameraX, wave.getScore());
			_spriteBatch.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			
			tryAddNewWave();
			tryAddEnemies();
		} else {
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    _spriteBatch.setProjectionMatrix(fixedCam.combined);
			_spriteBatch.begin();
		    
			drawBackground(fixedCam);
			Menu menu = _stateToMenu.get(_gameState);
			menu.update();
			for (Button btn: menu.buttons) {
				renderButton(btn, _spriteBatch);
			}
			_spriteBatch.end();
		}
		
		if (_gameState == GameState.BALL_FALLING) {
			_fallingDuration += _timeSnapshot.snapshot();
			if (_fallingDuration >= FALLING_DURATION_TOTAL) {
				_fallingDuration = 0;
				storeScores();
				_scoreMenu.reset(wave.getScore(), (int) _prefManager.getMaxScore(), (int) _prefManager.getPoints());
				_gameState = GameState.SCORE_MENU;
				_alpha = 0.0f;
				_soundManager.changeMusic(_gameState);
			}
		}
		_alpha += 0.03f;
	}

	public void drawBackground(OrthographicCamera cam) {
		_backgroundAnimation.update();
		float x = (cam.position.x - _assetLoader.backgroundWidth / 2);
		float y = (cam.position.y - _assetLoader.backgroundHeight / 2);
		_assetLoader.background.setBounds(x + _backgroundAnimation.getX(), y + _backgroundAnimation.getY(), _assetLoader.backgroundWidth, _assetLoader.backgroundHeight);
		_assetLoader.background.draw(_spriteBatch);
	}
	
	public void checkGameEnd() {
		if (wave.getBallX() < wave.getStartX()) {
			_gameState = GameState.BALL_FALLING;
			_timeSnapshot.snapshot();
			_fallingDuration = 0;
			startParticleEffect();
			_assetLoader.punchSound.play();
			return;
		}
		boolean found = false;
		int collidingIndex = 0;
		for (CircleEntity enemy: counterWave._circles) {
			if (enemy._type == Type.ENEMY && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				if (!wave._heroMode) {
					_gameState = GameState.BALL_FALLING;
					_timeSnapshot.snapshot();
					_fallingDuration = 0;
					startParticleEffect();
					_assetLoader.punchSound.play();
					return;
				}
			}
			if (enemy._type == Type.DIAMOND && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				counterWave.plusOne(enemy._x, enemy._y);
				found = true;
				wave.incrementPoints(1);
				_assetLoader.diamondSound.play();
			}
			if (!found) {
				collidingIndex++;
			}
		}
		if (found) {
			counterWave._circles.remove(collidingIndex);
		}
		found = false;
		collidingIndex = 0;
		for (Rotator rotator: wave._rotators) {
			if (rotator.checkCollision(wave.ballShape)) {
				if (!wave._heroMode) {
					_gameState = GameState.BALL_FALLING;
					_timeSnapshot.snapshot();
					_fallingDuration = 0;
					startParticleEffect();
					_assetLoader.punchSound.play();
					return;
				}
				if (!found) {
					collidingIndex++;
				}
			}
		}
		if (found) {
			wave._rotators.remove(collidingIndex);
		}
		collidingIndex = 0;
		found = false;
		for (CircleEntity diamond: wave._diamonds) {
			if (Intersector.overlaps(diamond.shape, wave.ballShape)) {
				if (diamond._type == Type.HERO) {
					wave._heroModeStartTime = System.currentTimeMillis();
					wave._heroMode = true;
					wave.hero(diamond._x, diamond._y);
				} else {
					counterWave.plusOne(diamond._x, diamond._y);
					wave.incrementPoints(1);
					_assetLoader.diamondSound.play();
				}
				found = true;
				break;
			}
			collidingIndex++;
		}
		if (found) {
			wave._diamonds.remove(collidingIndex);
		}
	}
	
	public void restartGame() {
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth * _ballPositionFraction,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _scoreBoard, _scoreCheckpoints, _prefManager, _assetLoader);
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), 1, _assetLoader);
		counterWave2 = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), 2, _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		counterWave2.setWaveEquation(wave.getWaveEquation());
		_tmpVector.x = -cameraX;
		_tmpVector.y = 0.0f;
		_tmpVector.rotate(Constants.rotation);
		cam.translate(_tmpVector.x, _tmpVector.y);
		cameraX = 0.0f;
		cameraY = 0.0f;
		_lastEnemy1 = 0.0f;
		_secondWave = false;
		_scoreCheckpoints.init(_prefManager, screenWidth, screenHeight, _assetLoader);
		if (Math.random() < 0.3) {
			_assetLoader.setBackground();
		}
		_enemyFrequency = screenWidth / 1.2f;
		_enemyFrequencyFraction = MathUtils.randomTriangular(0.7f, 1.0f);
	}
	
	private void storeScores() {
		if (_prefManager.getMaxScore() < wave.getScore()) {
			_prefManager.setMaxScore(wave.getScore());
		}
		_prefManager.setPoints(_prefManager.getPoints() + wave.getPoints());
		_prefManager.setLastScore(wave.getScore());
	}
	
	private void manageCameraTranslation() {
		if (_gameState != GameState.PLAYING) {
			return;
		}
		if ((float) ((int) wave.getBallX() - cameraX) / screenWidth >= _ballPositionFraction) {
			_tmpVector.x = wave.getBallX() - cameraX - screenWidth * _ballPositionFraction;
			_tmpVector.y = 0.0f;
			_tmpVector.rotate(Constants.rotation);
			cam.translate(_tmpVector.x, _tmpVector.y);
			cameraY += _tmpVector.y;
			cameraX += wave.getBallX() - cameraX - screenWidth * _ballPositionFraction;
		}
	}
	
	public void touchDown(float x, float y) {
		if (!_assetsLoaded) {
			return;
		}
		if (_gameState == GameState.PLAYING) {
			wave.start();
			return;
		}
		if (_gameState == GameState.BALL_FALLING) {
			return;
		}
		Menu menu = _stateToMenu.get(_gameState);
		int id = menu.handleClick(x, y);
		if (_gameState == GameState.MENU) {
			switch (id) {
			case MainMenu.PLAY:
				restartGame();
				_tutorialMenu.reset();
				_gameState = GameState.TUTORIAL;
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			case MainMenu.RATE:
				_rate.rate();
				break;
			case MainMenu.BALL:
				_ballMenu.reset();
				_gameState = GameState.BALL_CHOOSE;
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			}
		} else if (_gameState == GameState.SCORE_MENU) {
			switch (id) {
			case ScoreMenu.PLAY:
				restartGame();
				_gameState = GameState.PLAYING;
				_soundManager.changeMusic(_gameState);
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			case ScoreMenu.RATE:
				_rate.rate();
				break;
			case ScoreMenu.SHARE:
				_share.share(wave.getScore());
				break;
			case ScoreMenu.HOME:
				_mainMenu.reset();
				_gameState = GameState.MENU;
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			case ScoreMenu.BALL:
				_ballMenu.reset();
				_gameState = GameState.BALL_CHOOSE;
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			}
		} else if (_gameState == GameState.TUTORIAL) {
			switch (id) {
			case TutorialMenu.PLAY:
				restartGame();
				_gameState = GameState.PLAYING;
				_soundManager.changeMusic(_gameState);
				_soundManager.playButton();
				break;
			}
		} else if (_gameState == GameState.BALL_CHOOSE) {
			switch (id) {
			case BallMenu.HOME:
				_mainMenu.reset();
				_gameState = GameState.MENU;
				_soundManager.playButton();
				_alpha = 0.0f;
				break;
			}
			if (id == BallMenu.BALL1 + _prefManager.getMaxBallUnlock() + 1 && _prefManager.getPoints() >= POINTS_PER_BALL) {
				_prefManager.setMaxBallUnlock(_prefManager.getMaxBallUnlock() + 1);
				_prefManager.setPoints(_prefManager.getPoints() - POINTS_PER_BALL);
				_ballMenu.resetMaxBallUnlock();
				_mainMenu.reset();
				_soundManager.playButton();
			} else if (id >= BallMenu.BALL1 && id <= BallMenu.BALL1 + _prefManager.getMaxBallUnlock()) {
				_prefManager.setSelectedBall(id - BallMenu.BALL1);
				_ballMenu.resetSelection();
				_assetLoader.ball = _assetLoader.balls[id - BallMenu.BALL1];
			}
		}
	}
	
	public void touchUp(float x, float y) {
		if (!_assetsLoaded) {
			return;
		}
		if (_gameState == GameState.MENU) {
			
		} else if (_gameState == GameState.PLAYING) {
			wave.stop();
		}
	}
	
	private void renderButton(Button button, SpriteBatch spriteBatch) {
		float width = button.w;
		float height = button.h;
		
		if (button.isTexture) {
			button.sprite.setOriginCenter();
			button.sprite.setRotation(button.angle);
			button.sprite.setBounds(button.x, button.y, width, height);
			button.sprite.setAlpha(Math.min(button.alpha, _alpha));
			button.sprite.draw(spriteBatch);
		} else {
			button.font.setColor(0.0f, 0.0f, 0.0f, Math.min(0.85f, _alpha));
			button.font.draw(spriteBatch, button.label, button.x, button.y);
		}
	}

	@Override
	public void resume() {
		TimeSnapshotStore.resume();
	}
	
	public void startParticleEffect() {
		_assetLoader.particleEffect.reset();
		_tmpVector.x = wave._ballX - screenWidth / 2.0f;
		_tmpVector.y = wave._ballY - screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		_assetLoader.particleEffect.setPosition(screenWidth / 2.0f + _tmpVector.x, screenHeight / 2.0f + _tmpVector.y);
		_assetLoader.particleEffect.start();
	}
	
	private void tryAddNewWave() {
		if (cameraX > 10 * screenWidth && !_secondWave) {
			counterWave2.setMinX(cameraX + screenWidth);
			_enemyFrequency = _enemyFrequency * 0.8f;
			_secondWave = true;
		}
	}
	
	private void tryAddEnemies() {
		if (cameraX < 4 * screenWidth) {
			return;
		}
		float lastX = 100 * screenWidth;
		if (counterWave._circles.size() > 0) {
			lastX = counterWave._circles.get(counterWave._circles.size() - 1)._x;
		}
		if (counterWave2._circles.size() > 0) {
			lastX = Math.min(counterWave2._circles.get(counterWave2._circles.size() - 1)._x, lastX);
		}
		if (!_secondWave || Math.random() > 0.5) {
			if (lastX - cameraX > _enemyFrequency * _enemyFrequencyFraction) {
				counterWave.addEnemy(cameraX);
				_enemyFrequencyFraction = MathUtils.random(0.7f, 1.0f);
			}
		} else {
			if (lastX - cameraX > _enemyFrequency * _enemyFrequencyFraction) {
				counterWave2.addEnemy(cameraX);
				_enemyFrequencyFraction = MathUtils.random(0.7f, 1.0f);
			}
		}
	}
}
