package entities;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wave.ball.WaveBall.GameState;

import entities.CircleEntity.Type;
import utils.Acceleration;
import utils.AssetLoader;
import utils.Constants;
import utils.PhaseClassifier;
import utils.TimeSnapshot;
import utils.TimeSnapshotStore;
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
	private static final Color heroColor = new Color(0.0f, 0.5f, 1.0f, 0.4f);
	public Circle ballShape;
	public Vector2 ballVector = new Vector2();
	private Vector2 _tmpVector = new Vector2();
	private float camSpeedTrailing;
	private float rotatorWidth;
	private PhaseClassifier _phaseClassifier;
	private float _ballMaxSpeed;
	private float _ballMaxBoostedSpeed;
	private static Color waveColor = new Color(0.0f, 1.0f, 0.0f, 0.5f);
	private static Color _diamondColor = new Color(1.0f, 0.7f, 0.0f, 0.9f);
	private static Color _heroColor = new Color(0.2f, 0.7f, 0.3f, 0.9f);
	private float _baseAngleDelta;
	private int _score;
	private int _points;
	private LinkedList<Float> _rotatorPositions;
	private float _rotatorSpeed;
	public ArrayList<CircleEntity> _diamonds;
	private float _lastAngle;
	private float _lastSpeed;
	private boolean _lastVaryingSpeed;
	private boolean _lastMultiple = false;
	public boolean _heroMode;
	public long _heroModeStartTime;
	private long HERO_DURATION = 4000;
	private boolean _heroAnim;
	private ScoreBoard _scoreBoard;
	private ScoreCheckpoints _scoreCheckpoints;
	
	private TimeSnapshot _heroAnimTimeSnapshot = TimeSnapshotStore.get();
	private float _heroAnimX;
	private float _heroAnimY;
	private long _heroAnimTime;
	private final long _heroAnimTotalTime = 1000;
	private float _ballAlpha = 1.0f;

	public float getBallX() {
		return _ballX;
	}

	public WaveEquation getWaveEquation() {
		return _waveEquation;
	}
	public MainWave(float phase, float amplitude, int frequency, float speed, float radius, float ballX,
			float screenWidth, float screenHeight, Color color, ScoreBoard scoreBoard, ScoreCheckpoints checkpoints, AssetLoader assetLoader) {
		super(phase, amplitude, speed, screenWidth, screenHeight, color, assetLoader);
		_baseAngleDelta = 2 * ((float) Math.PI) / 700.0f;
		_waveEquation = new WaveEquation(amplitude, _baseAngleDelta, screenWidth,
				screenHeight / 2.0f, _baseAngleDelta / 2500.0f, amplitude / 2200.0f);
		_ballX = ballX;
		_radius = radius;
		_acceleration = new Acceleration(speed / 50.0f, speed / 300.0f, speed);
		ballShape = new Circle(0.0f, 0.0f, _radius);
		_startX = 0.0f;
		camSpeedTrailing = (float) screenWidth / 6.0f;
		rotatorWidth = _screenWidth / 9.0f;
		_phaseClassifier = new PhaseClassifier(screenWidth);
		_ballMaxSpeed = speed;
		_ballMaxBoostedSpeed = speed * 1.5f;
		_score = 0;
		_points = 0;
		_rotatorPositions = new LinkedList<Float>();
		_rotatorSpeed = (float) Math.PI / 1200.0f;
		_diamonds = new ArrayList<CircleEntity>();
		_heroMode = false;
		_heroModeStartTime = 0;
		_scoreBoard = scoreBoard;
		_scoreCheckpoints = checkpoints;
	}

	public void update(float cameraX, GameState gameState) {
		super.update();
		heroUpdate();
		updateBallMaxSpeed();
		long timeElapsed = _timeSnapshot.snapshot();
		_startX = Math.max(_startX + camSpeedTrailing * timeElapsed / 1000.0f, cameraX);
		if (moving) {
			_acceleration.start();
		} else {
			_acceleration.stop();
		}
		if (!_waveEquation.allRotatorsDone() && (cameraX + _screenWidth * 1.5f) > _waveEquation.peekNextRotatorPosition()) {
			float xPos = _waveEquation.peekNextRotatorPosition();
			_waveEquation.popRotatorPosition();
			addRotator(xPos, _waveEquation.peekRotatorMultiple());
			_scoreCheckpoints.nextCheckpoint(xPos);
			_waveEquation.popRotatorMultiple();
			_rotatorPositions.add(xPos);
		}
		if (_rotatorPositions.size() != 0 && _rotatorPositions.get(0) < cameraX - _screenWidth / 2.0f) {
			_rotatorPositions.remove(0);
		}
		_acceleration.update(timeElapsed);
		if (gameState != GameState.BALL_FALLING) {
			increasePhase(timeElapsed * _acceleration.getSpeed() / 1000 * (_heroMode ? 2.0f : 1.0f));
			_waveEquation.get(_ballX, _tmpVector);
			_ballY = _tmpVector.y;
		} else {
			_ballY += _screenWidth / 100.0f * Math.sin(-Constants.rotation);
			_ballX += _screenWidth / 100.0f * Math.cos(-Constants.rotation);
			_radius = Math.max(0.0f, _radius / 1.1f);
		}
		
		ballShape.x = _ballX;
		ballShape.y = _ballY;
		ballVector.x = _ballX;
		ballVector.y = _ballY;
		for (Rotator rotator: _rotators) {
			rotator.update();
		}
		tryRemoveRotator(cameraX);
//		rotatorWidth = Math.max(_screenWidth / 14.0f, rotatorWidth - _screenWidth / 220000.0f);
//		camSpeedTrailing = Math.min(camSpeedTrailing + _screenWidth / 40000.0f, 1.5f * _screenWidth / 5.0f);
		if (gameState == GameState.PLAYING) {
			updateScore();
		}
//		_ballMaxSpeed = Math.min(_ballMaxSpeed + _screenWidth / 100000.0f, _screenWidth / 4.0f);
		_ballMaxBoostedSpeed = _ballMaxSpeed * 1.3f;
		
//		_rotatorSpeed = Math.min(_rotatorSpeed + (float) Math.PI / 1300.0f / 80000.0f, (float) Math.PI / 1100.0f);
		
		if (!_waveEquation.allDiamondsDone() && (cameraX + _screenWidth * 1.1f) > _waveEquation.peekNextDiamondPosition()) {
			float xPos = _waveEquation.peekNextDiamondPosition();
			_waveEquation.popDiamondPosition();
			addDiamond(xPos);
			_scoreCheckpoints.nextCheckpoint(xPos);
		}
		if (!_waveEquation.allHerosDone() && (cameraX + _screenWidth * 1.1f) > _waveEquation.peekNextHeroPosition()) {
			float xPos = _waveEquation.peekNextHeroPosition();
			_waveEquation.popHeroPosition();
			addHero(xPos);
		}
		tryRemoveDiamonds(cameraX);
		if (System.currentTimeMillis() - _heroModeStartTime > HERO_DURATION) {
			_heroMode = false;
		}
		if (System.currentTimeMillis() - _heroModeStartTime > HERO_DURATION * 0.7f) {
			_ballAlpha = (float) Math.min(0.004 + _ballAlpha, 1.0);
		}
		if (_heroMode && System.currentTimeMillis() - _heroModeStartTime < HERO_DURATION * 0.1f) {
			_ballAlpha = (float) Math.max(_ballAlpha - 0.01, 0.4f);
		}
	}

	protected void addDiamond(float x) {
		_waveEquation.get(x, _tmpVector);
		_diamonds.add(new CircleEntity(x, _tmpVector.y,  _diamondRadius, (float) _screenWidth / 2.0f, Type.DIAMOND));
	}
	protected void addHero(float x) {
		_waveEquation.get(x, _tmpVector);
		_diamonds.add(new CircleEntity(x, _tmpVector.y,  _diamondRadius, (float) _screenWidth / 2.0f, Type.HERO));
	}
	private void tryRemoveDiamonds(float cameraX) {
		if (_diamonds.size() == 0) {
			return;
		}
		CircleEntity diamond = _diamonds.get(0);
		if (diamond._x < cameraX - _screenWidth * 1.1f) {
			_diamonds.remove(diamond);
		}
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
	
	public void incrementPoints(int increment) {
		_points += increment;
		_score += increment;
	}
	
	public int getPoints() {
		return _points;
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
		if (_ballX > _rotatorPositions.get(0) + _screenWidth / 30.0f) {
			_score++;
			_rotatorPositions.remove(0);
			_scoreBoard.incrementScore();
		}
	}

	public void render(SpriteBatch renderer, float cameraX) {
		super.render(renderer, cameraX, waveColor);
		heroRender(renderer);
//		_waveEquation.getDerivative(_ballX, _tmpVector);
		drawCircle(renderer, _ballX, _ballY, _radius, 0.0f, _ballAlpha, _heroMode ? CircleType.INVISIBLE : CircleType.BALL);
//		if (_startX > _screenWidth / 100.0f) {
//			_waveEquation.get(_startX, _tmpVector);
//			drawCircle(renderer, _tmpVector.x, _tmpVector.y, _radius / 2.0f, CircleType.ENEMY);
//		}
		for (Rotator rotator: _rotators) {
			rotator.render(renderer);
		}
		for (CircleEntity diamond: _diamonds) {
			drawCircle(renderer, diamond._x, diamond._y, diamond._type == Type.DIAMOND ? _diamondRadius : _enemyRadius, 0.0f, diamond._type == Type.DIAMOND ? CircleType.DIAMOND : CircleType.HERO);
		}
	}
	public void start() {
		moving = true;
	}
	public void stop() {
		moving = false;
	}
	private void addRotator(float x, boolean multiple) {
		_waveEquation.get(x, _tmpVector);
		boolean clockwise = rotatorClockwise(x);
		_lastRotatorClockwise = clockwise;
		boolean alternating = rotatorVaryingSpeed(multiple);
		_lastVaryingSpeed = alternating;
		float y = _tmpVector.y;
		_waveEquation.getDerivative(x, _tmpVector);
		float angle;
		if (multiple && _lastMultiple && _rotators.size() > 0) {
			
			angle = (float) Math.atan2(_tmpVector.y, _tmpVector.x) + (_rotators.getLast()._angle - _rotators.getLast()._baseAngle);
		} else {
			angle = (float) Math.atan2(_tmpVector.y, _tmpVector.x);
		}
		_lastAngle = angle;
		float rotatorSpeed = getRotatorSpeed(x, multiple);
		_lastSpeed = rotatorSpeed;
		Rotator rotator = new Rotator(angle, rotatorSpeed,
				0, x, y, _screenWidth, _screenHeight, rotatorWidth, clockwise, alternating, rotatorEasy(multiple), _assetLoader);
		_rotators.add(rotator);
		_lastRotatorLaunchX = x;
		_lastMultiple = multiple;
	}
	private void tryRemoveRotator(float cameraX) {
		if (_rotators.size() == 0) {
			return;
		}
		Rotator rotator = _rotators.get(0);
		if (rotator._x < cameraX - _screenWidth / 2.0f) {
			_rotators.remove(0);
		}
	}
	private boolean rotatorClockwise(float x) {
		if (_phaseClassifier.getPhase(_ballX) == 1) {
			return true;
		}
		if (x - _lastRotatorLaunchX <= _screenWidth / 7.0f) {
			return _lastRotatorClockwise;
		}
		return Math.random() > 0.5;
	}
	private float getRotatorSpeed(float x, boolean multiple) {
		if (_phaseClassifier.getPhase(_ballX) <= 1) {
			return _rotatorSpeed;
		}
		if (multiple && _lastMultiple) {
			return _lastSpeed;
		}
		if (multiple && !_lastMultiple) {
			return _rotatorSpeed * 1.1f;
		}
		return _rotatorSpeed * MathUtils.random(0.8f, 1.1f);
	}
	private boolean rotatorVaryingSpeed(boolean multiple) {
		if (multiple) {
			return false;
		}
		if (_phaseClassifier.getPhase(_ballX) >= 2) {
			return Math.random() > 0.7;
		}
		return false;
	}
	private boolean rotatorEasy(boolean multiple) {
		if (multiple) {
			return false;
		}
		if (_phaseClassifier.getPhase(_ballX) <= 1) {
			return true;
		}
		return false;
	}
	public void destroyDiamond(CircleEntity diamond) {
		_diamonds.remove(diamond);
	}
	
	public void hero(float x, float y) {
		if (_heroAnim) {
			return;
		}
		_heroAnim = true;
		_heroAnimX = x;
		_heroAnimY = y;
		_heroAnimTimeSnapshot.snapshot();
	}
	protected void heroUpdate() {
		if (!_heroAnim) {
			return;
		}
		_heroAnimTime += _heroAnimTimeSnapshot.snapshot();
		if (_heroAnimTime >= _heroAnimTotalTime) {
			_heroAnim = false;
			_heroAnimTime = 0;
		}
	}
	protected void heroRender(SpriteBatch batcher) {
		if (_heroAnim) {
			float fraction = ((float) _heroAnimTime) / _heroAnimTotalTime;
			_heroAnimY *= 1.005f;
			_tmpVector.x = _heroAnimX - _screenWidth / 2.0f;
			_tmpVector.y = _heroAnimY - _screenHeight / 2.0f;
			_tmpVector.rotate(Constants.rotation);
			
			float size = _screenWidth / 25.0f * (0.3f + 0.7f * fraction);
			_assetLoader.hero.setBounds(_tmpVector.x + _screenWidth / 2.0f, _tmpVector.y + _screenHeight / 2.0f, size,  size);
			_assetLoader.hero.draw(batcher);
		}
	}
}
