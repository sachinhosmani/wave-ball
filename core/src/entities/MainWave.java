package entities;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import utils.Acceleration;
import utils.PhaseClassifier;
import utils.WaveEquation;

public class MainWave extends Wave {
	public LinkedList<Rotator> _rotators = new LinkedList<Rotator>();
	protected float _ballX;
	protected float _ballY;
	protected float _radius;
	protected boolean moving = false;
	protected Acceleration _acceleration;
	protected float _lastRotatorLaunchX = 0.0f;
	protected boolean _lastRotatorClockwise;
	private static final Color color = new Color(0.0f, 0.0f, 1.0f, 0.9f);
	public Circle ballShape;
	public Vector2 ballVector = new Vector2();
	private Vector2 _tmpVector = new Vector2();
	private float camSpeedTrailing;
	private float rotatorWidth;
	private PhaseClassifier _phaseClassifier;
	private float _ballMaxSpeed;
	private float _ballMaxBoostedSpeed;
	private static Color waveColor = new Color(0.0f, 1.0f, 0.0f, 0.5f);
	private float _baseAngleDelta;
	private int _score;
	private ArrayList<Float> _rotatorPositions;
	private float _rotatorSpeed;
	
	private float lastX = 0.0f;
	public float getBallX() {
		return _ballX;
	}
	public float getRadius() {
		return _radius;
	}
	public WaveEquation getWaveEquation() {
		return _waveEquation;
	}
	public MainWave(float phase, float amplitude, int frequency, float speed, float radius, float ballX,
			float screenWidth, float screenHeight, Color color) {
		super(phase, amplitude, speed, screenWidth, screenHeight, color);
		_baseAngleDelta = 2 * ((float) Math.PI) / 700.0f;
		_waveEquation = new WaveEquation(amplitude, _baseAngleDelta, screenWidth,
				screenHeight / 2.0f, _baseAngleDelta / 2500.0f, amplitude / 2000.0f);
		_ballX = ballX;
		_radius = radius;
		_acceleration = new Acceleration(speed / 5.0f, speed / 2.0f, speed);
		ballShape = new Circle(0.0f, 0.0f, _radius);
		_startX = 0.0f;
		camSpeedTrailing = (float) screenWidth / 5.0f;
		rotatorWidth = _screenWidth / 13.0f;
		_phaseClassifier = new PhaseClassifier(screenWidth);
		_ballMaxSpeed = speed;
		_ballMaxBoostedSpeed = speed * 1.2f;
		_score = 0;
		_rotatorPositions = new ArrayList<Float>();
		_rotatorSpeed = (float) Math.PI / 1300.0f;
	}

	public void update(float cameraX) {
		updateBallMaxSpeed();
		long timeElapsed = _timeSnapshot.snapshot();
		_startX = Math.max(_startX + camSpeedTrailing * timeElapsed / 1000.0f, cameraX);
		if (moving) {
			_acceleration.start();
		} else {
			_acceleration.stop();
		}
		if (!_waveEquation.allRotatorsDone() && (cameraX + _screenWidth) > _waveEquation.peekNextRotatorPosition()) {
			float xPos = _waveEquation.peekNextRotatorPosition();
			_waveEquation.popRotatorPosition();
			addRotator(xPos);
			_rotatorPositions.add(xPos);
		}
		_acceleration.update(timeElapsed);
		increasePhase(timeElapsed * _acceleration.getSpeed() / 1000);
		_waveEquation.get(_ballX, _tmpVector);
		_ballY = _tmpVector.y;
		ballShape.x = _ballX;
		ballShape.y = _ballY;
		ballVector.x = _ballX;
		ballVector.y = _ballY;
		for (Rotator rotator: _rotators) {
			rotator.update();
		}
		tryRemoveRotator(cameraX);
		rotatorWidth = Math.max(_screenWidth / 20.0f, rotatorWidth - _screenWidth / 80000.0f);
		camSpeedTrailing = Math.min(camSpeedTrailing + _screenWidth / 40000.0f, 1.5f * _screenWidth / 5.0f);
		updateScore();
		_ballMaxSpeed = Math.min(_ballMaxSpeed + _screenWidth / 40000.0f, 2.0f * _screenWidth / 5.0f);
		_ballMaxBoostedSpeed = _ballMaxSpeed * 1.2f;
		
		_rotatorSpeed += (float) Math.PI / 1300.0f / 40000.0f;
	}
	protected void updateBallMaxSpeed() {
		boolean found = false;
		for (Rotator rotator: _rotators) {
			if (Math.abs(rotator._x - _ballX) < _screenWidth / 20.0f) {
				found = true;
				_acceleration.setMaxSpeed(_ballMaxBoostedSpeed);
				break;
			}
		}
		if (!found) {
			_acceleration.setMaxSpeed(_ballMaxSpeed);
		}
	}
	
