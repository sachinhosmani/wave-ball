package utils;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class AccelerationManager {
	private CatmullRomSpline<Vector2> spline;
	private float _xMax;
	private Vector2 _tmpVector = new Vector2();
	private PhaseClassifier _phaseClassifier;
	private float _baseAccelerationDelta;
	public AccelerationManager(float screenWidth, float screenHeight,  float xMax, float granularity,
			float baseAcceleration, float minSpeed, float maxSpeed, float accelerationDeltaBase,
			WaveEquation waveEquation) {
		_xMax = xMax;
		_phaseClassifier = new PhaseClassifier(screenWidth);
		int size = (int) (xMax / granularity);
		Vector2[] controlPoints = new Vector2[size];
		float speed = screenWidth / 2.0f;
		int i = 0;
		float acceleration = baseAcceleration;
		float lastAccelerationChangeX = 0.0f;
		_baseAccelerationDelta = accelerationDeltaBase;
		float accelerationDelta = getAccelerationDelta();
		
		float x = 0.0f;
		for (; _phaseClassifier.getPhase(x) == 1; x += granularity) {
			controlPoints[i++] = new Vector2(x, speed);
		}
		
		for (; i < size; x += granularity) {
			if (x - lastAccelerationChangeX > accelerationDelta) {
				acceleration = -acceleration;
				lastAccelerationChangeX = x;
				accelerationDelta = getAccelerationDelta();
			}
			speed += x / xMax * acceleration;
			speed = Math.max(minSpeed, speed);
			speed = Math.min(speed, maxSpeed);
			controlPoints[i++] = new Vector2(x, speed);
		}
		
//		for (; i < size; x += granularity) {
//			if (x - lastAccelerationChangeX > accelerationDelta) {
//				acceleration = -acceleration;
//				lastAccelerationChangeX = x;
//			}
//			waveEquation.get(x, _tmpVector);
//			speed = minSpeed + (Math.abs(_tmpVector.y - screenHeight / 2.0f) * 5 - minSpeed) * 2.0f;
//			speed = Math.max(minSpeed, speed);
//			speed = Math.min(speed, maxSpeed);
//			controlPoints[i++] = new Vector2(x, speed);
//		}
//		
		spline = new CatmullRomSpline<Vector2>(controlPoints, true);
	}
	public float get(float x) {
		while (x >= _xMax) {
			x -= _xMax;
		}
		spline.valueAt(_tmpVector, x / _xMax);
		return _tmpVector.y;
	}
	public float getAccelerationDelta() {
		return _baseAccelerationDelta * MathUtils.random(0.3f, 1.5f);
	}
}
