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
		return prefs.getLong("points");
	}
	public long getMaxScore() {
		return prefs.getLong("max_score");
	}
	public void setPoints(long val) {
		prefs.putLong("points", val);
	}
	public void setMaxScore(long val) {
		prefs.putLong("max_score", val);
	}
}
