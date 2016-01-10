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
	private Button playButton, titleButton;
	private int _score = -1;
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	private Color scoreColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	public MainMenu(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		
		titleWidth = 1 / 2.3f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = 1 / 7.0f;
		
		titlePositionEnd.x = 1 / 2.0f;
		titlePositionEnd.y = 3.2f * 1 / 4.0f;
		
		playPositionEnd.x = 1 / 2.0f;
		playPositionEnd.y = 1 / 2.0f;
		
		titlePositionStart.x = 0.0f;
		titlePositionStart.y = titlePositionEnd.y;
		
		playPositionStart.x = 1;
		playPositionStart.y = playPositionEnd.y;
		
		titleButton = addButton(titlePositionStart, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionStart, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
		
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
}
