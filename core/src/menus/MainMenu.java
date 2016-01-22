package menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.ButtonAnimation;
import utils.ButtonLinearAnimation;
import utils.ButtonScaleAnimation;

public class MainMenu extends Menu {
	public static final int PLAY = 99;
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
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	private float diamondsButtonWidth;
	private Color diamondTextColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	public MainMenu(float screenWidth, float screenHeight, int numDiamonds, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		
		_numDiamonds = numDiamonds;
		
		titleWidth = 1 / 2.3f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = 1 / 7.0f;
		diamondsButtonWidth = 1 / 20.0f;
		
		titlePositionEnd.x = 1 / 2.0f;
		titlePositionEnd.y = 3.2f * 1 / 4.0f;
		
		playPositionEnd.x = 1 / 2.0f;
		playPositionEnd.y = 1 / 2.0f;
		
		titlePositionStart.x = 0.0f;
		titlePositionStart.y = titlePositionEnd.y;
		
		playPositionStart.x = 1;
		playPositionStart.y = playPositionEnd.y;
		
		diamondsPositionEnd.x = diamondsButtonWidth * 0.5f + 0.01f;
		diamondsPositionEnd.y = 0.95f;
		
		diamondsPositionStart.x = diamondsPositionEnd.x;
		diamondsPositionStart.y = diamondsPositionEnd.y;
		
		diamondsTextPositionEnd.x = diamondsPositionEnd.x + 0.1f;
		diamondsTextPositionEnd.y = 0.95f;
		
		diamondsTextPositionStart.x = diamondsTextPositionEnd.x;
		diamondsTextPositionStart.y = diamondsTextPositionEnd.y;
		
		titleButton = addButton(titlePositionStart, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionStart, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
		diamonds = addButton(diamondsPositionStart, diamondsButtonWidth, diamondsButtonWidth, assetLoader.diamond, 0);
		diamondsText = addText(diamondsTextPositionStart, _numDiamonds + "", diamondTextColor, assetLoader.ubuntuFont[assetLoader.SMALL_FONT]);
		
		_animations.add(new ButtonLinearAnimation(titleButton, titlePositionStart, titlePositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(playButton, playPositionStart, playPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonScaleAnimation(playButton, 0.9f, 1.2f, 800));
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
	public void reset(int numDiamonds) {
		_numDiamonds = numDiamonds;
		diamondsText.changeLabel(_numDiamonds + "");
		reset();
	}
}
