package entities;

import utils.Acceleration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainWave extends Wave {
	protected float _ballX;
	protected float _radius;
	protected boolean moving = false;
	protected Acceleration _acceleration;
	public float getBallX() {
		return _ballX;
	}
	public MainWave(float phase, float amplitude, int frequency, float speed, float radius, float ballX, float screenWidth, float screenHeight, Color color) {
		super(phase, amplitude, frequency, speed, screenWidth, screenHeight, color);
		_ballX = ballX;
		_radius = radius;
		_acceleration = new Acceleration(speed / 3.0f, speed / 1.0f, speed);
	}
	public void update() {
		long timeElapsed = _timeSnapshot.snapshot();
		if (moving) {
			_acceleration.start();
		} else {
			_acceleration.stop();
		}
		_acceleration.update(timeElapsed);
		increasePhase(timeElapsed * _acceleration.getSpeed() / 1000);
	}
	protected void increasePhase(float delta) {
		_ballX += delta;
	}
	public void render(ShapeRenderer renderer, float screenWidth, float screenHeight, float cameraX) {
		super.render(renderer, cameraX);
		float waveWidth = screenWidth / _frequency;
		float angleToWidth = (float) (waveWidth / (2 * Math.PI));
		float ballAngle = ((int)_ballX % (int)waveWidth) / angleToWidth;
		float ballY = (float) Math.sin(ballAngle) * _amplitude + screenHeight / 2;
		drawCircle(renderer, _ballX, ballY, _radius);
	}
	public void start() {
		moving = true;
	}
	public void stop() {
		moving = false;
	}
}
