package menus;

import java.util.ArrayList;

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

public class ScoreMenu extends Menu {
	public static final int PLAY = 99;
	public static final int SHARE = 101;
	public static final int START = 102;
	public static final int RATE = 102;
	public static final int HOME = 103;
	public static final int BALL = 104;
	
	private Vector2 titlePositionStart = new Vector2();
	private Vector2 playPositionStart = new Vector2();
	private Vector2 titlePositionEnd = new Vector2();
	private Vector2 playPositionEnd = new Vector2();
	private Vector2 sharePositionStart = new Vector2();
	private Vector2 sharePositionEnd = new Vector2();
	private Vector2 ratePositionStart = new Vector2();
	private Vector2 ratePositionEnd = new Vector2();
	private Vector2 homePositionStart = new Vector2();
	private Vector2 homePositionEnd = new Vector2();
	private Vector2 ballPositionStart = new Vector2();
	private Vector2 ballPositionEnd = new Vector2();
	
	private Vector2 scorePositionStart = new Vector2();
	private Vector2 scorePositionEnd = new Vector2();
	private Vector2 maxScorePositionStart = new Vector2();
	private Vector2 maxScorePositionEnd = new Vector2();
	
	private Vector2 scoreTextPositionStart = new Vector2();
	private Vector2 scoreTextPositionEnd = new Vector2();
	private Vector2 maxScoreTextPositionStart = new Vector2();
	private Vector2 maxScoreTextPositionEnd = new Vector2();
	private Vector2 diamondsPositionStart = new Vector2();
	private Vector2 diamondsPositionEnd = new Vector2();
	private Vector2 diamondsTextPositionStart = new Vector2();
	private Vector2 diamondsTextPositionEnd = new Vector2();
	private Vector2 unlockPositionStart = new Vector2();
	private Vector2 unlockPositionEnd = new Vector2();
	
	private Button diamondsText, diamonds;
	
	private Button playButton, titleButton, shareButton, rateButton, homeButton, ballButton;
	private Button scoreButton, maxScoreButton;
	private Button scoreTextButton, maxScoreTextButton;
	private Button unlockSign;
	private RotationAnimation unlockAnimation;
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	private float shareButtonWidth;
	private float rateButtonWidth;
	private float homeButtonWidth;
	private float ballButtonWidth;
	private float unlockSignWidth;
	
	private String scoreText = "Score";
	private String maxScoreText = "Best";
	private Color scoreColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private float diamondsButtonWidth;
	private Color diamondTextColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	private AssetLoader _assetLoader;
	private PreferenceManager _prefManager;
	
