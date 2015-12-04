package menus;

import com.badlogic.gdx.graphics.Color;

import utils.AssetLoader;

public class MainMenu extends Menu {
	public static final int PLAY = 99;
	public MainMenu(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		super(screenWidth, screenHeight);
		Color headingColor = new Color(0.0f, 0.0f, 0.0f, 0.9f);
		addText(screenWidth / 2, 13.0f * screenHeight / 15, "Wave Ball", headingColor, assetLoader.ubuntuFont[assetLoader.LARGE_FONT], screenWidth);
		
		addButton(screenWidth / 2, screenHeight / 2, headingColor, "Play", PLAY, assetLoader.ubuntuFont[assetLoader.MEDIUM_FONT], 0.5f, screenWidth);
	}
}
