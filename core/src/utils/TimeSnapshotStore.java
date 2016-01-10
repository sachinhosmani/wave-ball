package utils;

import java.util.ArrayList;

public class TimeSnapshotStore {
	private static ArrayList<TimeSnapshot> _snapshots = new ArrayList<TimeSnapshot>();
	public static TimeSnapshot get() {
		TimeSnapshot snapshot = new TimeSnapshot();
		_snapshots.add(snapshot);
		return snapshot;
	}
	public static void resume() {
		for (TimeSnapshot snapshot: _snapshots) {
			snapshot.snapshot();
		}
	}
	public static void clear() {
		_snapshots.clear();
	}
}
