package com.wave.ball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import entities.CircleEntity;
import entities.CounterWave;
import entities.MainWave;
import entities.Rotator;
import entities.ScoreBoard;
import entities.CircleEntity.Type;
import menus.Button;
import menus.MainMenu;
import utils.AccelerationManager;
import utils.AssetLoader;
import utils.Constants;
import utils.InputHandler;
import utils.PreferenceManager;
import utils.TimeSnapshot;
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
	
	private TimeSnapshot _timeSnapshot = new TimeSnapshot();
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
		PLAYING, MENU, BALL_FALLING
	};
	private GameState _gameState = GameState.MENU;
	private MainMenu _mainMenu;
	private PreferenceManager _prefManager;
	@Override
	public void create() {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);
		fixedCam = new OrthographicCamera();
		fixedCam.setToOrtho(false, screenWidth, screenHeight);
		_spriteBatch = new SpriteBatch();
		_spriteBatch.setProjectionMatrix(cam.combined);
		camSpeedNormal = (float) screenWidth / 4.25f;
		
		_ballSize = screenWidth / 52.0f;
		_waveAmplitude = screenWidth / 12.0f;
		_enemySize = screenWidth / 100.0f;
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
		if (collided) {
			return;
		}
		if (!_assetsLoaded) {
			if (_assetLoader.update()) {
				_assetsLoaded = true;
				_assetLoader.assignAssets();
				_mainMenu = new MainMenu(screenWidth, screenHeight, _assetLoader);
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
			_assetLoader.background.setBounds(cam.position.x - screenWidth / 2, cam.position.y - screenHeight / 2, screenWidth, screenHeight);
			_assetLoader.background.draw(_spriteBatch);
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
		} else if (_gameState == GameState.MENU) {
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    _spriteBatch.setProjectionMatrix(fixedCam.combined);
			_spriteBatch.begin();
		    _assetLoader.background.setBounds(0.0f, 0.0f, screenWidth, screenHeight);
			_assetLoader.background.draw(_spriteBatch);
			_mainMenu.update();
			for (Button btn: _mainMenu.buttons) {
				renderButton(btn, _spriteBatch);
			}
			_spriteBatch.end();
		}
		if (_gameState == GameState.BALL_FALLING) {
			_fallingDuration += _timeSnapshot.snapshot();
			if (_fallingDuration >= FALLING_DURATION_TOTAL) {
				_fallingDuration = 0;
				_mainMenu.reset();
				_gameState = GameState.MENU;
			}
		}
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
		if (_gameState == GameState.MENU) {
			int id = _mainMenu.handleClick(x, y);
			switch (id) {
			case MainMenu.PLAY:
				restartGame();
				_gameState = GameState.PLAYING;
				break;
			}
		} else if (_gameState == GameState.PLAYING) {
			wave.start();
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
		
		button.sprite.setBounds(button.x, button.y, width, height);
		button.sprite.draw(spriteBatch);
	}
}
