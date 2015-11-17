package utils;

public class PhaseClassifier {
	private float _screenWidth;
	public PhaseClassifier(float screenWidth) {
		_screenWidth = screenWidth;
	}
	public int getPhase(float x) {
		if (x < _screenWidth * 3.0f) {
			return 1;
		}
		if (x < _screenWidth * 6.0f) {
			return 2;
		}
		if (x < _screenWidth * 9.0f) {
			return 3;
		}
		return 4;
	}
}
