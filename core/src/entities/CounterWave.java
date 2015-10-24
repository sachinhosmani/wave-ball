package entities;

import java.util.LinkedList;

import utils.CircleEnemy;
import utils.TimeSnapshot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CounterWave extends Wave {
	protected LinkedList<CircleEnemy> _enemies = new LinkedList<CircleEnemy>();
	protected float _radius;
	protected long _lastEnemyLauchTime = System.currentTimeMillis();
	private static final long enemyFrequency = 1000;
	TimeSnapshot _timeSnapshot = new TimeSnapshot();
	public CounterWave(float phase, float amplitude, int frequency, float speed, float radius, float ballX, float screenWidth, float screenHeight, Color color) {
		super(phase, amplitude, frequency, speed, screenWidth, screenHeight, color);
		_radius = radius;
	}
	public void update(float cameraX) {
		if (System.currentTimeMillis() - _lastEnemyLauchTime > enemyFrequency) {
			addEnemy(cameraX);
			_lastEnemyLauchTime = System.currentTimeMillis();
		}
		long timeDiff = _timeSnapshot.snapshot();
		for (CircleEnemy enemy: _enemies) {
			enemy._x += (timeDiff * enemy._speed / 1000.0f);
		}
		tryRemoveEnemy(cameraX);
	}
	private void addEnemy(float cameraX) {
		_enemies.add(new CircleEnemy(cameraX,  _screenWidth / 100, (float) _screenWidth / 2.0f));
	}
	private void tryRemoveEnemy(float cameraX) {
		if (_enemies.size() == 0) {
			return;
		}
		CircleEnemy enemy = _enemies.get(0);
		if (enemy._x > cameraX + _screenWidth) {
			_enemies.remove(0);
		}
	}
	public void render(ShapeRenderer renderer, float screenWidth, float screenHeight, float cameraX) {
		super.render(renderer, cameraX);
		float waveWidth = screenWidth / _frequency;
		float angleToWidth = (float) (waveWidth / (2 * Math.PI));
		for (CircleEnemy enemy: _enemies) {
			float ballAngle = ((int) enemy._x % (int)waveWidth) / angleToWidth;
			float ballY = (float) Math.sin(-ballAngle) * _amplitude + screenHeight / 2;
			drawCircle(renderer, enemy._x, ballY, _radius);
		}
	}
}
