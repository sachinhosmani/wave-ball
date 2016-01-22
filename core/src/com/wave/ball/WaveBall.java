package com.wave.ball;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import cross.CrossRate;
import cross.CrossShare;
import entities.CircleEntity;
import entities.CircleEntity.Type;
import entities.CounterWave;
import entities.MainWave;
import entities.Rotator;
import entities.ScoreBoard;
import menus.Button;
import menus.MainMenu;
import menus.Menu;
import menus.ScoreMenu;
import utils.AccelerationManager;
import utils.AssetLoader;
import utils.BackgroundAnimation;
import utils.Constants;
import utils.InputHandler;
import utils.PreferenceManager;
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
	private InputHandler inputHandler;
	private float camSpeedNormal;
	private float cameraX = 0.0f;
	private float cameraY = 0.0f;
	FPSLogger logger = new FPSLogger();
	private final long FALLING_DURATION_TOTAL = 1000;
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
	
	private final float _ballPositionFraction = 0.4f;
	private SpriteBatch _spriteBatch;
	boolean collided = false;
	public enum GameState {
		PLAYING, MENU, BALL_FALLING, SCORE_MENU
	};
	private GameState _gameState = GameState.MENU;
	private MainMenu _mainMenu;
	private ScoreMenu _scoreMenu;
	private PreferenceManager _prefManager;
	private HashMap<GameState, Menu> _stateToMenu = new HashMap<GameState, Menu>();
	
	private BackgroundAnimation _backgroundAnimation;
	
	private CrossRate _rate;
	private CrossShare _share;
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
		
		_ballSize = screenWidth / 40.0f;
		_waveAmplitude = screenWidth / 12.0f;
		_enemySize = screenWidth / 60.0f;
		_counterWaveAmplitude = screenWidth / 18.0f;

		_assetLoader = new AssetLoader();
		_assetLoader.load(screenWidth, screenHeight);
		
		WaveEquation.initSize(screenWidth * 100, screenWidth / 1000.0f);
		AccelerationManager.initSize(screenWidth * 100, screenWidth / 100.0f);
		_scoreBoard = new ScoreBoard(screenWidth, screenHeight, _assetLoader);
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth * _ballPositionFraction,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _scoreBoard, _assetLoader);
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		inputHandler = new InputHandler(screenWidth, screenHeight, this);
		Gdx.input.setInputProcessor(inputHandler);
		
		_tmpVector = new Vector2();
		_prefManager = new PreferenceManager();
		
	}

	@Override
	public void render() {
		logger.log();
		if (!_assetsLoaded) {
			if (_assetLoader.update()) {
				_assetsLoaded = true;
				_assetLoader.assignAssets();
				_mainMenu = new MainMenu(screenWidth, screenHeight, (int) _prefManager.getPoints(), _assetLoader);
				_scoreMenu = new ScoreMenu(screenWidth, screenHeight, _assetLoader);
				_stateToMenu.put(GameState.MENU, _mainMenu);
				_stateToMenu.put(GameState.SCORE_MENU, _scoreMenu);
				
				_backgroundAnimation = new BackgroundAnimation((_assetLoader.backgroundWidth - screenWidth) / 2, (_assetLoader.backgroundHeight - screenHeight) / 2);
				System.out.println("loaded");
			}
			return;
		}
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
			wave.render(_spriteBatch, cameraX);
			wave.update(cameraX, _gameState);
			if (_gameState == GameState.PLAYING) {
				checkGameEnd();
			}
			_spriteBatch.setProjectionMatrix(fixedCam.combined);
			_scoreBoard.update();
			_scoreBoard.render(_spriteBatch, cameraX, wave.getScore());
			_spriteBatch.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
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
			}
		}
	}

	public void drawBackground(OrthographicCamera cam) {
		_backgroundAnimation.update();
		float x = (cam.position.x - _assetLoader.backgroundWidth / 2);
		float y = (cam.position.y - _assetLoader.backgroundHeight / 2);
//		System.out.println(_backgroundAnimation.getX() + "," + _backgroundAnimation.getY());
		_assetLoader.background.setBounds(x + _backgroundAnimation.getX(), y + _backgroundAnimation.getY(), _assetLoader.backgroundWidth, _assetLoader.backgroundHeight);
		_assetLoader.background.draw(_spriteBatch);
	}
	public void checkGameEnd() {
		if (wave.getBallX() < wave.getStartX()) {
			_gameState = GameState.BALL_FALLING;
			_timeSnapshot.snapshot();
			_fallingDuration = 0;
		}
		boolean found = false;
		int collidingIndex = 0;
		for (CircleEntity enemy: counterWave._circles) {
			if (enemy._type == Type.ENEMY && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				if (!wave._heroMode) {
					_gameState = GameState.BALL_FALLING;
					_timeSnapshot.snapshot();
					_fallingDuration = 0;
					return;
				}
			}
			if (enemy._type == Type.DIAMOND && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				counterWave.plusOne(enemy._x, enemy._y);
				found = true;
				wave.incrementPoints(1);
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
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _scoreBoard, _assetLoader);
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		_tmpVector.x = -cameraX;
		_tmpVector.y = 0.0f;
		_tmpVector.rotate(Constants.rotation);
		cam.translate(_tmpVector.x, _tmpVector.y);
		cameraX = 0.0f;
		cameraY = 0.0f;
	}
	
	private void storeScores() {
		if (_prefManager.getMaxScore() < wave.getScore()) {
			_prefManager.setMaxScore(wave.getScore());
		}
		_prefManager.setPoints(_prefManager.getPoints() + 1);
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
				_gameState = GameState.PLAYING;
				break;
			}
		} else if (_gameState == GameState.SCORE_MENU) {
			switch (id) {
			case ScoreMenu.PLAY:
				restartGame();
				_gameState = GameState.PLAYING;
				break;
			case ScoreMenu.RATE:
				_rate.rate();
				break;
			case ScoreMenu.SHARE:
				_share.share(wave.getScore());
				break;
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
			button.sprite.draw(spriteBatch);
		} else {
			button.font.setColor(button.color);
			button.font.draw(spriteBatch, button.label, button.x, button.y);
		}
	}

	@Override
	public void resume() {
		TimeSnapshotStore.resume();
	}
}
