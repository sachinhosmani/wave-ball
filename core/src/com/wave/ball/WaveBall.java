package com.wave.ball;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import entities.CounterWave;
import entities.MainWave;
import entities.Rotator;
import entities.ScoreBoard;
import utils.CircleEnemy;
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
	
	private SpriteBatch _spriteBatch;
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
		
		_ballSize = screenWidth / 65.0f;
		_waveAmplitude = screenWidth / 12.0f;
		_enemySize = screenWidth / 100.0f;
		_counterWaveAmplitude = screenWidth / 18.0f;

		WaveEquation.initSize(screenWidth * 100, screenWidth / 1000.0f);
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth / 2.0f,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f));
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation());
		counterWave.setWaveEquation(wave.getWaveEquation());
		inputHandler = new InputHandler(screenWidth, screenHeight, this);
		Gdx.input.setInputProcessor(inputHandler);
		_scoreBoard = new ScoreBoard(screenWidth, screenHeight);
		
		_tmpVector = new Vector2();
	}

	@Override
	public void render() {
		logger.log();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		manageCameraTranslation();
		cam.update();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Filled);
		wave.render(renderer, cameraX);
		wave.update(cameraX);
		counterWave.render(renderer, cameraX);
		counterWave.update(cameraX);
		checkGameEnd();
		renderer.end();
		
		_spriteBatch.setProjectionMatrix(cam.combined);
		_spriteBatch.begin();
		_scoreBoard.render(_spriteBatch, cameraX, wave.getScore());
		_spriteBatch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public void checkGameEnd() {
		if (wave.getBallX() < wave.getStartX()) {
			restartGame();
		}
		for (CircleEnemy enemy: counterWave._enemies) {
			if (Intersector.overlaps(enemy.shape, wave.ballShape)) {
				restartGame();
				return;
			}
		}
		for (Rotator rotator: wave._rotators) {
			if (rotator.checkCollision(wave.ballVector, wave.getRadius())) {
				restartGame();
				return;
			}
		}
	}
	
	public void restartGame() {
		wave = new MainWave(0.0f, _waveAmplitude, 2, camSpeedNormal, _ballSize, screenWidth / 2.0f,
				screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f));
		counterWave = new CounterWave((float) (Math.PI), _counterWaveAmplitude, camSpeedNormal, _enemySize,
				screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f), wave.getWaveEquation());
		counterWave.setWaveEquation(wave.getWaveEquation());
		_tmpVector.x = -cameraX;
		_tmpVector.y = 0.0f;
		_tmpVector.rotate(20);
		cam.translate(_tmpVector.x, _tmpVector.y);
		cameraX = 0.0f;
	}
	
	private void manageCameraTranslation() {
		if ((float) ((int) wave.getBallX() - cameraX) / screenWidth >= 0.5f) {
			_tmpVector.x = wave.getBallX() - cameraX - screenWidth * 0.5f;
			_tmpVector.y = 0.0f;
			_tmpVector.rotate(20);
			cam.translate(_tmpVector.x, _tmpVector.y);
			cameraX += wave.getBallX() - cameraX - screenWidth * 0.5f;
		}
	}
	
	public void touchDown(InputHandler.UserInput input) {
		wave.start();
	}
	public void touchUp(InputHandler.UserInput input) {
		wave.stop();
	}
}
