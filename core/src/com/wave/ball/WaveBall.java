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

import entities.CounterWave;
import entities.MainWave;
import entities.Rotator;
import entities.ScoreBoard;
import menus.Button;
import menus.MainMenu;
import utils.AssetLoader;
import utils.CircleEntity;
import utils.CircleEntity.Type;
import utils.Constants;
import utils.InputHandler;
import utils.WaveEquation;

public class WaveBall extends ApplicationAdapter {
	private ShapeRenderer renderer;
	private OrthographicCamera cam;
	private float screenWidth;
	private float screenHeight;
	private MainWave wave;
	private CounterWave counterWave;
	private InputHandler inputHandler;
	private float camSpeedNormal;
	private float cameraX = 0.0f;
	FPSLogger logger = new FPSLogger();
	
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
	
	private enum GameState {
		PLAYING, MENU
	};
	private GameState _gameState = GameState.PLAYING;
	private MainMenu _mainMenu;
	@Override
	public void create() {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(cam.combined);
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
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth * _ballPositionFraction,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _assetLoader);
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		inputHandler = new InputHandler(screenWidth, screenHeight, this);
		Gdx.input.setInputProcessor(inputHandler);
		_scoreBoard = new ScoreBoard(screenWidth, screenHeight, _assetLoader);
		
		_tmpVector = new Vector2();
	}

	@Override
	public void render() {
		logger.log();
		if (!_assetsLoaded) {
			if (_assetLoader.update()) {
				_assetsLoaded = true;
				_assetLoader.assignAssets();
				_mainMenu = new MainMenu(screenWidth, screenHeight, _assetLoader);
				System.out.println("loaded");
			}
			return;
		}
		if (_gameState == GameState.PLAYING) {
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			manageCameraTranslation();
			cam.update();
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			renderer.setProjectionMatrix(cam.combined);
			
			_spriteBatch.setProjectionMatrix(cam.combined);
			_spriteBatch.begin();
			wave.render(_spriteBatch, cameraX);
			wave.update(cameraX);
			counterWave.render(_spriteBatch, cameraX);
			counterWave.update(cameraX);
			checkGameEnd();
			_scoreBoard.render(_spriteBatch, cameraX, wave.getScore());
			_spriteBatch.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		} else if (_gameState == GameState.MENU) {
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glEnable(GL20.GL_BLEND);
		    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			_spriteBatch.setProjectionMatrix(cam.combined);
			_spriteBatch.begin();
			for (Button btn: _mainMenu.buttons) {
				renderButton(btn, _spriteBatch);
			}
			_spriteBatch.end();
		}
	}
	
	public void checkGameEnd() {
		if (wave.getBallX() < wave.getStartX()) {
			restartGame();
		}
		boolean found = false;
		int collidingDiamondIndex = 0;
		for (CircleEntity enemy: counterWave._circles) {
			if (enemy._type == Type.ENEMY && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				restartGame();
				return;
			}
			if (enemy._type == Type.DIAMOND && Intersector.overlaps(enemy.shape, wave.ballShape)) {
				found = true;
				wave.incrementScore(1);
			}
			if (!found) {
				collidingDiamondIndex++;
			}
		}
		if (found) {
			counterWave._circles.remove(collidingDiamondIndex);
		}
		for (Rotator rotator: wave._rotators) {
			if (rotator.checkCollision(wave.ballVector, wave.getRadius())) {
				restartGame();
				return;
			}
		}
		collidingDiamondIndex = 0;
		found = false;
		for (CircleEntity diamond: wave._diamonds) {
			if (Intersector.overlaps(diamond.shape, wave.ballShape)) {
				found = true;
				wave.incrementScore(1);
				break;
			}
			collidingDiamondIndex++;
		}
		if (found) {
			wave._diamonds.remove(collidingDiamondIndex);
		}
	}
	
	public void restartGame() {
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth * _ballPositionFraction,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), _assetLoader);
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation(), _assetLoader);
		counterWave.setWaveEquation(wave.getWaveEquation());
		_tmpVector.x = -cameraX;
		_tmpVector.y = 0.0f;
		_tmpVector.rotate(Constants.rotation);
		cam.translate(_tmpVector.x, _tmpVector.y);
		cameraX = 0.0f;
	}
	
	private void manageCameraTranslation() {
		if ((float) ((int) wave.getBallX() - cameraX) / screenWidth >= _ballPositionFraction) {
			_tmpVector.x = wave.getBallX() - cameraX - screenWidth * _ballPositionFraction;
			_tmpVector.y = 0.0f;
			_tmpVector.rotate(Constants.rotation);
			cam.translate(_tmpVector.x, _tmpVector.y);
			cameraX += wave.getBallX() - cameraX - screenWidth * _ballPositionFraction;
		}
	}
	
	public void touchDown(InputHandler.UserInput input) {
		wave.start();
	}
	public void touchUp(InputHandler.UserInput input) {
		wave.stop();
	}
	
	private void renderButton(Button button, SpriteBatch spriteBatch) {
		Sprite buttonSprite;
		float width = button.boxWidth == 0.0f ? button.w : button.boxWidth;
		float height = button.h;
		width += button.paddingFractionX * width;
		float ratio = width / height;
		if (ratio < 2.0f) {
			buttonSprite = _assetLoader.button1;
		} else if (ratio < 6.0f) {
			buttonSprite = _assetLoader.button2;
		} else {
			buttonSprite = _assetLoader.button3;
		}
		buttonSprite.setColor(0.5f, 0.5f, 0.5f, 0.9f);
		buttonSprite.setRotation(0.0f);
		float centerX = button.x + button.w / 2;
		buttonSprite.setBounds(centerX - width * 0.5f, button.y - height * 1.4f, width, height * 1.8f);
		buttonSprite.draw(spriteBatch);
		button.font.setColor(0.0f, 0.0f, 0.0f, 0.9f);
		button.font.draw(spriteBatch, button.label, button.x, button.y);
	}
}
