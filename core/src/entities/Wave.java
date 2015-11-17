package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import utils.TimeSnapshot;
import utils.WaveEquation;

public class Wave {
	protected float _phase;
	protected float _amplitude;
	protected float _speed;
	protected static int numComponentsPerWave = 50;
	protected TimeSnapshot _timeSnapshot = new TimeSnapshot();
	protected float _screenWidth;
	protected float _screenHeight;
	protected Color _color;
	protected WaveEquation _waveEquation;
	protected Float _startX = null;
	protected Vector2 _tmpVector = new Vector2();
	public Wave(float phase, float amplitude, float speed, float screenWidth, float screenHeight, Color color) {
		_phase = phase;
		_amplitude = amplitude;
		_speed = speed;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_color = color;
	}
	public Float getStartX() {
		return _startX;
	}
	public void render(ShapeRenderer renderer, float cameraX, Color color) {
		render(renderer, cameraX, false, color);
	}
	public void render(ShapeRenderer renderer, float cameraX, boolean opposite, Color color) {
		float y = 0.0f;
		float prevX = (_startX != null) ? _startX : cameraX;
		_waveEquation.get(prevX, _tmpVector);
		float prevY = _tmpVector.y;
		if (opposite) {
			prevY = _screenHeight / 2.0f + (_screenHeight / 2.0f - prevY) / 2.0f;
		}
		float damping = 1.0f;
		float dampingFraction = 1.0f;
		boolean firstTime = true;
		for (float x = cameraX; x <= cameraX + _screenWidth * 1.01f; x += _screenWidth / 200.0f) {
			if (x < prevX) {
				continue;
			} else if (firstTime) {
				firstTime = false;
				prevX = x;
			}
			_waveEquation.get(x, _tmpVector);
			y = _tmpVector.y;
			if (opposite) {
				y = _screenHeight / 2.0f + (_screenHeight / 2.0f - y) / 2.0f;
			}
			if (!opposite && x - cameraX > _screenWidth * 0.8f) {
				damping *= dampingFraction;
				dampingFraction *= 0.995f;
			}
			y = (y - _screenHeight / 2.0f) * damping + _screenHeight / 2.0f;
			drawLine(renderer, x, y, prevX, prevY, _screenWidth / 200.0f, color);
			prevY = y;
			prevX = x;
		}
	}
	public void drawLine(ShapeRenderer renderer, float x1, float y1, float x2, float y2, float width, Color color) {
		renderer.setColor(color);
		renderer.rectLine(x1, y1, x2, y2, width);
	}
	public void drawCircle(ShapeRenderer renderer, float x, float y, float r, Color c) {
		renderer.setColor(c);
		renderer.circle(x, y, r);
	}
	public float normalizeAngle(float angle) {
		return angle - (float) ((int)(angle / 2 / Math.PI) * 2 * Math.PI);
	}
}
