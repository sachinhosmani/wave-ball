package utils;

import com.badlogic.gdx.math.Circle;

public class CircleEnemy {
	public float _x;
	public float _y;
	public float _r;
	public float _speed;
	public Circle shape;
	public CircleEnemy(float x, float y, float r, float speed) {
		_x = x;
		_r = r;
		_y = y;
		_speed = speed;
		shape = new Circle(_x, _y, _r);
	}
	public void updatePosition(float x, float y) {
		_x = x;
		_y = y;
		shape.x = x;
		shape.y = y;
	}
}
