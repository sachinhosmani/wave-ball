package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferenceManager {
	private String preferenceNamespace = "wave.ball.applegloss.prefs";
	private Preferences prefs = Gdx.app.getPreferences(preferenceNamespace);
	public PreferenceManager() {
	}
	public boolean getPref(String key, boolean def) {
		return prefs.getBoolean(key, def);
	}
	public int getPref(String key, int def) {
		return prefs.getInteger(key, def);
	}
	public float getPref(String key, float def) {
		return prefs.getFloat(key, def);
	}
	public void setPref(String key, boolean val) {
		prefs.putBoolean(key, val);
		prefs.flush();
	}
	public void setPref(String key, int val) {
		prefs.putInteger(key, val);
		prefs.flush();
	}
	public void setPref(String key, float val) {
		prefs.putFloat(key, val);
		prefs.flush();
	}
	
	public boolean firstTime() {
		return !prefs.contains("dirty");
	}
	public void dirty() {
		prefs.putBoolean("dirty", true);
	}
	
	public void rated() {
		prefs.putBoolean("rated", true);
		prefs.flush();
	}
	public boolean isRated() {
		return prefs.contains("rated");
	}

	public long getPoints() {
		return prefs.getLong("points", 0);
	}
	public long getMaxScore() {
		return prefs.getLong("max_score", 0);
	}
	public void setPoints(long val) {
		prefs.putLong("points", val);
		prefs.flush();
	}
	public void setMaxScore(long val) {
		prefs.putLong("max_score", val);
		prefs.flush();
	}
	public long getLastScore() {
		return prefs.getLong("last_score", -1);
	}
	public void setLastScore(long val) {
		prefs.putLong("last_score", val);
		prefs.flush();
	}
	public long getSelectedBall() {
		return prefs.getLong("selected_ball", 0);
	}
	public void setSelectedBall(long val) {
		prefs.putLong("selected_ball", val);
		prefs.flush();
	}
	public long getMaxBallUnlock() {
		return prefs.getLong("max_ball_unlock", 0);
	}
	public void setMaxBallUnlock(long val) {
		prefs.putLong("max_ball_unlock", val);
		prefs.flush();
	}
}
