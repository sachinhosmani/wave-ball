package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import utils.TimeSnapshot;

public class Rotator {
	private float _angle;
	private float _speed;
	private float _baseAngle;
	private TimeSnapshot _timeSnapshot;
	private long _waitingTime = 0;
	private long _pauseDuration = 0;
	protected float _x;
	protected float _y;
	private float _screenWidth;
	private float _screenHeight;
	private Vector2 point1;
	private Vector2 point2;
	private Vector2 point3;
	private Vector2 point4;
	private Vector2 line11, line12;
	private Vector2 line21, line22;
	private boolean _clockwise;
	private boolean _alternating;
	private Vector2 _tmpVector;
	private Color color = new Color(1.0f, 0.0f, 0.0f, 0.8f);
	public Rotator(float angle, float speed, float baseAngle, long pauseDuration,
			float x, float y, float screenWidth, float screenHeight, float rotatorWidth,
			boolean clockwise, boolean alternating) {
		_angle = angle;
		_speed = speed;
		_baseAngle = baseAngle;
		_timeSnapshot = new TimeSnapshot();
		_pauseDuration = pauseDuration;
		_x = x;
		_y = y;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		float rotatorLength = _screenWidth / 25.0f;
		point1 = new Vector2(-rotatorLength / 2.0f, -rotatorWidth / 2.0f);
		point2 = new Vector2(rotatorLength / 2.0f, -rotatorWidth / 2.0f);
		point3 = new Vector2(-rotatorLength / 2.0f, rotatorWidth / 2.0f);
		point4 = new Vector2(rotatorLength / 2.0f, rotatorWidth / 2.0f);
		line11 = new Vector2(_x + point1.x, _y + point1.y);
		line12 = new Vector2(_x + point2.x, _y + point2.y);
		line21 = new Vector2(_x + point3.x, _y + point3.y);
		line22 = new Vector2(_x + point4.x, _y + point4.y);
		_clockwise = clockwise;
		_alternating = alternating;
		_tmpVector = new Vector2();
	}
	public float getSpeed() {
		return _speed;
	}
	public void update() {
		long timeSince = _timeSnapshot.snapshot();
		if (Math.abs(_angle - _baseAngle) < 0.01 || Math.abs(_angle - Math.PI - _baseAngle) < 0.01 ||
				Math.abs(_angle + Math.PI - _baseAngle) < 0.01) {
			_waitingTime += timeSince;
			if (_waitingTime > _pauseDuration) {
				_waitingTime = 0;
				_angle += _speed * timeSince * (_clockwise ? -1 : 1);
			}
		} else {
			_angle += _speed * timeSince * (_clockwise ? -1 : 1);
		}
		if (_angle > 2 * Math.PI) {
			_angle -= 2 * Math.PI;
		}
		
		point1.rotateRad(_angle);
		point2.rotateRad(_angle);
		point3.rotateRad(_angle);
		point4.rotateRad(_angle);
		
		line11.x = (_x + point1.x);
		line11.y = (_y + point1.y);
		line12.x = (_x + point2.x);
		line12.y = (_y + point2.y);
		line21.x = (_x + point3.x);
		line21.y = (_y + point3.y);
		line22.x = (_x + point4.x);
		line22.y = (_y + point4.y);
		
		point1.rotateRad(-_angle);
		point2.rotateRad(-_angle);
		point3.rotateRad(-_angle);
		point4.rotateRad(-_angle);
	}
	public void render(ShapeRenderer renderer) {
		point1.rotateRad(_angle);
		point2.rotateRad(_angle);
		point3.rotateRad(_angle);
		point4.rotateRad(_angle);
		
		drawLine(renderer, _x + point1.x, _y + point1.y, _x + point2.x, _y + point2.y, _screenWidth / 100.0f);
		drawLine(renderer, _x + point3.x, _y + point3.y, _x + point4.x, _y + point4.y, _screenWidth / 100.0f);
		
		point1.rotateRad(-_angle);
		point2.rotateRad(-_angle);
		point3.rotateRad(-_angle);
		point4.rotateRad(-_angle);
	}
	public void drawLine(ShapeRenderer renderer, float x1, float y1, float x2, float y2, float width) {
		_tmpVector.x = x1 - _screenWidth / 2.0f;
		_tmpVector.y = y1 - _screenHeight / 2.0f;
		_tmpVector.rotate(20);
		x1 = _screenWidth / 2.0f + _tmpVector.x;
		y1 = _screenHeight / 2.0f + _tmpVector.y;
		_tmpVector.x = x2 - _screenWidth / 2.0f;
		_tmpVector.y = y2 - _screenHeight / 2.0f;
		_tmpVector.rotate(20);
		x2 = _screenWidth / 2.0f + _tmpVector.x;
		y2 = _screenHeight / 2.0f + _tmpVector.y;
		renderer.setColor(color);
		renderer.rectLine(x1, y1, x2, y2, width);
	}
	public boolean checkCollision(Vector2 center, float radius) {
		return Intersector.intersectSegmentCircle(line11, line12, center, radius) ||
			 Intersector.intersectSegmentCircle(line21, line22, center, radius);
	}
}
