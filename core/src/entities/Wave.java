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
	protected TimeSnapshot _timeSnapshot = new TimeSnapshot();
	protected float _screenWidth;
	protected float _screenHeight;
	protected Color _color;
	public Wave(float phase, float amplitude, int frequency, float speed, float screenWidth, float screenHeight, Color color) {
		_phase = phase;
		_amplitude = amplitude;
		_frequency = frequency;
		_speed = speed;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_color = color;
	}
	public void render(ShapeRenderer renderer, float cameraX) {
		float waveWidth = _screenWidth / _frequency;
		float angleToWidth = (float) (waveWidth / (2 * Math.PI));
		float angle = (float) (0.0f + 2 * Math.PI / numComponentsPerWave);
		float phase = (float)(((int)cameraX % (int)waveWidth) * Math.PI * 2.0f / waveWidth) + _phase; 
		float prevX = cameraX, prevY = (float) Math.sin(phase) * _amplitude + _screenHeight / 2;
		for (int i = 0; i < _frequency; i++) {
			for (angle = 0.0f; angle < 2 * Math.PI; angle += 2 * Math.PI / numComponentsPerWave) {
				float x = cameraX + angle * angleToWidth + i * waveWidth;
				float y = (float) Math.sin(angle + phase) * _amplitude + _screenHeight / 2;
				drawLine(renderer, prevX, prevY, x, y, _screenWidth / 200);
				prevX = x;
				prevY = y;
			}
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
}
