package entities;

import java.util.LinkedList;

import sun.java2d.loops.DrawRect;
import utils.AccelerationManager;
import utils.CircleEnemy;
import utils.RectangleEnemy;
import utils.TimeSnapshot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StraightLine {
	protected LinkedList<RectangleEnemy> _enemies = new LinkedList<RectangleEnemy>();
	protected float _radius;
	protected long _lastEnemyLauchTime = System.currentTimeMillis();
	private static final long enemyFrequency = 1000;
	private float _screenWidth;
	TimeSnapshot _timeSnapshot = new TimeSnapshot();
	public StraightLine(float speed, float screenWidth, float screenHeight, Color color) {
		_screenWidth = screenWidth;
		_radius = _screenWidth / 100;
	}
	public void update(float cameraX) {
		if (System.currentTimeMillis() - _lastEnemyLauchTime > enemyFrequency) {
			addEnemy(cameraX);
			_lastEnemyLauchTime = System.currentTimeMillis();
		}
		long timeDiff = _timeSnapshot.snapshot();
		for (RectangleEnemy enemy: _enemies) {
			enemy._speed += Math.max(0.0f, timeDiff * AccelerationManager.getAcceleration(enemy._x));
			enemy._x += (timeDiff * enemy._speed / 1000.0f);
		}
		tryRemoveEnemy(cameraX);
	}
	private void addEnemy(float cameraX) {
		float enemyWidth = _screenWidth / 10 + (float) Math.random() * _screenWidth / 10;
		_enemies.add(new RectangleEnemy(cameraX, enemyWidth, (float) _screenWidth / 30f, _screenWidth / 2.0f));
	}
	private void tryRemoveEnemy(float cameraX) {
		if (_enemies.size() == 0) {
			return;
		}
		RectangleEnemy enemy = _enemies.get(0);
		if (enemy._x > cameraX + _screenWidth) {
			_enemies.remove(0);
		}
	}
	public void render(ShapeRenderer renderer, float screenWidth, float screenHeight, float cameraX) {
		drawLine(renderer, cameraX, screenHeight / 2, cameraX + screenWidth, screenHeight / 2, screenHeight / 100);
		for (RectangleEnemy enemy: _enemies) {
			drawRectangle(renderer, enemy._x, screenHeight / 2 - enemy._h / 2, enemy._w, enemy._h);
		}
	}
	public void drawLine(ShapeRenderer renderer, float x1, float y1, float x2, float y2, float width) {
		renderer.setColor(0.0f, 1.0f, 0.0f, 0.5f);
		renderer.rectLine(x1, y1, x2, y2, width);
	}
	public void drawRectangle(ShapeRenderer renderer, float x, float y, float w, float h) {
		renderer.setColor(0.0f, 1.0f, 0.0f, 0.5f);
		renderer.rect(x, y, w, h);
	}
}
