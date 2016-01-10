package utils;

import menus.Button;

public class ButtonScaleAnimation implements ButtonAnimation {
	private Button _btn;
	private float _start;
	private float _end;
	private float _speed;
	private TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	private boolean _increasing = true;
	public ButtonScaleAnimation(Button btn, float start, float end, long duration) {
		_btn = btn;
		_start = start;
		_end = end;
		_speed = (end - start) / duration;
	}
	@Override
	public void update() {
		// TODO: Take care of jumps over end points
		long timeElapsed = _timeSnapshot.snapshot();
		if (_increasing) {
			_btn.scale(_btn.scale + timeElapsed * _speed);
		} else {
			_btn.scale(_btn.scale - timeElapsed * _speed);
		}
		if (_btn.scale < Math.min(_start, _end)) {
			_increasing = true;
		} else if (_btn.scale > Math.max(_start, _end)) {
			_increasing = false;
		}
	}

	@Override
	public void reset() {
		_btn.angle = _start;
		_timeSnapshot.snapshot();
	}
}
