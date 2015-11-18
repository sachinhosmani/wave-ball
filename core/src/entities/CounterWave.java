package entities;

import java.util.LinkedList;

import utils.AccelerationManager;
import utils.CircleEnemy;
import utils.TimeSnapshot;
import utils.WaveEquation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CounterWave extends Wave {
	public LinkedList<CircleEnemy> _enemies = new LinkedList<CircleEnemy>();
	protected float _radius;
	protected float _lastEnemyLaunchX = 0.0f;
	private float _enemyFrequency;
	private static final Color color = new Color(1.0f, 0.0f, 0.0f, 0.9f);
	private AccelerationManager _accelerationManager;
	private TimeSnapshot _timeSnapshot = new TimeSnapshot();
	private static Color waveColor = new Color(1.0f, 0.2f, 0.3f, 0.5f);
	public CounterWave(float phase, float amplitude, float speed, float radius,
			float ballX, float screenWidth, float screenHeight, Color color, WaveEquation waveEquation) {
		super(phase, amplitude, speed, screenWidth, screenHeight, color);
		_radius = radius;
		_enemyFrequency = screenWidth / 2.7f;
		_accelerationManager = new AccelerationManager(screenWidth, screenHeight, 100 * screenWidth, screenWidth / 100.0f,
				screenWidth / 40.0f, screenWidth / 2.5f, screenWidth / 1.8f, screenWidth / 2.0f, waveEquation);
//		_enemies.add(new CircleEnemy(screenWidth / 2.0f, 0.0f,  _screenWidth / 100, (float) _screenWidth / 2.0f));
//		_enemies.add(new CircleEnemy(3 * screenWidth / 4.0f, screenWidth / 2.0f,  _screenWidth / 100, (float) _screenWidth / 2.0f));
	}
	public void setWaveEquation(WaveEquation waveEquation) {
		_waveEquation = waveEquation;
	}
	public void update(float cameraX) {
		tryAddEnemy(cameraX);
		long timeDiff = _timeSnapshot.snapshot();
		for (CircleEnemy enemy: _enemies) {
			enemy._speed = _accelerationManager.get(enemy._x);
			_waveEquation.getDerivative(enemy._x, _tmpVector);
			float x = enemy._x + (timeDiff * enemy._speed / 1000.0f) / _tmpVector.len() * _screenWidth / 1000.0f;
			_waveEquation.get(x, _tmpVector);
			float y = _tmpVector.y;
			y = _screenHeight / 2.0f + (_screenHeight / 2.0f - y) / 2.0f;
			enemy.updatePosition(x, y);
		}
		tryRemoveEnemy(cameraX);
		
		_enemyFrequency = Math.max(_enemyFrequency - _screenWidth / 10000.0f, _screenWidth / 3.7f); 
	}
	private void tryAddEnemy(float cameraX) {
		if ((cameraX > _screenWidth / 4.0f) && (_enemies.size() == 0 || _enemies.get(_enemies.size() - 1)._x - cameraX > _enemyFrequency)) {
			_enemies.add(new CircleEnemy(cameraX, 0.0f,  _screenWidth / 100, (float) _screenWidth / 2.0f));
		}
	}
	private void tryRemoveEnemy(float cameraX) {
		if (_enemies.size() == 0) {
			return;
		}
		CircleEnemy enemy = _enemies.get(0);
		if (enemy._x > cameraX + _screenWidth || enemy._x < cameraX - _screenWidth) {
			_enemies.remove(enemy);
		}
	}
	public void render(ShapeRenderer renderer, float cameraX) {
		super.render(renderer, cameraX, true, waveColor);
		for (CircleEnemy enemy: _enemies) {
			drawCircle(renderer, enemy._x, enemy._y, _radius, color);
		}
	}
}
