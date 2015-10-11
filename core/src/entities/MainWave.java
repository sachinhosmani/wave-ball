package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainWave extends Wave {
	protected float _ballX;
	protected float _radius;
	public float getBallX() {
		return _ballX;
	}
	public MainWave(float phase, float amplitude, int frequency, float speed, float radius, float ballX, float screenWidth, float screenHeight, Color color) {
		super(phase, amplitude, frequency, speed, screenWidth, screenHeight, color);
		_ballX = ballX;
		_radius = radius;
	}
	public void update() {
		super.update();
	}
	protected void increasePhase(float delta) {
		super.increasePhase(delta);
		_ballX += delta;
	}
	protected void decreasePhase(float delta) {
		super.decreasePhase(delta);
		_ballX -= delta;
	}
	public void render(ShapeRenderer renderer, float screenWidth, float screenHeight) {
		System.out.println(_translationX);
		super.render(renderer);
		float waveWidth = screenWidth / _frequency;
		float angleToWidth = (float) (waveWidth / (2 * Math.PI));
		float ballAngle = ((int)_ballX % (int)waveWidth) / angleToWidth;
		float ballY = (float) Math.sin(ballAngle) * _amplitude + screenHeight / 2;
		drawCircle(renderer, _ballX, ballY, _radius);
	}
}
