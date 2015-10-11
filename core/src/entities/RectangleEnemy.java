package entities;

import utils.TimeSnapshot;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class RectangleEnemy implements Enemy {

	private float _w;
	private float _h;
	private float _x;
	private float _y;
	private float _speed;
	private Rectangle _rectangle;
	private TimeSnapshot _timeSnapshot;
	
	public RectangleEnemy(float x, float y, float w, float h, float speed) {
		_w = w;
		_h = h;
		_speed = speed;
		_x = x;
		_y = y;
		_rectangle = new Rectangle(_x, _y, _w, _h);
		_timeSnapshot = new TimeSnapshot();
	}
	
	@Override
	public void update() {
		long timeS = _timeSnapshot.snapshot();
		_x = _x - timeS * _speed;
		_rectangle.x = _x;
		_rectangle.y = _y;
	}

	@Override
	public boolean checkOverlap(Circle circle) {
		// TODO Auto-generated method stub
		return Intersector.overlaps(circle, _rectangle);
	}
	
	@Override
	public void render(ShapeRenderer renderer) {
		renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		renderer.rect(_x, _y, _w, _h);
	}

}
