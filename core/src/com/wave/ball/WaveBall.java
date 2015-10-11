package com.wave.ball;

import java.util.ArrayList;

import utils.InputHandler;
import utils.TimeSnapshot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import entities.CounterWave;
import entities.RectangleEnemy;
import entities.MainWave;

public class WaveBall extends ApplicationAdapter {
	private ShapeRenderer renderer;
	private OrthographicCamera cam;
	private float screenWidth;
	private float screenHeight;
	private MainWave wave;
	private CounterWave counterWave;
	private InputHandler inputHandler;
	private float camSpeedNormal;
	private float camSpeedTrailing;
	private ArrayList<RectangleEnemy> rectangleEnemies = new ArrayList<RectangleEnemy>();
	private TimeSnapshot _timeSnapshot = new TimeSnapshot();
	
	@Override
	public void create() {
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, screenWidth, screenHeight);
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(cam.combined);
		camSpeedNormal = (float) screenWidth / 10.0f;
		camSpeedTrailing = (float) screenWidth / 20.0f;
		wave = new MainWave(0.0f, screenHeight / 6.0f, 2, camSpeedNormal, screenHeight / 60.0f, screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f));
		counterWave = new CounterWave((float) (Math.PI), screenHeight / 6.0f, 2, camSpeedNormal, screenHeight / 60.0f, screenWidth / 3.0f, screenWidth, screenHeight, new Color(0.0f, 1.0f, 0.0f, 0.5f));
		inputHandler = new InputHandler(screenWidth, screenHeight, this);
		Gdx.input.setInputProcessor(inputHandler);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		manageCameraTranslation();
		cam.update();
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Filled);
		wave.update();
		counterWave.update();
		wave.render(renderer, screenWidth, screenHeight);
		counterWave.render(renderer, screenWidth, screenHeight);
		updateAndRenderEnemies();
		renderer.end();
	}
	
	private void manageCameraTranslation() {
		long delta = _timeSnapshot .snapshot();
		if ((float)((int)wave.getBallX() % (int)screenWidth) / screenWidth < 0.35f) {
			cam.translate(camSpeedNormal * delta / 1000.0f, 0.0f);
		} else {
			cam.translate(camSpeedTrailing * delta / 1000.0f, 0.0f);
		}
	}
	
	private void updateAndRenderEnemies() {
		for (RectangleEnemy enemy: rectangleEnemies) {
			enemy.update();
			enemy.render(renderer);
		}
	}
	public void touchDown(InputHandler.UserInput input) {
		switch (input) {
		case LEFT:
			wave.startLeft();
			counterWave.startLeft();
			break;
		case RIGHT:
			wave.startRight();
			counterWave.startRight();
			break;
		}
	}
	public void touchUp(InputHandler.UserInput input) {
		wave.stop();
		counterWave.stop();
	}
}
