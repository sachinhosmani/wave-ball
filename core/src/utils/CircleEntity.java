package utils;

import com.badlogic.gdx.math.Circle;

public class CircleEntity {
	public float _x;
	public float _y;
	public float _r;
	public float _speed;
	public Circle shape;
	public enum Type {
		DIAMOND,
		ENEMY
	};
	public Type _type;
	public CircleEntity(float x, float y, float r, float speed) {
		this(x, y, r, speed, Type.ENEMY);
	}
	public CircleEntity(float x, float y, float r, float speed, Type type) {
		_x = x;
		_r = r;
		_y = y;
		_speed = speed;
		shape = new Circle(_x, _y, _r);
		_type = type;
	}
	public void updatePosition(float x, float y) {
		_x = x;
		_y = y;
		shape.x = x;
		shape.y = y;
	}
}
