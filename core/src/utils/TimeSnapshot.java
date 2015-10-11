package utils;

public class TimeSnapshot {
	private long lastTime;
	public TimeSnapshot() {
		lastTime = System.currentTimeMillis();
	}
	public long snapshot() {
		long timeNow = System.currentTimeMillis();
		long timeElapsed = timeNow - lastTime;
		lastTime = timeNow;
		return timeElapsed;
	}
	public long viewLastTime() {
		return lastTime;
	}
}
