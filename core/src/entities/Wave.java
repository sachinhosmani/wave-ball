package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.Constants;
import utils.TimeSnapshot;
import utils.TimeSnapshotStore;
import utils.WaveEquation;

public class Wave {
	protected float _phase;
	protected float _amplitude;
	protected float _speed;
	protected static int numComponentsPerWave = 50;
	protected TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	protected float _screenWidth;
	protected float _screenHeight;
	protected WaveEquation _waveEquation;
	protected Float _startX = null;
	protected Vector2 _tmpVector = new Vector2();
	protected Vector2 _tmpVector1 = new Vector2();
	protected AssetLoader _assetLoader;
	private float vertices[] = new float[5 * 4];
	private float _color;
	private boolean _plusOne;
	private long _plusOneTime;
	private final long _plusOneTotalTime = 1000;
	private float _plusOneX, _plusOneY;
	protected TimeSnapshot _plusOneTimeSnapshot = TimeSnapshotStore.get();
	protected float _diamondRadius;
	
	public enum CircleType {
		BALL,
		ENEMY,
		HERO,
		DIAMOND,
		INVISIBLE
	};
	
	public enum WaveType {
		MAIN,
		COUNTER
	};
	
	public Wave(float phase, float amplitude, float speed, float screenWidth, float screenHeight, Color color, AssetLoader assetLoader) {
		_phase = phase;
		_amplitude = amplitude;
		_speed = speed;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_color = color.toFloatBits();
		_assetLoader = assetLoader;
		_diamondRadius = _screenWidth / 50.0f;
	}
	public Float getStartX() {
		return _startX;
	}
	public void render(SpriteBatch renderer, float cameraX, Color color) {
		render(renderer, cameraX, false, color);
	}
	public void render(SpriteBatch renderer, float cameraX, boolean opposite, Color color) {
		float y = 0.0f;
		float prevX = (_startX != null) ? _startX : Math.max(0.0f, cameraX - _screenWidth /10.0f);
		_waveEquation.get(prevX, _tmpVector);
		float prevY = _tmpVector.y;
		if (opposite) {
			prevY = _screenHeight / 2.0f + (_screenHeight / 2.0f - prevY) / 2.0f;
		}
		float damping = 1.0f;
		float dampingFraction = 1.0f;
		boolean firstTime = true;
		for (float x = Math.max(0.0f, cameraX - _screenWidth /10.0f); x <= cameraX + _screenWidth * 1.1f; ) {
			x += _screenWidth / 200.0f / Math.min(Math.max(Math.sqrt(_tmpVector.len()), 1.0f), 20.0f);
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
//			y = (y - _screenHeight / 2.0f) * damping + _screenHeight / 2.0f;
			drawLine(renderer, x, y, prevX, prevY, _screenWidth / 300.0f, opposite ? WaveType.COUNTER : WaveType.MAIN);
			prevY = y;
			prevX = x;
			_waveEquation.getDerivative(x, _tmpVector);
		}
		plusOneRender(renderer);
	}

