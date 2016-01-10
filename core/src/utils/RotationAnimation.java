package utils;

import menus.Button;

public class RotationAnimation implements ButtonAnimation {
	private Button _btn;
	private float _start;
	private float _end;
	private float _speed;
	private TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	private boolean _clockwise = true;
	public RotationAnimation(Button btn, float start, float end, long duration) {
		_btn = btn;
		_start = start;
		_end = end;
		_speed = (end - start) / duration;
	}
	@Override
	public void update() {
		// TODO: Take care of jumps over end points
		long timeElapsed = _timeSnapshot.snapshot();
		if (_clockwise) {
			_btn.angle -= timeElapsed * _speed;
		} else {
			_btn.angle += timeElapsed * _speed;
		}
		if (_btn.angle < Math.min(_start, _end)) {
			_clockwise = false;
		} else if (_btn.angle > Math.max(_start, _end)) {
			_clockwise = true;
		}
	}

	@Override
	public void reset() {
		_btn.angle = _start;
		_timeSnapshot.snapshot();
	}
}
