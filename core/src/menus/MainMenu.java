package menus;

import com.badlogic.gdx.graphics.Color;

import utils.AssetLoader;

public class MainMenu extends Menu {
	public static final int PLAY = 99;
	private float titlePositionX;
	private float titlePositionY;
	private float playPositionX;
	private float playPositionY;
	private float titleFinalPositionX;
	private float titleFinalPositionY;
	private float playFinalPositionX;
	private float playFinalPositionY;
	private float _screenWidth, _screenHeight;
	private Button playButton, titleButton;
	
	private float titleWidth;
	private float titleHeight;
	private float playButtonWidth;
	public MainMenu(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		
		titleWidth = screenWidth / 2.3f;
		titleHeight = titleWidth * 61.0f / 275.0f;
		
		playButtonWidth = screenWidth / 7.0f;
		
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		
		titleFinalPositionX = screenWidth / 2.0f - titleWidth / 2.0f;
		titleFinalPositionY = 3.2f * screenHeight / 4.0f - titleHeight / 2.0f;
		
		playFinalPositionX = screenWidth / 2.0f - playButtonWidth / 2.0f;
		playFinalPositionY = screenHeight / 2.0f - playButtonWidth / 2.0f;
		
		titlePositionX = 0.0f;
		titlePositionY = titleFinalPositionY;
		
		playPositionX = screenWidth;
		playPositionY = playFinalPositionY;
		
		titleButton = addButton(titlePositionX, titlePositionY, titleWidth, titleHeight, assetLoader.title, 0);
		playButton = addButton(playPositionX, playPositionY, playButtonWidth, playButtonWidth, assetLoader.play, PLAY);
	}
	public void update() {
		if (titlePositionX < titleFinalPositionX) {
			titlePositionX += _screenWidth / 40.0f;
		}
		if (playPositionX > playFinalPositionX) {
			playPositionX -= _screenWidth / 40.0f;
		}
		titleButton.x = titlePositionX;
		titleButton.y = titlePositionY;
		playButton.x = playPositionX;
		playButton.y = playPositionY;
	}
	public void reset() {
		titlePositionX = 0.0f;
		titlePositionY = titleFinalPositionY;
		
		playPositionX = _screenWidth;
		playPositionY = playFinalPositionY;
		
		titleButton.x = titlePositionX;
		titleButton.y = titlePositionY;
		playButton.x = playPositionX;
		playButton.y = playPositionY;
	}
}
