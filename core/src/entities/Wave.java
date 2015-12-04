package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.Constants;
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
	protected AssetLoader _assetLoader;
	
	public Wave(float phase, float amplitude, float speed, float screenWidth, float screenHeight, Color color, AssetLoader assetLoader) {
		_phase = phase;
		_amplitude = amplitude;
		_speed = speed;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_color = color;
		_assetLoader = assetLoader;
	}
	public Float getStartX() {
		return _startX;
	}
	public void render(SpriteBatch renderer, float cameraX, Color color) {
		render(renderer, cameraX, false, color);
	}
	public void render(SpriteBatch renderer, float cameraX, boolean opposite, Color color) {
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
				dampingFraction *= 0.998f;
			}
			y = (y - _screenHeight / 2.0f) * damping + _screenHeight / 2.0f;
			drawLine(renderer, x, y, prevX, prevY, _screenWidth / 300.0f, color);
			prevY = y;
			prevX = x;
		}
	}
	public void drawLine(SpriteBatch renderer, float x1, float y1, float x2, float y2, float width, Color color) {
		_tmpVector.x = x1 - _screenWidth / 2.0f;
		_tmpVector.y = y1 - _screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		x1 = _screenWidth / 2.0f + _tmpVector.x;
		y1 = _screenHeight / 2.0f + _tmpVector.y;
		_tmpVector.x = x2 - _screenWidth / 2.0f;
		_tmpVector.y = y2 - _screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		x2 = _screenWidth / 2.0f + _tmpVector.x;
		y2 = _screenHeight / 2.0f + _tmpVector.y;
		
		float midX = (x1 + x2) / 2.0f;
		float midY = (y1 + y2) / 2.0f;
		
		_tmpVector.x = x1 - midX;
		_tmpVector.y = y1 - midY;
		float angle = _tmpVector.angle();
		_tmpVector.rotate(-angle + 90);
		x1 = _tmpVector.x + midX;
		y1 = _tmpVector.y + midY;
		
		_tmpVector.x = x2 - midX;
		_tmpVector.y = y2 - midY;
		_tmpVector.rotate(-angle + 90);
		x2 = _tmpVector.x + midX;
		y2 = _tmpVector.y + midY;
		
		_assetLoader.rectangle1.setColor(color);
		_assetLoader.rectangle1.setBounds(x2 - width / 2.0f, y2, width, Math.abs(y2 - y1));
		_assetLoader.rectangle1.setOriginCenter();
		_assetLoader.rectangle1.setRotation(angle - 90);
		_assetLoader.rectangle1.draw(renderer);
	}
	public void drawCircle(SpriteBatch renderer, float x, float y, float r, Color c) {
		_tmpVector.x = x - _screenWidth / 2.0f;
		_tmpVector.y = y - _screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		x = _screenWidth / 2.0f + _tmpVector.x;
		y = _screenHeight / 2.0f + _tmpVector.y;
		_assetLoader.circle2.setColor(c);
		_assetLoader.circle2.setBounds(x - r, y - r, 2 * r, 2 * r);
		_assetLoader.circle2.setOrigin(0.0f, 0.0f);
		_assetLoader.circle2.setRotation(0.0f);
		_assetLoader.circle2.draw(renderer);
	}
	public float normalizeAngle(float angle) {
		return angle - (float) ((int)(angle / 2 / Math.PI) * 2 * Math.PI);
	}
}
