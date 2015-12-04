package entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import utils.AssetLoader;
import utils.Constants;

public class ScoreBoard {
	private float _screenWidth;
	private float _screenHeight;
	private AssetLoader _assetLoader;
	public ScoreBoard(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_assetLoader = assetLoader;
	}
	public void render(SpriteBatch renderer, float cameraX, int score) {
		_assetLoader.ubuntuFont[_assetLoader.LARGE_FONT].setColor(0.0f, 0.0f, 0.0f, 1.0f);
		_assetLoader.ubuntuFont[_assetLoader.LARGE_FONT].draw(renderer, score + "", cameraX * (float) Math.cos(Constants.rotation / 180.0f * Math.PI) + _screenWidth * 0.5f,
				cameraX * (float) Math.sin(Constants.rotation / 180.0f * Math.PI) + _screenHeight * 0.75f);
	}
}
