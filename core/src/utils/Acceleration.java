package utils;

public class Acceleration {
	private float _maxSpeed;
	private float _acceleration;
	private float _deceleration;
	private float _speed;
	private boolean stopping = true;
	public Acceleration(float acceleration, float deceleration, float maxSpeed) {
		_acceleration = acceleration;
		_maxSpeed = maxSpeed;
		_deceleration = deceleration;
	}
	public float getSpeed() {
		return _speed;
	}
	public void stop() {
		stopping = true;
	}
	public void start() {
		stopping = false;
	}
	public void update(float delta) {
		if (stopping) {
			_speed = Math.max(0.0f, _speed - delta * _deceleration);
		} else {
			_speed = Math.min(_maxSpeed, _speed + delta * _acceleration);
		}
	}
	public void setMaxSpeed(float maxSpeed) {
		_maxSpeed = maxSpeed;
	}
}
