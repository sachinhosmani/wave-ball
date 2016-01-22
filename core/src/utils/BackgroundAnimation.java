package utils;

import com.badlogic.gdx.math.MathUtils;

public class BackgroundAnimation {
	private boolean _left = Math.random() > 0.5;
	private boolean _down = Math.random() > 0.5;
	private float _vOffset = 0.0f;
	private float _hOffset = 0.0f;
	private TimeSnapshot _timeSnapshot;
	private float _speed;
	private float x, y;
	private float _boundsX, _boundsY;
	private long _timeSinceXChange = 0;
	private long _timeSinceYChange = 0;
	private long _timeX, _timeY;
	
	private float TOTAL_TIME = 10000;
	public BackgroundAnimation(float boundsX, float boundsY) {
		_timeSnapshot = TimeSnapshotStore.get();
		_speed = boundsX / TOTAL_TIME;
		setXTime();
		setYTime();
		_boundsX = boundsX;
		_boundsY = boundsY;
	}
	private void setXTime() {
		_timeX = 6 * (long) (TOTAL_TIME * MathUtils.random(0.1f, 1.0f));
	}
	private void setYTime() {
		_timeY = 6 * (long) (TOTAL_TIME * MathUtils.random(0.1f, 1.0f));
	}
	public void update() {
		long timeElapsed = _timeSnapshot.snapshot();
		
		_timeSinceXChange += timeElapsed;
		_timeSinceYChange += timeElapsed;
//		if (_timeSinceXChange > _timeX) {
//			setXTime();
//			_timeSinceXChange = 0;
//			_left = !_left;
//		}
//		if (_timeSinceYChange > _timeY) {
//			setYTime();
//			_timeSinceYChange = 0;
//			_down = !_down;
//		}
		
		if (_left) {
			x -= timeElapsed * getSpeed();
		} else {
			x += timeElapsed * getSpeed();
		}
		if (_left) {
			if (x - timeElapsed * getSpeed() <= -_boundsX) {
				_timeSinceXChange = 0;
				_left = false;
			}
		} else {
			if (x + timeElapsed * getSpeed() >= _boundsX) {
				_timeSinceXChange = 0;
				_left = true;
			}
		}
		if (_down) {
			y -= timeElapsed * getSpeed();
		} else {
			y += timeElapsed * getSpeed();
		}
		if (_down) {
			if (y - timeElapsed * getSpeed() <= -_boundsY) {
				_down = false;
				_timeSinceYChange = 0;
			}
		} else {
			if (y + timeElapsed * getSpeed() >= _boundsY) {
				_down = true;
				_timeSinceYChange = 0;
			}
		}
	}
	public float getSpeed() {
		return _speed / ((Math.abs(Math.max(x, y)) / Math.abs(Math.max(_boundsX, _boundsY))) + 1.0f);
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
}
