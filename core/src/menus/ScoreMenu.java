package menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.ButtonAnimation;
import utils.ButtonLinearAnimation;
import utils.ButtonScaleAnimation;
import utils.RotationAnimation;

public class ScoreMenu extends Menu {
	public static final int PLAY = 99;
	public static final int SHARE = 101;
	public static final int START = 102;
	public static final int RATE = 102;
	
	private Vector2 titlePositionStart = new Vector2();
	private Vector2 playPositionStart = new Vector2();
	private Vector2 titlePositionEnd = new Vector2();
	private Vector2 playPositionEnd = new Vector2();
	private Vector2 sharePositionStart = new Vector2();
	private Vector2 sharePositionEnd = new Vector2();
	private Vector2 ratePositionStart = new Vector2();
	private Vector2 ratePositionEnd = new Vector2();
	
	private Vector2 scorePositionStart = new Vector2();
	private Vector2 scorePositionEnd = new Vector2();
	private Vector2 maxScorePositionStart = new Vector2();
	private Vector2 maxScorePositionEnd = new Vector2();
	
	private Vector2 scoreTextPositionStart = new Vector2();
	private Vector2 scoreTextPositionEnd = new Vector2();
	private Vector2 maxScoreTextPositionStart = new Vector2();
	private Vector2 maxScoreTextPositionEnd = new Vector2();
	
	private Button playButton, titleButton, shareButton, rateButton;
	private Button scoreButton, maxScoreButton;
	private Button scoreTextButton, maxScoreTextButton;
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	private float shareButtonWidth;
	private float rateButtonWidth;
	
	private String scoreText = "score";
	private String maxScoreText = "max score";
	private Color scoreColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	public ScoreMenu(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		
		titleWidth = 1 / 2.3f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = 1 / 9.0f;
		
		shareButtonWidth = 1 / 10.0f;
		rateButtonWidth = 1 / 10.0f;
		
		titlePositionEnd.x = 1 / 2.0f;
		titlePositionEnd.y = 3.2f * 1 / 4.0f;
		
		playPositionEnd.x = 0.5f;
		playPositionEnd.y = 0.6f;
		
		titlePositionStart.x = 0.0f;
		titlePositionStart.y = titlePositionEnd.y;
		
		playPositionStart.x = 1;
		playPositionStart.y = playPositionEnd.y;
		
		scorePositionStart.x = 0.0f;
		scorePositionStart.y = 0.46f;
		
		scorePositionEnd.x = 0.5f;
		scorePositionEnd.y = scorePositionStart.y;
		
		maxScorePositionStart.x = 0.0f;
		maxScorePositionStart.y = 0.32f;
		
		maxScorePositionEnd.x = 0.5f;
		maxScorePositionEnd.y = maxScorePositionStart.y;
		
		scoreTextPositionStart.x = 0.0f;
		scoreTextPositionStart.y = 0.53f;
		
		scoreTextPositionEnd.x = 0.5f;
		scoreTextPositionEnd.y = scoreTextPositionStart.y;
		
		maxScoreTextPositionStart.x = 0.0f;
		maxScoreTextPositionStart.y = 0.39f;
		
		maxScoreTextPositionEnd.x = 0.5f;
		maxScoreTextPositionEnd.y = maxScoreTextPositionStart.y;
		
		sharePositionStart.x = 1.0f;
		sharePositionStart.y = 0.4f;
		
		sharePositionEnd.x = 0.8f;
		sharePositionEnd.y = sharePositionStart.y;
		
		ratePositionStart.x = 0.0f;
		ratePositionStart.y = 0.4f;
		
		ratePositionEnd.x = 0.2f;
		ratePositionEnd.y = ratePositionStart.y;
		
		titleButton = addButton(titlePositionStart, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionStart, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
		shareButton = addButton(sharePositionStart, shareButtonWidth, shareButtonWidth, assetLoader.share, SHARE);
		rateButton = addButton(ratePositionStart, rateButtonWidth, rateButtonWidth, assetLoader.rate, RATE);
		
		scoreButton = addText(scorePositionStart, "", scoreColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		maxScoreButton = addText(maxScorePositionStart, "", scoreColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		
		scoreTextButton = addText(scoreTextPositionStart, scoreText, scoreColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		maxScoreTextButton = addText(maxScoreTextPositionStart, maxScoreText, scoreColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		
		_animations.add(new ButtonLinearAnimation(titleButton, titlePositionStart, titlePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(playButton, playPositionStart, playPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonScaleAnimation(playButton, 0.9f, 1.2f, 800));
		_animations.add(new ButtonLinearAnimation(scoreButton, scorePositionStart, scorePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(shareButton, sharePositionStart, sharePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new RotationAnimation(shareButton, -15.0f, 15.0f, 500));
		_animations.add(new ButtonLinearAnimation(maxScoreButton, maxScorePositionStart, maxScorePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(scoreTextButton, scoreTextPositionStart, scoreTextPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(maxScoreTextButton, maxScoreTextPositionStart, maxScoreTextPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(rateButton, ratePositionStart, ratePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new RotationAnimation(rateButton, -15.0f, 15.0f, 500));
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
	public void reset(int score, int maxScore) {
		scoreButton.changeLabel(score + "");
		maxScoreButton.changeLabel(maxScore + "");
		reset();
	}
}
