package utils;

public class AccelerationManager {
	public static int PERIOD;
	public static float screenWidth;
	public static float getAcceleration(float x) {
		int moduloX = ((int)x) % PERIOD;
		if (moduloX < PERIOD / 4.0f) {
			return 0.0f;
		} else if (moduloX < PERIOD / 2.0f) {
			return screenWidth / 100.0f;
		} else if (moduloX < 3.0f * PERIOD / 4.0f) {
			return -screenWidth / 100.0f;
		} else if (moduloX < PERIOD) {
			return 0.0f;
		}
		return 0.0f;
	}
}
