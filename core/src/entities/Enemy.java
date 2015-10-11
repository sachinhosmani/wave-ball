package entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public interface Enemy {
	public void update();
	public boolean checkOverlap(Circle circle);
	public void render(ShapeRenderer renderer);
}
