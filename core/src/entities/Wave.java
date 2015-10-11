package entities;

import utils.Acceleration;
import utils.TimeSnapshot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Wave {
	protected float _phase;
	protected float _amplitude;
	protected int _frequency;
	protected float _speed;
	protected static int numComponentsPerWave = 50;
	protected Acceleration _acceleration;
	protected TimeSnapshot _timeSnapshot = new TimeSnapshot();
	protected float _translationX = 0.0f;
	protected float _screenWidth;
	protected float _screenHeight;
	protected Color _color;
	protected static enum DIRECTION {
		LEFT,
		RIGHT,
		NONE
	};
	protected DIRECTION _direction = DIRECTION.NONE;
	protected DIRECTION _lastDirection = DIRECTION.NONE;
	public Wave(float phase, float amplitude, int frequency, float speed, float screenWidth, float screenHeight, Color color) {
		_phase = phase;
		_amplitude = amplitude;
		_frequency = frequency;
		_speed = speed;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_acceleration = new Acceleration(speed / 3.0f, speed / 1.0f, speed);
		_color = color;
	}
	protected void increasePhase(float deltaX) {
		float waveWidth = _screenWidth / _frequency;
		_phase += deltaX / waveWidth * 2 * Math.PI;
		_translationX += deltaX;
	}
	protected void decreasePhase(float deltaX) {
		float waveWidth = _screenWidth / _frequency;
		_phase -= deltaX / waveWidth * 2 * Math.PI;
		_translationX -= deltaX;
	}
	public void update() {
		long timeElapsed = _timeSnapshot.snapshot();
		switch (_direction) {
		case LEFT:
			_lastDirection = DIRECTION.LEFT;
			_acceleration.start();
			_acceleration.update(timeElapsed / 1000.0f);
			decreasePhase(timeElapsed * _acceleration.getSpeed() / 1000);
			break;
		case RIGHT:
			_acceleration.start();
			_lastDirection = DIRECTION.RIGHT;
			_acceleration.update(timeElapsed / 1000.0f);
			increasePhase(timeElapsed * _acceleration.getSpeed() / 1000);
			break;
		case NONE:
			_acceleration.stop();
			_acceleration.update(timeElapsed / 1000.0f);
			if (_lastDirection == DIRECTION.LEFT) {
				decreasePhase(timeElapsed * _acceleration.getSpeed());
			} else if (_lastDirection == DIRECTION.RIGHT) {
				increasePhase(timeElapsed * _acceleration.getSpeed());
			}
			break;
		}
	}
	public void render(ShapeRenderer renderer) {
		float waveWidth = _screenWidth / _frequency;
		float angleToWidth = (float) (waveWidth / (2 * Math.PI));
		float angle = (float) (0.0f + 2 * Math.PI / numComponentsPerWave);
		float prevX = _translationX, prevY = (float) Math.sin(_phase) * _amplitude + _screenHeight / 2;
		for (int i = 0; i < _frequency; i++) {
			for (; angle < 2 * Math.PI; angle += 2 * Math.PI / numComponentsPerWave) {
				float x = _translationX + angle * angleToWidth + i * waveWidth;
				float y = (float) Math.sin(angle + _phase) * _amplitude + _screenHeight / 2;
				drawLine(renderer, prevX, prevY, x, y, _screenWidth / 200);
				prevX = x;
				prevY = y;
			}
			angle = 0.0f;
		}
	}
	public void drawLine(ShapeRenderer renderer, float x1, float y1, float x2, float y2, float width) {
		renderer.setColor(0.0f, 1.0f, 0.0f, 0.5f);
		renderer.rectLine(x1, y1, x2, y2, width);
	}
	public void drawCircle(ShapeRenderer renderer, float x, float y, float r) {
		renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		renderer.circle(x, y, r);
	}
	public void startLeft() {
		_direction = DIRECTION.LEFT;
	}
	public void startRight() {
		_direction = DIRECTION.RIGHT;
	}
	public void stop() {
		_direction = DIRECTION.NONE;
	}
	public float getTranslation() {
		return _translationX;
	}
}
