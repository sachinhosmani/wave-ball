package menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.wave.ball.WaveBall;

import utils.AssetLoader;
import utils.ButtonAnimation;
import utils.ButtonLinearAnimation;
import utils.ButtonScaleAnimation;
import utils.PreferenceManager;
import utils.RotationAnimation;

public class MainMenu extends Menu {
	public static final int PLAY = 99;
	public static final int RATE = 100;
	public static final int BALL = 101;
	
	private Vector2 titlePositionStart = new Vector2();
	private Vector2 playPositionStart = new Vector2();
	private Vector2 titlePositionEnd = new Vector2();
	private Vector2 playPositionEnd = new Vector2();
	private Vector2 diamondsPositionStart = new Vector2();
	private Vector2 diamondsPositionEnd = new Vector2();
	private Vector2 diamondsTextPositionStart = new Vector2();
	private Vector2 diamondsTextPositionEnd = new Vector2();
	private Button playButton, titleButton;
	private Button diamondsText, diamonds;
	private int _numDiamonds = 0;
	
	private Vector2 ratePositionStart = new Vector2();
	private Vector2 ratePositionEnd = new Vector2();
	private Vector2 ballPositionStart = new Vector2();
	private Vector2 ballPositionEnd = new Vector2();
	private Vector2 unlockPositionStart = new Vector2();
	private Vector2 unlockPositionEnd = new Vector2();
	
	private Button rateButton;
	private Button ballButton;
	private Button unlockSign;
	private RotationAnimation unlockAnimation;
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	private float diamondsButtonWidth;
	private float unlockSignWidth;
	private Color diamondTextColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private AssetLoader _assetLoader;
	private PreferenceManager _prefManager;
	public MainMenu(float screenWidth, float screenHeight, int numDiamonds, PreferenceManager prefManager, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		_assetLoader = assetLoader;
		_prefManager = prefManager;
		
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(_assetLoader.ubuntuFont[_assetLoader.SMALL_FONT], "10");
		float diamondScoreHeight = bounds.height;
		
		float rateButtonWidth = 0.1f;
		float ballButtonWidth = 0.1f;
		unlockSignWidth = 0.06f;
		
		_numDiamonds = numDiamonds;
		
		titleWidth = 1 / 2.1f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = 1 / 4.4f;
		diamondsButtonWidth = 1 / 18.0f;
		
		titlePositionEnd.x = 1 / 2.0f;
		titlePositionEnd.y = 3.2f * 1 / 4.0f;
		
		playPositionEnd.x = 1 / 2.0f;
		playPositionEnd.y = 1 / 1.8f;
		
		titlePositionStart.x = 0.0f;
		titlePositionStart.y = titlePositionEnd.y;
		
		playPositionStart.x = 1;
		playPositionStart.y = playPositionEnd.y;
		
		diamondsPositionEnd.x = diamondsButtonWidth + 0.005f;
		diamondsPositionEnd.y = 0.96f;
		
		diamondsPositionStart.x = diamondsPositionEnd.x;
		diamondsPositionStart.y = diamondsPositionEnd.y;
		
		diamondsTextPositionEnd.y = diamondsPositionEnd.y + diamondScoreHeight / _screenHeight;
		
		diamondsTextPositionStart.x = diamondsTextPositionEnd.x;
		diamondsTextPositionStart.y = diamondsTextPositionEnd.y;
		
		ratePositionStart.x = 0.0f;
		ratePositionStart.y = 0.32f;
		
		ratePositionEnd.x = 0.3f;
		ratePositionEnd.y = ratePositionStart.y;
		
		ballPositionStart.x = 0.0f;
		ballPositionStart.y = 0.32f;
		
		ballPositionEnd.x = 0.7f;
		ballPositionEnd.y = ballPositionStart.y;
		
		unlockPositionStart.x = 0.0f;
		unlockPositionStart.y = ballPositionStart.y - 0.02f;
		
		unlockPositionEnd.x = ballPositionEnd.x + 0.02f;
		unlockPositionEnd.y = unlockPositionStart.y;
		
		titleButton = addButton(titlePositionStart, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionEnd, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
		diamonds = addButton(diamondsPositionStart, diamondsButtonWidth, diamondsButtonWidth, assetLoader.diamond, 0);
		diamondsText = addText(diamondsTextPositionStart, _numDiamonds + "", diamondTextColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		
		diamondsTextPositionEnd.x = diamondsPositionEnd.x + 0.06f + diamondsText.w / 2 / _screenWidth;
		
		rateButton = addButton(ratePositionStart, rateButtonWidth, rateButtonWidth, assetLoader.rate, RATE);
		ballButton = addButton(ballPositionStart, ballButtonWidth, ballButtonWidth, assetLoader.ball, BALL);
		unlockSign = addButton(unlockPositionStart, unlockSignWidth, unlockSignWidth, assetLoader.lock, 0);
		
		_animations.add(new ButtonLinearAnimation(titleButton, titlePositionStart, titlePositionEnd, 500, screenWidth, screenHeight));
//		_animations.add(new ButtonLinearAnimation(playButton, playPositionStart, playPositionEnd, 500, screenWidth, screenHeight));
//		_animations.add(new ButtonScaleAnimation(playButton, 0.9f, 1.2f, 800));
		_animations.add(new ButtonLinearAnimation(rateButton, ratePositionStart, ratePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new RotationAnimation(rateButton, -15.0f, 15.0f, 500));
		_animations.add(new ButtonLinearAnimation(ballButton, ballPositionStart, ballPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(unlockSign, unlockPositionStart, unlockPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(diamondsText, diamondsTextPositionStart, diamondsTextPositionEnd, 500, screenWidth, screenHeight));
		unlockAnimation = new RotationAnimation(unlockSign, -15.0f, 15.0f, 500);
	}
	public void update() {
		for (ButtonAnimation animation: _animations) {
			animation.update();
		}
	}
	public void reset() {
		_numDiamonds = (int) _prefManager.getPoints();
		diamondsText.changeLabel(_numDiamonds + "");
		diamondsTextPositionEnd.x = diamondsPositionEnd.x + 0.06f + diamondsText.w / 2 / _screenWidth;
		ballButton.sprite = _assetLoader.ball;
		
		_animations.remove(unlockAnimation);
		if (_prefManager.getPoints() >= WaveBall.POINTS_PER_BALL) {
			_animations.add(unlockAnimation);
		}
		super.reset();
	}
}
