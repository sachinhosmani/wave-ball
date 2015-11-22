package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;

public class ScoreBoard {
	private BitmapFont font;
	private float _screenWidth;
	private float _screenHeight;
	private Vector2 _tmpVector;
	public ScoreBoard(float screenWidth, float screenHeight) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("good_times_rg.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 50;
		font = generator.generateFont(parameter);
		generator.dispose();
		
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
	}
	public void render(SpriteBatch renderer, float cameraX, int score) {
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(renderer, score + "", cameraX * (float) Math.cos(20 / 180.0f * Math.PI) + _screenWidth * 0.89f,
				cameraX * (float) Math.sin(20 / 180.0f * Math.PI) + _screenHeight * 0.89f);
	}
}
