package menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.ButtonAnimation;
import utils.ButtonLinearAnimation;
import utils.ButtonScaleAnimation;

public class TutorialMenu extends Menu {
	public static final int PLAY = 99;
	public Button tutorialImage;
	public Button playButton;

	private Vector2 _tutorialPositionStart = new Vector2();
	private Vector2 _tutorialPositionEnd = new Vector2();
	private Vector2 _playPositionStart = new Vector2();
	private Vector2 _playPositionEnd = new Vector2();
	private float _tutorialWidth, _tutorialHeight;
	private float _playSize;
	private ArrayList<ButtonAnimation> _animations = new ArrayList<ButtonAnimation>();
	public TutorialMenu(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		
		_tutorialPositionStart.x = 0.0f;
		_tutorialPositionStart.y = 0.65f;
		_tutorialPositionEnd.x = 0.5f;
		_tutorialPositionEnd.y = _tutorialPositionStart.y;
		_tutorialWidth = 0.9f;
		_tutorialHeight = (assetLoader.tutorial.getHeight() / assetLoader.tutorial.getWidth()) * _tutorialWidth;  
		
		_playPositionStart.x = 0.0f;
		_playPositionStart.y = 0.22f;
		_playPositionEnd.x = 0.5f;
		_playPositionEnd.y = _playPositionStart.y;
		_playSize = 0.22f;
		
		tutorialImage = addButton(_tutorialPositionStart, _tutorialWidth, _tutorialHeight, assetLoader.tutorial, 0);
		playButton = addButton(_playPositionStart, _playSize, _playSize, assetLoader.play, PLAY);
		
		_animations.add(new ButtonLinearAnimation(tutorialImage, _tutorialPositionStart, _tutorialPositionEnd, 500, screenWidth, screenHeight));
		_animations.add(new ButtonLinearAnimation(playButton, _playPositionStart, _playPositionEnd, 500, screenWidth, screenHeight));
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
