package entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.Constants;

public class ScoreBoard {
	private float _screenWidth;
	private float _screenHeight;
	private AssetLoader _assetLoader;
	private float _fontPositionOffsetFraction = 0.0f;
	private float _offset;
	public ScoreBoard(float screenWidth, float screenHeight, AssetLoader assetLoader) {
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_assetLoader = assetLoader;
		_offset = _screenWidth / 10.0f;
	}
	public void update() {
		if (_fontPositionOffsetFraction < 1.0f) {
			_fontPositionOffsetFraction += 0.02f;
		}
	}
	public void incrementScore() {
		_fontPositionOffsetFraction = 0.0f;
	}
	public void render(SpriteBatch renderer, float cameraX, int score) {
		_assetLoader.ubuntuFont[_assetLoader.MEDIUM_FONT].setColor(0.0f, 0.0f, 0.0f, _fontPositionOffsetFraction);
		_assetLoader.ubuntuFont[_assetLoader.MEDIUM_FONT].draw(renderer, score + "", _screenWidth * 0.87f,
				_screenHeight * 0.94f);
	}
}