	protected void increasePhase(float delta) {
		_waveEquation.getDerivative(_ballX, _tmpVector);
		_ballX += delta / _tmpVector.len() * _screenWidth / 500.0f;
	}
	
	public int getScore() {
		return _score;
	}
	
	public void updateScore() {
		if (_rotatorPositions.size() == 0) {
			return;
		}
		if (_ballX > _rotatorPositions.get(0) + _screenWidth / 10.0f) {
			_score++;
			_rotatorPositions.remove(0);
		}
	}
	
	public void render(ShapeRenderer renderer, float cameraX) {
		super.render(renderer, cameraX, waveColor);
		drawCircle(renderer, _ballX, _ballY, _radius, color);
		for (Rotator rotator: _rotators) {
			rotator.render(renderer);
		}
	}
	public void start() {
		moving = true;
	}
	public void stop() {
		moving = false;
	}
	private void addRotator(float x) {
		_waveEquation.get(x, _tmpVector);
		boolean clockwise = rotatorClockwise();
		_lastRotatorClockwise = clockwise;
		boolean alternating = rotatorAlternating();
		float y = _tmpVector.y;
		_waveEquation.getDerivative(x, _tmpVector);
		float angle = (float) Math.atan2(_tmpVector.y, _tmpVector.x);
		float rotatorSpeed = getRotatorSpeed(x);
		_rotators.add(new Rotator(angle, rotatorSpeed, normalizeAngle(0.0f + (float) Math.PI / 2.0f),
				0, x, y, _screenWidth, _screenHeight, rotatorWidth, clockwise, alternating));
		_lastRotatorLaunchX = x;
	}
	private boolean checkIfRotatorAddable(float x, float y) {
		_waveEquation.get(x - _screenWidth / 10.0f, _tmpVector);
		float oldY = _tmpVector.y;
		_waveEquation.getDerivative(x, _tmpVector);
		if (_tmpVector.len() < 1.3 && Math.abs(oldY - y) > _screenHeight / 100.0f) {
			return false;
		}
		return true;
	}
	private void tryRemoveRotator(float cameraX) {
		if (_rotators.size() == 0) {
			return;
		}
		Rotator rotator = _rotators.get(0);
		if (rotator._x < cameraX) {
			_rotators.remove(0);
		}
	}
	private float getRotatorFrequency() {
		return MathUtils.random(0.7f * _screenWidth, 1.1f * _screenWidth);
	}
	private boolean rotatorClockwise() {
		if (_phaseClassifier.getPhase(_ballX) == 1) {
			return true;
		}
		if (_ballX - _lastRotatorLaunchX <= _screenWidth / 7.0f) {
			return _lastRotatorClockwise;
		}
		return Math.random() > 0.5;
	}
	private float getRotatorSpeed(float x) {
		if (_phaseClassifier.getPhase(_ballX) <= 1) {
			return _rotatorSpeed;
		}
		if (x - _lastRotatorLaunchX <= _screenWidth / 7.0f) {
			return _rotators.getLast().getSpeed();
		}
		return _rotatorSpeed * MathUtils.random(0.6f, 1.2f);
	}
	private boolean rotatorAlternating() {
		if (_phaseClassifier.getPhase(_ballX) == 3) {
			return Math.random() > 0.5;
		}
		return false;
	}
}
