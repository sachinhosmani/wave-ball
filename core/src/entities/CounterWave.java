package entities;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.CircleEntity.Type;
import entities.Wave.CircleType;
import utils.AccelerationManager;
import utils.AssetLoader;
import utils.TimeSnapshot;
import utils.TimeSnapshotStore;
import utils.WaveEquation;

public class CounterWave extends Wave {
	public LinkedList<CircleEntity> _circles = new LinkedList<CircleEntity>();
	protected float _lastEnemyLaunchX = 0.0f;
	private float _enemyFrequency;
	private static final Color _enemyColor = new Color(1.0f, 0.0f, 0.0f, 0.9f);
	private static final Color _diamondColor = new Color(1.0f, 0.7f, 0.0f, 0.9f);
	private AccelerationManager _accelerationManager;
	private TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	private float _enemyRadius;
	private static Color waveColor = new Color(1.0f, 0.2f, 0.3f, 0.5f);
	public CounterWave(float phase, float amplitude, float speed, float radius,
			float ballX, float screenWidth, float screenHeight, Color color, WaveEquation waveEquation, AssetLoader assetLoader) {
		super(phase, amplitude, speed, screenWidth, screenHeight, color, assetLoader);
		_enemyFrequency = screenWidth / 1.2f;
		_enemyRadius = radius;
		_accelerationManager = new AccelerationManager(screenWidth, screenHeight,
				screenWidth / 40.0f, screenWidth / 2.2f, screenWidth / 1.6f, screenWidth / 2.0f, waveEquation);
	}
	public void setWaveEquation(WaveEquation waveEquation) {
		_waveEquation = waveEquation;
	}
	public void destroyDiamond(CircleEntity diamond) {
		_circles.remove(diamond);
	}
	public void update(float cameraX) {
		super.update();
		tryAddEnemyOrDiamond(cameraX);
		long timeDiff = _timeSnapshot.snapshot();
		for (CircleEntity enemy: _circles) {
			enemy._speed = _accelerationManager.get(enemy._x);
			_waveEquation.getDerivative(enemy._x, _tmpVector);
			float x = enemy._x + (timeDiff * enemy._speed / 1000.0f) / _tmpVector.len() * _screenWidth / 750.0f;
			_waveEquation.get(x, _tmpVector);
			float y = _tmpVector.y;
			y = _screenHeight / 2.0f + (_screenHeight / 2.0f - y) / 2.0f;
			enemy.updatePosition(x, y);
		}
		tryRemoveEnemyOrDiamond(cameraX);
		
		_enemyFrequency = Math.max(_screenWidth / 1.2f - (cameraX / (_screenWidth * 100.0f)) * _screenWidth / 2.0f, _screenWidth / 2.6f); 
	}
	private void tryAddEnemyOrDiamond(float cameraX) {
		if ((cameraX > _screenWidth * 3.75f) && (_circles.size() == 0 || _circles.get(_circles.size() - 1)._x - cameraX > _enemyFrequency)) {
			if (Math.random() < 0.9 || _circles.size() < 2) {
				_circles.add(new CircleEntity(cameraX - _screenWidth * 0.1f, 0.0f,  _enemyRadius, (float) _screenWidth / 2.0f));
			} else {
				_circles.add(new CircleEntity(cameraX - _screenWidth * 0.1f, 0.0f,  _enemyRadius, (float) _screenWidth / 2.0f, CircleEntity.Type.DIAMOND));
			}
		}
	}
	private void tryRemoveEnemyOrDiamond(float cameraX) {
		if (_circles.size() == 0) {
			return;
		}
		CircleEntity enemy = _circles.get(0);
		if (enemy._x > cameraX + _screenWidth * 1.1f || enemy._x < cameraX - _screenWidth * 1.1f) {
			_circles.remove(enemy);
		}
	}
	public void render(SpriteBatch renderer, float cameraX) {
		super.render(renderer, cameraX, true, waveColor);
		for (CircleEntity enemy: _circles) {
			_waveEquation.getDerivative(enemy._x, _tmpVector);
			drawCircle(renderer, enemy._x, enemy._y, enemy._type == Type.DIAMOND ? _diamondRadius : _enemyRadius, 0.0f, enemy._type == Type.DIAMOND ? CircleType.DIAMOND : CircleType.ENEMY);
		}
	}
}
