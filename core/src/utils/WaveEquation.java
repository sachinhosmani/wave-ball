package utils;

import java.util.Stack;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class WaveEquation {
	private CatmullRomSpline<Vector2> spline;
	private static Vector2[] controlPoints;
	public static float xMax;
	public static float granularity;
	public Stack<Float> rotatorPositions;
	public Stack<Boolean> rotatorMultiple;
	public Stack<Float> diamondPositions;
	public Stack<Float> heroPositions;
	private PhaseClassifier phaseClassifier;
	private float _screenWidth;
	public static void initSize(float aXMax, float aGranularity) {
		xMax = aXMax;
		granularity = aGranularity;
		int size = (int) (xMax / granularity);
		controlPoints = new Vector2[size];
		for (int i = 0; i < size; i++) {
			controlPoints[i] = new Vector2();
		}
	}
	public WaveEquation(float baseAmplitude, float baseAngleDelta, float screenWidth,
			float yBase, float angleRate, float amplitudeRate) {
		_screenWidth = screenWidth;
		int size = (int) (xMax / granularity);
		rotatorPositions = new Stack<Float>();
		rotatorMultiple = new Stack<Boolean>();
		diamondPositions = new Stack<Float>();
		heroPositions = new Stack<Float>();
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
		float rotatorFrequency = screenWidth / 1.5f;
		phaseClassifier = new PhaseClassifier(screenWidth);
		float lastRotatorX = screenWidth / 2.0f;
		float x;
		boolean first = false, second = false;
		float lastAngle = 0.0f;
		float maxAngleDiff = 0.66f;
		for (x = 0.0f; i < size; x += granularity) {
			y = waveEquation(angle, amplitude) + yBase;
			amplitude += (amplitudeIncreasing ? amplitudeRate : -amplitudeRate);
			angleDelta = angleDelta + (frequencyIncreasing ? angleRate : -angleRate);
			if (amplitude > 1.7f * baseAmplitude) {
				amplitudeIncreasing = false;
				lastAmplitudeChangeX = x;
			}
			if (angleDelta > 0.85f * baseAngleDelta) {
				frequencyIncreasing = false;
				lastFrequencyChangeX = x;
			}
			if (amplitude < 0.8f * baseAmplitude) {
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
//			if (!first && second && Math.abs(x - lastRotatorX - screenWidth / 7.0f) < screenWidth / 50.0f &&
//					phaseClassifier.getPhase(x) >= 3 &&
//					Math.abs(lastAngle - angle) < maxAngleDiff) {
//				rotatorPositions.add(0, x);
//				rotatorMultiple.add(0, true);
//				lastRotatorX = x;
//				first = second = false;
//				System.out.println(Math.abs(lastAngle - angle) + " " + maxAngleDiff);
//				lastAngle = angle;
//			}
			if (first && !second && Math.abs(x - lastRotatorX - screenWidth / 7.0f) < screenWidth / 50.0f &&
					phaseClassifier.getPhase(x) >= 3 &&
					Math.abs(lastAngle - angle) < maxAngleDiff && false) {
				rotatorPositions.add(0, x);
				rotatorMultiple.remove(0);
				rotatorMultiple.add(0, true);
				rotatorMultiple.add(0, true);
				lastRotatorX = x;
				second = true;
				first = false;
//				System.out.println(Math.abs(lastAngle - angle) + " " + maxAngleDiff);
				lastAngle = angle;
			}
			if (x - lastRotatorX > MathUtils.random(0.9f, 1.5f) * rotatorFrequency) {
				rotatorPositions.add(0, x);
				rotatorMultiple.add(0, false);
				lastRotatorX = x;
				first = true;
				second = false;
				lastAngle = angle;
			}
			controlPoints[i].x = x;
			controlPoints[i].y = y;
			i++;
			angle += angleDelta;
			maxAngleDiff += granularity / size * 0.3f;
		}
		float lastDiamondPosition = 0.0f;
		float lastHeroPosition = 0.0f;
		for (i = 0; i < rotatorPositions.size() - 1; i++) {
			float pos1 = rotatorPositions.get(i);
			float pos2 = rotatorPositions.get(i + 1);
			if (pos1 - pos2 > screenWidth / 5.0f && Math.random() > 0.3) {
				float pos = (pos1 + pos2) / 2 + MathUtils.random(-0.3f, 0.3f) * (pos1 - pos2);
				if (Math.random() > 0.6) {
					if (Math.random() > 0.15) {
						if (Math.abs(pos - lastDiamondPosition) > screenWidth) {
							diamondPositions.add(pos);
							lastDiamondPosition = (pos1 + pos2) / 2;
						}
					} else {
						if (Math.abs(pos - lastHeroPosition) > 2 * screenWidth) {
//							heroPositions.add((pos1 + pos2) / 2 + MathUtils.random(-0.3f, 0.3f) * (pos1 - pos2));
							lastHeroPosition = (pos1 + pos2) / 2;
						}
					}
				}
			}
		}
		spline = new CatmullRomSpline<Vector2>(controlPoints, true);
	}
	private float waveEquation(float angle, float amplitude) {
		return (float) Math.sin(angle) * amplitude;
	}
	public void get(float x, Vector2 out, boolean main, int num) {
		while (x >= xMax) {
			x -= xMax;
		}
		try {
			spline.valueAt(out, x / xMax);
			out.y += _screenWidth / 8.0f * Math.sin(Math.sin(x / _screenWidth / 10.0f) * x / _screenWidth * 2.0f) * (main ? 1 : (num == 1 ? -(2.0f / 0.85f) : -(1.0f / 1.35f)));
		} catch (Exception e) {
			System.out.println(e);
		}
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
	public void popRotatorMultiple() {
		rotatorMultiple.pop();
	}
	public boolean peekRotatorMultiple() {
		return rotatorMultiple.peek();
	}
	public boolean allDiamondsDone() {
		return diamondPositions.isEmpty();
	}
	public float peekNextDiamondPosition() {
		return diamondPositions.peek();
	}
	public void popDiamondPosition() {
		diamondPositions.pop();
	}
	public boolean allHerosDone() {
		return heroPositions.isEmpty();
	}
	public float peekNextHeroPosition() {
		return heroPositions.peek();
	}
	public void popHeroPosition() {
		heroPositions.pop();
	}
}