	public void drawLine(SpriteBatch renderer, float x1, float y1, float x2, float y2, float width, WaveType type) {
		if (y1 > y2) {
			float tmp = y1;
			y1 = y2;
			y2 = tmp;
			tmp = x1;
			x1 = x2;
			x2 = tmp;
		}
		
		_tmpVector1.x = x1 - _screenWidth / 2.0f;
		_tmpVector1.y = y1 - _screenHeight / 2.0f;
		_tmpVector1.rotate(Constants.rotation);
		x1 = _screenWidth / 2.0f + _tmpVector1.x;
		y1 = _screenHeight / 2.0f + _tmpVector1.y;
		_tmpVector1.x = x2 - _screenWidth / 2.0f;
		_tmpVector1.y = y2 - _screenHeight / 2.0f;
		_tmpVector1.rotate(Constants.rotation);
		x2 = _screenWidth / 2.0f + _tmpVector1.x;
		y2 = _screenHeight / 2.0f + _tmpVector1.y;
		
		float midX = (x1 + x2) / 2.0f;
		float midY = (y1 + y2) / 2.0f;
		
		_tmpVector1.x = x1 - midX;
		_tmpVector1.y = y1 - midY;
		float angle = _tmpVector1.angle();
		_tmpVector1.rotate(-angle - 90);
		x1 = _tmpVector1.x + midX;
		y1 = _tmpVector1.y + midY;
		
		_tmpVector1.x = x2 - midX;
		_tmpVector1.y = y2 - midY;
		angle = _tmpVector1.angle();
		_tmpVector1.rotate(-angle + 90);
		x2 = _tmpVector1.x + midX;
		y2 = _tmpVector1.y + midY;

		Sprite wave = type == WaveType.MAIN ? _assetLoader.wave1 : _assetLoader.wave2;
		wave.setBounds(x1 - width / 2.0f, y1, width, Math.abs(y2 - y1));
		wave.setOriginCenter();
		wave.setRotation(angle - 90);
		wave.draw(renderer);
		
//		y1 -= _screenWidth / 500.0f;
//		y2 -= _screenWidth / 500.0f;
//		Sprite wave = _assetLoader.shadow;
//		wave.setBounds(x1 - width / 2.0f, y1, width, Math.abs(y2 - y1));
//		wave.setOriginCenter();
//		wave.setRotation(angle - 90);
//		wave.draw(renderer);

//		float xa = x1 - width / 2.0f;
//		float ya = y1;
//		float xb = x1 + width / 2.0f;
//		float yb = y1;
//		float xc = x1 + width / 2.0f;
//		float yc = y2;
//		float xd = x1 - width / 2.0f;
//		float yd = y2;
//		
//		_tmpVector.x = xa - midX;
//		_tmpVector.y = ya - midY;
//		_tmpVector.rotate(angle - 90);
//		xa = midX + _tmpVector.x;
//		ya = midY + _tmpVector.y;
//		_tmpVector.x = xb - midX;
//		_tmpVector.y = yb - midY;
//		_tmpVector.rotate(angle - 90);
//		xb = midX + _tmpVector.x;
//		yb = midY + _tmpVector.y;
//		_tmpVector.x = xc - midX;
//		_tmpVector.y = yc - midY;
//		_tmpVector.rotate(angle - 90);
//		xc = midX + _tmpVector.x;
//		yc = midY + _tmpVector.y;
//		_tmpVector.x = xd - midX;
//		_tmpVector.y = yd - midY;
//		_tmpVector.rotate(angle - 90);
//		xd = midX + _tmpVector.x;
//		yd = midY + _tmpVector.y;
		
//		System.out.print("(" + xa + "," + ya + ") ");
//		System.out.print("(" + xb + "," + yb + ") ");
//		System.out.print("(" + xc + "," + yc + ") ");
//		System.out.println("(" + xd + "," + yd + ") ");
//		
//	  vertices[Batch.X1] = xa;
//	  vertices[Batch.Y1] = ya;
//	  vertices[Batch.C1] = _color;
//	  vertices[Batch.U1] = 0f;
//	  vertices[Batch.V1] = 0f;
//	 
//	  vertices[Batch.X2] = xb;
//	  vertices[Batch.Y2] = yb;
//	  vertices[Batch.C2] = _color;
//	  vertices[Batch.U2] = 1f;
//	  vertices[Batch.V2] = 0f;
//	 
//	  vertices[Batch.X3] = xc;
//	  vertices[Batch.Y3] = yc;
//	  vertices[Batch.C3] = _color;
//	  vertices[Batch.U3] = 1f;
//	  vertices[Batch.V3] = 1f;
//	 
//	  vertices[Batch.X4] = xd;
//	  vertices[Batch.Y4] = yd;
//	  vertices[Batch.C4] = _color;
//	  vertices[Batch.U4] = 0f;
//	  vertices[Batch.V4] = 1f;
//	 
//	  renderer.draw(_assetLoader.rectangle1.getTexture(), vertices, 0, vertices.length);
	}
	public void drawCircle(SpriteBatch renderer, float x, float y, float r, CircleType type) {
		_tmpVector.x = x - _screenWidth / 2.0f;
		_tmpVector.y = y - _screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		x = _screenWidth / 2.0f + _tmpVector.x;
		y = _screenHeight / 2.0f + _tmpVector.y;
//		_assetLoader.circle2.setColor(c);
		
		Sprite asset;
		switch (type) {
		case BALL:
			asset = _assetLoader.ball;
			break;
		case ENEMY:
			asset = _assetLoader.enemy;
			break;
		case DIAMOND:
			asset = _assetLoader.diamond;
			break;
		case HERO:
			asset = _assetLoader.hero;
			break;
		default:
			asset = _assetLoader.ball;
		}
		asset.setBounds(x - r, y - r, 2 * r, 2 * r);
		if (type == CircleType.INVISIBLE) {
			asset.setAlpha(0.4f);
		} else {
			asset.setAlpha(1.0f);
		}
		asset.setOrigin(0.0f, 0.0f);
		asset.setRotation(0.0f);
		asset.draw(renderer);
	}
	public float normalizeAngle(float angle) {
		return angle - (float) ((int)(angle / 2 / Math.PI) * 2 * Math.PI);
	}
	public void plusOne(float x, float y) {
		if (_plusOne) {
			return;
		}
		_plusOne = true;
		_plusOneTimeSnapshot.snapshot();
		_plusOneX = x;
		_plusOneY = y;
	}
	protected void plusOneUpdate() {
		if (!_plusOne) {
			return;
		}
		_plusOneTime += _plusOneTimeSnapshot.snapshot();
		if (_plusOneTime >= _plusOneTotalTime) {
			_plusOne = false;
			_plusOneTime = 0;
		}
	}
	protected void plusOneRender(SpriteBatch batcher) {
		if (_plusOne) {
			float fraction = ((float) _plusOneTime) / _plusOneTotalTime;
			_plusOneY *= 1.005f;
			_tmpVector.x = _plusOneX - _screenWidth / 2.0f;
			_tmpVector.y = _plusOneY - _screenHeight / 2.0f;
			_tmpVector.rotate(Constants.rotation);
			
			float size = _screenWidth / 25.0f * (0.3f + 0.7f * fraction);
			_assetLoader.plusOne.setBounds(_tmpVector.x + _screenWidth / 2.0f, _tmpVector.y + _screenHeight / 2.0f, size,  size);
			_assetLoader.plusOne.draw(batcher);
		}
	}
	protected void update() {
		plusOneUpdate();
	}
}
