package utils;

import com.badlogic.gdx.math.Vector2;

import menus.Button;

public class ButtonLinearAnimation implements ButtonAnimation {
	private Button _btn;
	private Vector2 _start;
	private Vector2 _end;
	private long _duration;
	private float _screenWidth, _screenHeight;
	private Vector2 _speed = new Vector2();
	private TimeSnapshot _timeSnapshot = TimeSnapshotStore.get();
	public ButtonLinearAnimation(Button btn, Vector2 start, Vector2 end, long duration, float screenWidth, float screenHeight) {
		_btn = btn;
		_start = start;
		_end = end;
		_duration = duration;
		_speed.x = (end.x - start.x) / duration;
		_speed.y = (end.y - start.y) / duration;
		_timeSnapshot.snapshot();
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
	}
	@Override
	public void update() {
		// TODO: Take care of jumps over end points
		long timeElapsed = _timeSnapshot.snapshot();
		if (Math.abs(_btn.centerX - _end.x * _screenWidth) > _screenWidth / 50.0f) {
			_btn.setCenterX(_btn.centerX + timeElapsed * _speed.x * _screenWidth);
		}
		if (Math.abs(_btn.centerY - _end.y * _screenHeight) > _screenHeight / 50.0f) {
			_btn.setCenterY(_btn.centerY + timeElapsed * _speed.y * _screenHeight);
		}
	}

	@Override
	public void reset() {
		_btn.setCenterX(_start.x * _screenWidth);
		_btn.setCenterY(_start.y * _screenHeight);
		_timeSnapshot.snapshot();
	}
	
}
