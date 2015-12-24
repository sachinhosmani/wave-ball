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
	private static Vector2[] controlPoints;
	public static float xMax;
	public static float granularity;
	public static void initSize(float aXMax, float aGranularity) {
		xMax = aXMax;
		granularity = aGranularity;
		int size = (int) (xMax / granularity);
		controlPoints = new Vector2[size];
		for (int i = 0; i < size; i++) {
			controlPoints[i] = new Vector2();
		}
	}
	public AccelerationManager(float screenWidth, float screenHeight,
			float baseAcceleration, float minSpeed, float maxSpeed, float accelerationDeltaBase,
			WaveEquation waveEquation) {
		_xMax = xMax;
		_phaseClassifier = new PhaseClassifier(screenWidth);
		int size = (int) (xMax / granularity);
		float speed = screenWidth / 1.5f;
		int i = 0;
		float acceleration = baseAcceleration;
		float lastAccelerationChangeX = 0.0f;
		_baseAccelerationDelta = accelerationDeltaBase;
		float accelerationDelta = getAccelerationDelta();
		
		boolean constantSpeed = false;
		float x = 0.0f;
		for (; i < size && _phaseClassifier.getPhase(x) <= 2; x += granularity) {
			if (x - lastAccelerationChangeX > accelerationDelta) {
				acceleration = -acceleration;
				lastAccelerationChangeX = x;
				accelerationDelta = getAccelerationDelta();
			}
			speed += x / xMax * acceleration / 5.0f;
			speed = Math.max(minSpeed, speed);
			speed = Math.min(speed, maxSpeed / 1.5f);
			controlPoints[i].x = x;
			controlPoints[i].y = speed;
			i++;
		}
		
		for (; i < size; x += granularity) {
			if (x - lastAccelerationChangeX > accelerationDelta) {
				if (constantSpeed) {
					// speeding up
					if (Math.random() > 0.5) {
						acceleration = baseAcceleration;
					}
				} else {
					// slowing down
					acceleration = -acceleration;
				}
				lastAccelerationChangeX = x;
				accelerationDelta = getAccelerationDelta();
			}
			speed += x / xMax * acceleration;
			if (speed < minSpeed) {
				constantSpeed = true;
				acceleration = 0.0f;
			}
			if (speed > maxSpeed) {
				acceleration = -baseAcceleration;
			}
			speed = Math.max(minSpeed, speed);
			speed = Math.min(speed, maxSpeed);
			controlPoints[i].x = x;
			controlPoints[i].y = speed;
			i++;
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
		return _baseAccelerationDelta * MathUtils.random(0.7f, 1.5f);
	}
}
