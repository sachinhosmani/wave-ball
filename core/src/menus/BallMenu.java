package menus;

import java.awt.List;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.wave.ball.WaveBall;

import utils.AssetLoader;
import utils.ButtonAnimation;
import utils.ButtonLinearAnimation;
import utils.PreferenceManager;
import utils.RotationAnimation;

public class BallMenu extends Menu {
	public static final int HOME = 150;
	public static final int BALL1 = 1;
	private int _maxBallUnlock = 0;
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	
	private ArrayList<Button> balls;
	private float ballWidth;
	
	private Vector2 _homePositionStart = new Vector2();
	private Vector2 _homePositionEnd = new Vector2();
	private Button _home;
	private AssetLoader _assetLoader;
	private PreferenceManager _prefManager;
	private float _screenWidth, _screenHeight;
	private RotationAnimation _unlockAnimation;
	public BallMenu(float screenWidth, float screenHeight, int maxBallUnlock, PreferenceManager prefManager, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		_assetLoader = assetLoader;
		_prefManager = prefManager;
		float homeWidth = 0.1f;
		
		balls = new ArrayList<Button>();
		
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		
		ballWidth = 1 / 10.0f;
		
		_maxBallUnlock = maxBallUnlock;
		
		_homePositionStart.x = 0.0f;
		_homePositionStart.y = 0.95f;
		
		_homePositionEnd.x = 0.12f;
		_homePositionEnd.y = _homePositionStart.y;
		_home = addButton(_homePositionEnd, homeWidth, homeWidth, assetLoader.home, HOME);
		
		Vector2 startPosition = new Vector2(0.0f, 0.0f);
		int count = 0;
		for (int i = 1; i <= (screenHeight / screenWidth * 3) && count < assetLoader.NUM_BALLS; i++) {
			float y = 0.95f - (1 / (screenHeight / screenWidth * 3 + 1)) * i;
			for (int j = 1; j <= 3 && count < assetLoader.NUM_BALLS; j++) {
				float x = 0.25f * j;
				Vector2 pos = new Vector2();
				pos.y = y;
				pos.x = x;
				if (count < assetLoader.NUM_BALLS) {
					Sprite asset = (count <= _maxBallUnlock) ? assetLoader.balls[count] : assetLoader.lock;
					balls.add(addButton(pos, ballWidth, ballWidth, asset, BALL1 + count));
					_animations.add(new ButtonLinearAnimation(balls.get(count), startPosition, pos, 500, screenWidth, screenHeight));
				}
				count++;
			}
		}
		balls.get((int) _prefManager.getSelectedBall()).scale(1.15f);
	}
	public void update() {
		for (ButtonAnimation animation: _animations) {
			animation.update();
		}
	}
	public void reset() {
		resetMaxBallUnlock();
		resetSelection();
		for (ButtonAnimation animation: _animations) {
			animation.reset();
		}
	}

	public void resetMaxBallUnlock() {
		_maxBallUnlock = (int) _prefManager.getMaxBallUnlock();
		_animations.remove(_unlockAnimation);
		if (_maxBallUnlock + 1 < balls.size() && _prefManager.getPoints() >= WaveBall.POINTS_PER_BALL) {
			_unlockAnimation = new RotationAnimation(balls.get(_maxBallUnlock + 1), -15.0f, 15.0f, 500);
			_animations.add(_unlockAnimation);
		}
		if (_maxBallUnlock < balls.size()) {
			balls.get(_maxBallUnlock).sprite = _assetLoader.balls[_maxBallUnlock];
		}
	}
	public void resetSelection() {
		for (int i = 0; i <= _maxBallUnlock && i < _assetLoader.balls.length; i++) {
			balls.get(i).sprite = _assetLoader.balls[i];
			balls.get(i).resetScale();
			balls.get(i).alpha = 0.7f;
		}
		balls.get((int) _prefManager.getSelectedBall()).alpha = 1.0f;
		balls.get((int) _prefManager.getSelectedBall()).scale(1.15f);
	}
}