	private int _numDiamonds = 0;
	public ScoreMenu(float screenWidth, float screenHeight, PreferenceManager prefManager, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		_assetLoader = assetLoader;
		_prefManager = prefManager;
		
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(_assetLoader.ubuntuFont[_assetLoader.SMALL_FONT], "10");
		float diamondScoreHeight = bounds.height;
		
		titleWidth = 1 / 2.1f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = 1 / 5.5f;
		diamondsButtonWidth = 1 / 18.0f;
		
		shareButtonWidth = 1 / 10.0f;
		rateButtonWidth = 1 / 10.0f;
		
		homeButtonWidth = 1 / 10.0f;
		ballButtonWidth = 1 / 10.0f;

		unlockSignWidth = 0.06f;
		
		titlePositionEnd.x = 1 / 2.0f;
		titlePositionEnd.y = 3.4f * 1 / 4.0f;
		
		playPositionEnd.x = 0.5f;
		playPositionEnd.y = 0.68f;
		
		titlePositionStart.x = 0.0f;
		titlePositionStart.y = titlePositionEnd.y;
		
		playPositionStart.x = 1;
		playPositionStart.y = playPositionEnd.y;
		
		scoreTextPositionStart.x = 0.0f;
		scoreTextPositionStart.y = 0.57f;
		
		scoreTextPositionEnd.x = 0.5f;
		scoreTextPositionEnd.y = scoreTextPositionStart.y;
		
		scorePositionStart.x = 0.0f;
		scorePositionStart.y = scoreTextPositionStart.y - 0.06f;
		
		scorePositionEnd.x = 0.5f;
		scorePositionEnd.y = scorePositionStart.y;
		
		maxScoreTextPositionStart.x = 0.0f;
		maxScoreTextPositionStart.y = scorePositionStart.y - 0.08f;
		
		maxScoreTextPositionEnd.x = 0.5f;
		maxScoreTextPositionEnd.y = maxScoreTextPositionStart.y;
		
		maxScorePositionStart.x = 0.0f;
		maxScorePositionStart.y = maxScoreTextPositionStart.y - 0.06f;
		
		maxScorePositionEnd.x = 0.5f;
		maxScorePositionEnd.y = maxScorePositionStart.y;
		
		sharePositionStart.x = 1.0f;
		sharePositionStart.y = 0.5f;
		
		sharePositionEnd.x = 0.8f;
		sharePositionEnd.y = sharePositionStart.y;
		
		ratePositionStart.x = 0.0f;
		ratePositionStart.y = 0.5f;
		
		ratePositionEnd.x = 0.2f;
		ratePositionEnd.y = ratePositionStart.y;
		
		homePositionStart.x = 0.0f;
		homePositionStart.y = 0.27f;
		
		homePositionEnd.x = 0.3f;
		homePositionEnd.y = homePositionStart.y;
		
		ballPositionStart.x = 0.0f;
		ballPositionStart.y = 0.27f;
		
		ballPositionEnd.x = 0.7f;
		ballPositionEnd.y = ballPositionStart.y;
		
		
		diamondsPositionEnd.x = diamondsButtonWidth + 0.005f;
		diamondsPositionEnd.y = 0.96f;
		
		diamondsPositionStart.x = diamondsPositionEnd.x;
		diamondsPositionStart.y = diamondsPositionEnd.y;
		
		diamondsTextPositionEnd.y = diamondsPositionEnd.y + diamondScoreHeight / _screenHeight;
		
		diamondsTextPositionStart.x = diamondsTextPositionEnd.x;
		diamondsTextPositionStart.y = diamondsTextPositionEnd.y;
		
		unlockPositionStart.x = 0.0f;
		unlockPositionStart.y = ballPositionStart.y - 0.02f;
		
		unlockPositionEnd.x = ballPositionEnd.x + 0.02f;
		unlockPositionEnd.y = unlockPositionStart.y;
		
		titleButton = addButton(titlePositionStart, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionEnd, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
		shareButton = addButton(sharePositionStart, shareButtonWidth, shareButtonWidth, assetLoader.share, SHARE);
		rateButton = addButton(ratePositionStart, rateButtonWidth, rateButtonWidth, assetLoader.rate, RATE);
		homeButton = addButton(homePositionStart, homeButtonWidth, homeButtonWidth, assetLoader.home, HOME);
		ballButton = addButton(ballPositionStart, ballButtonWidth, ballButtonWidth, assetLoader.ball, BALL);
		
		scoreButton = addText(scorePositionStart, "", scoreColor, assetLoader.ubuntuFont[assetLoader.MEDIUM_FONT]);
		maxScoreButton = addText(maxScorePositionStart, "", scoreColor, assetLoader.ubuntuFont[assetLoader.MEDIUM_FONT]);
		
		scoreTextButton = addText(scoreTextPositionStart, scoreText, scoreColor, assetLoader.ubuntuFont[assetLoader.MEDIUM_FONT]);
		maxScoreTextButton = addText(maxScoreTextPositionStart, maxScoreText, scoreColor, assetLoader.ubuntuFont[assetLoader.MEDIUM_FONT]);
		
		diamonds = addButton(diamondsPositionStart, diamondsButtonWidth, diamondsButtonWidth, assetLoader.diamond, 0);
		diamondsText = addText(diamondsTextPositionStart, _numDiamonds + "", diamondTextColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		
		diamondsTextPositionEnd.x = diamondsPositionEnd.x + 0.06f + diamondsText.w / 2 / _screenWidth;
		
		unlockSign = addButton(unlockPositionStart, unlockSignWidth, unlockSignWidth, assetLoader.lock, 0);
		
		_animations.add(new ButtonLinearAnimation(titleButton, titlePositionStart, titlePositionEnd, 500, screenWidth, screenHeight));
//		_animations.add(new ButtonLinearAnimation(playButton, playPositionStart, playPositionEnd, 500, screenWidth, screenHeight));
//		_animations.add(new ButtonScaleAnimation(playButton, 0.9f, 1.2f, 800));
		_animations.add(new ButtonLinearAnimation(scoreButton, scorePositionStart, scorePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(shareButton, sharePositionStart, sharePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new RotationAnimation(shareButton, -15.0f, 15.0f, 500));
		_animations.add(new ButtonLinearAnimation(maxScoreButton, maxScorePositionStart, maxScorePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(scoreTextButton, scoreTextPositionStart, scoreTextPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(maxScoreTextButton, maxScoreTextPositionStart, maxScoreTextPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(rateButton, ratePositionStart, ratePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new RotationAnimation(rateButton, -15.0f, 15.0f, 500));
		_animations.add(new ButtonLinearAnimation(homeButton, homePositionStart, homePositionEnd, 500, screenWidth, screenHeight));
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
		for (ButtonAnimation animation: _animations) {
			animation.reset();
		}
	}
	public void reset(int score, int maxScore, int numDiamonds) {
		scoreButton.changeLabel(score + "");
		maxScoreButton.changeLabel(maxScore + "");
		_numDiamonds = numDiamonds;
		diamondsText.changeLabel(_numDiamonds + "");
		diamondsTextPositionEnd.x = diamondsPositionEnd.x + 0.06f + diamondsText.w / 2 / _screenWidth;
		
		ballButton.sprite = _assetLoader.ball;
		_animations.remove(unlockAnimation);
		if (_prefManager.getPoints() >= WaveBall.POINTS_PER_BALL) {
			_animations.add(unlockAnimation);
		}
		reset();
	}
}
