package entities;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
			_fontPositionOffsetFraction += 0.05f;
		}
	}
	public void incrementScore() {
		_fontPositionOffsetFraction = 0.0f;
	}
	public void render(SpriteBatch renderer, float cameraX, int score) {
		float width = _screenWidth / 50.0f * (score > 9 ? 2.0f : (score > 99 ? 3.0f : 1.0f ));
		_assetLoader.ubuntuFont[_assetLoader.LARGE_FONT].setColor(0.0f, 0.0f, 0.0f, _fontPositionOffsetFraction);
		_assetLoader.ubuntuFont[_assetLoader.LARGE_FONT].draw(renderer, score + "", _screenWidth * 0.5f - width / 2.0f,
				_screenHeight * 0.91f);
	}
}
