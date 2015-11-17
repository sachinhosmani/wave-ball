package utils;

import java.util.Stack;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WaveEquation {
	private CatmullRomSpline<Vector2> spline;
	private static Vector2[] controlPoints;
	private static Vector2[] frequencyPoints;
	public static float xMax;
	public static float granularity;
	public Stack<Float> rotatorPositions;
	private PhaseClassifier phaseClassifier;
	public static void initSize(float aXMax, float aGranularity) {
		xMax = aXMax;
		granularity = aGranularity;
		controlPoints = new Vector2[(int) (xMax / granularity)];
	}
	public WaveEquation(float baseAmplitude, float baseAngleDelta, float screenWidth,
			float yBase, float angleRate, float amplitudeRate) {
		int size = (int) (xMax / granularity);
		rotatorPositions = new Stack<Float>();
		float amplitude = baseAmplitude;
		float y = 0.0f;
		int i = 0;
		boolean amplitudeIncreasing = Math.random() > 0.5;
		boolean frequencyIncreasing = Math.random() > 0.5;
		float lastAmplitudeChangeX = MathUtils.random(0.0f, screenWidth / 2.0f);
		float lastFrequencyChangeX = MathUtils.random(0.0f, screenWidth / 2.0f);
		float angle = 0.0f;
		float angleDelta = baseAngleDelta;
		float amplitudeChangeWindow = findAmplitudeChangeWindow(screenWidth);
		float frequencyChangeWindow = findFrequencyChangeWindow(screenWidth);
		float rotatorFrequency = screenWidth / 2.0f;
		phaseClassifier = new PhaseClassifier(screenWidth);
		float lastRotatorX = 0.0f;
		float x;
		boolean first = false, second = false;
		for (x = 0.0f; i < size; x += granularity) {
			y = waveEquation(angle, amplitude) + yBase;
			amplitude += (amplitudeIncreasing ? amplitudeRate : -amplitudeRate);
			angleDelta = angleDelta + (frequencyIncreasing ? angleRate : -angleRate);
			if (amplitude > 1.7f * baseAmplitude) {
				amplitudeIncreasing = false;
				lastAmplitudeChangeX = x;
			}
			if (angleDelta > 1.0f * baseAngleDelta) {
				frequencyIncreasing = false;
				lastFrequencyChangeX = x;
			}
			if (amplitude < 0.7f * baseAmplitude) {
				amplitudeIncreasing = true;
				lastAmplitudeChangeX = x;
			}
			if (angleDelta < 0.5f * baseAngleDelta) {
				frequencyIncreasing = true;
				lastFrequencyChangeX = x;
			}
			if (x - lastAmplitudeChangeX > amplitudeChangeWindow) {
				amplitudeIncreasing = !amplitudeIncreasing;
				lastAmplitudeChangeX = x;
				amplitudeChangeWindow = findAmplitudeChangeWindow(screenWidth);
			}
			if (x - lastFrequencyChangeX > frequencyChangeWindow) {
				frequencyIncreasing = !frequencyIncreasing;
				lastFrequencyChangeX = x;
				frequencyChangeWindow = findFrequencyChangeWindow(screenWidth);
			}
			if (first && second && x - lastRotatorX < screenWidth / 100.0f && phaseClassifier.getPhase(x) > 3 && angleDelta < 0.6 * baseAngleDelta) {
				rotatorPositions.add(0, x);
				lastRotatorX = x;
				first = second = false;
			}
			if (first && !second && x - lastRotatorX < screenWidth / 100.0f && phaseClassifier.getPhase(x) > 1 && angleDelta < 0.6 * baseAngleDelta) {
				rotatorPositions.add(0, x);
				lastRotatorX = x;
				second = true;
			}
			if (x - lastRotatorX > MathUtils.random(0.7f, 1.5f) * rotatorFrequency) {
				rotatorPositions.add(0, x);
				lastRotatorX = x;
				first = true;
				second = false;
			}
			controlPoints[i++] = new Vector2(x, y);
			angle += angleDelta;
		}
//		float yDiff = (y - yBase) / ((xMax - x) / granularity);
//		while (x < xMax) {
//			y -= yDiff;
//			controlPoints[i++] = new Vector2(x, y);
//			x += granularity;
//		}
		spline = new CatmullRomSpline<Vector2>(controlPoints, true);
	}
	private float waveEquation(float angle, float amplitude) {
		return (float) Math.sin(angle) * amplitude;
	}
	public void get(float x, Vector2 out) {
		while (x >= xMax) {
			x -= xMax;
		}
		spline.valueAt(out, x / xMax);
	}
	public void getDerivative(float x, Vector2 out) {
		while (x >= xMax) {
			x -= xMax;
		}
		spline.derivativeAt(out, x / xMax);
	}
	public void getFraction(float t, Vector2 out) {
		spline.valueAt(out, t);
	}
	private float findFrequencyChangeWindow(float screenWidth) {
		return MathUtils.random(screenWidth / 2.0f, 1.5f * screenWidth);
	}
	private float findAmplitudeChangeWindow(float screenWidth) {
		return MathUtils.random(screenWidth / 2.0f, 3.0f * screenWidth);
	}
	public boolean allRotatorsDone() {
		return rotatorPositions.isEmpty();
	}
	public float peekNextRotatorPosition() {
		return rotatorPositions.peek();
	}
	public void popRotatorPosition() {
		rotatorPositions.pop();
	}
}
