package utils;

public class ScoreManager {
	private TimeSnapshot _timeSnapshot = new TimeSnapshot();
	private long _score = 0;
	public void update() {
		long timeElapsed = _timeSnapshot.snapshot();
		_score += timeElapsed;
	}
	public void reset() {
		_score = 0;
	}
	public long getScore() {
		return _score / 2000;
	}
}
