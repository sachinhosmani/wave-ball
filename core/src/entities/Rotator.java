package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.Constants;
import utils.TimeSnapshot;
import utils.TimeSnapshotStore;

public class Rotator {
	public float _angle;
	private float _speed;
	private TimeSnapshot _timeSnapshot;
	protected float _x;
	protected float _y;
	private float _screenWidth;
	private float _screenHeight;
	private Vector2 point1;
	private Vector2 point2;
	private Vector2 point3;
	private Vector2 point4;
	private Vector2 line11, line12;
	private Vector2 line21, line22;
	private boolean _clockwise;
	private Vector2 _tmpVector1, _tmpVector2, _tmpVector3, _tmpVector4, _tmpVector5;
	private AssetLoader _assetLoader;
	private boolean _varyingSpeed;
	private Color color = new Color(1.0f, 0.0f, 0.0f, 0.8f);
	private Polygon shape1;
	private Polygon shape2;
	public float _baseAngle;
	private float _angleOffset;
	private boolean _easy;
	private float _rotatorGirth;
	
	public Rotator(float angle, float speed, long pauseDuration,
			float x, float y, float screenWidth, float screenHeight, float rotatorWidth,
			boolean clockwise, boolean varyingSpeed, boolean easy, AssetLoader assetLoader) {
		_angle = angle;
		_baseAngle = _angle;
		_speed = speed;
		_timeSnapshot = TimeSnapshotStore.get();
		_x = x;
		_y = y;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		float rotatorLength = _screenWidth / 22.0f;
		_rotatorGirth = rotatorLength / 2.0f;
		point1 = new Vector2(-rotatorLength / 2.0f, -rotatorWidth / 2.0f);
		point2 = new Vector2(rotatorLength / 2.0f, -rotatorWidth / 2.0f);
		point3 = new Vector2(-rotatorLength / 2.0f, rotatorWidth / 2.0f);
		point4 = new Vector2(rotatorLength / 2.0f, rotatorWidth / 2.0f);
		line11 = new Vector2(_x + point1.x, _y + point1.y);
		line12 = new Vector2(_x + point2.x, _y + point2.y);
		line21 = new Vector2(_x + point3.x, _y + point3.y);
		line22 = new Vector2(_x + point4.x, _y + point4.y);
		_clockwise = clockwise;
		_varyingSpeed = varyingSpeed || easy;
		_tmpVector1 = new Vector2();
		_tmpVector2 = new Vector2();
		_tmpVector3 = new Vector2();
		_tmpVector4 = new Vector2();
		_tmpVector5 = new Vector2();
		_assetLoader = assetLoader;
		shape1 = new Polygon();
		shape2 = new Polygon();
		shape1.setVertices(new float[8]);
		shape2.setVertices(new float[8]);
		_angleOffset = easy ? _baseAngle : (float) Math.PI * MathUtils.random(0.0f, 1.0f);
		_easy = easy;
	}
	
	public float getSpeed() {
		return _speed;
	}
	
	public void update() {
		long timeSince = _timeSnapshot.snapshot();
		float speed = (float) (_varyingSpeed ? (0.8 + Math.abs(Math.sin(_angle - _angleOffset)) * 0.4) * _speed : _speed);
		if (_easy) {
			speed = (float) (_varyingSpeed ? (0.6 + Math.abs(Math.sin(_angle - _angleOffset)) * 0.6) * _speed : _speed);
		}
		_angle += speed * timeSince * (_clockwise ? -1 : 1);
		if (_angle > 2 * Math.PI) {
			_angle -= 2 * Math.PI;
		}
		
		point1.rotateRad(_angle);
		point2.rotateRad(_angle);
		point3.rotateRad(_angle);
		point4.rotateRad(_angle);
		
		line11.x = (_x + point1.x);
		line11.y = (_y + point1.y);
		line12.x = (_x + point2.x);
		line12.y = (_y + point2.y);
		line21.x = (_x + point3.x);
		line21.y = (_y + point3.y);
		line22.x = (_x + point4.x);
		line22.y = (_y + point4.y);
		
		point1.rotateRad(-_angle);
		point2.rotateRad(-_angle);
		point3.rotateRad(-_angle);
		point4.rotateRad(-_angle);
	}
	
	public void render(SpriteBatch renderer) {
		point1.rotateRad(_angle);
		point2.rotateRad(_angle);
		point3.rotateRad(_angle);
		point4.rotateRad(_angle);
		
		drawLine(renderer, _x + point1.x, _y + point1.y, _x + point2.x, _y + point2.y, _rotatorGirth, color, shape1);
		drawLine(renderer, _x + point3.x, _y + point3.y, _x + point4.x, _y + point4.y, _rotatorGirth, color, shape2);
		
		point1.rotateRad(-_angle);
		point2.rotateRad(-_angle);
		point3.rotateRad(-_angle);
		point4.rotateRad(-_angle);
	}
	
	public void drawLine(SpriteBatch renderer, float x1, float y1, float x2, float y2, float width, Color color, Polygon shape) {
		boolean reversed = false;
		if (y1 > y2) {
			float tmp = y1;
			y1 = y2;
			y2 = tmp;
			tmp = x1;
			x1 = x2;
			x2 = tmp;
			reversed = true;
		}
		
		_tmpVector1.x = x1 - _screenWidth / 2.0f;
		_tmpVector1.y = y1 - _screenHeight / 2.0f;
		_tmpVector1.rotate(Constants.rotation);
		x1 = _screenWidth / 2.0f + _tmpVector1.x;
		y1 = _screenHeight / 2.0f + _tmpVector1.y;
		_tmpVector1.x = x2 - _screenWidth / 2.0f;
		_tmpVector1.y = y2 - _screenHeight / 2.0f;
		_tmpVector1.rotate(Constants.rotation);
		x2 = _screenWidth / 2.0f + _tmpVector1.x;
		y2 = _screenHeight / 2.0f + _tmpVector1.y;
		
		float midX = (x1 + x2) / 2.0f;
		float midY = (y1 + y2) / 2.0f;
		
		_tmpVector1.x = x1 - midX;
		_tmpVector1.y = y1 - midY;
		float angle = _tmpVector1.angle();
		_tmpVector1.rotate(-angle - 90);
		x1 = _tmpVector1.x + midX;
		y1 = _tmpVector1.y + midY;
		
		_tmpVector1.x = x2 - midX;
		_tmpVector1.y = y2 - midY;
		angle = _tmpVector1.angle();
		_tmpVector1.rotate(-angle + 90);
		x2 = _tmpVector1.x + midX;
		y2 = _tmpVector1.y + midY;
		
//		_assetLoader.beam.setColor(color);
		_assetLoader.beam.setBounds(x1 - width / 2.0f, y1, width, Math.abs(y2 - y1));
		_assetLoader.beam.setOriginCenter();
		_assetLoader.beam.setRotation(reversed ? angle + 90 : angle - 90);
		_assetLoader.beam.draw(renderer);
		
		float length = y2 - y1;
		float[] vertices = shape.getVertices();
		vertices[0] = x1 - width / 3.0f;
		vertices[1] = y1 + length * 0.1f;
		vertices[2] = x1 + width / 3.0f;
		vertices[3] = y1 + length * 0.1f;
		vertices[4] = x1 + width / 3.0f;
		vertices[5] = y2 - length * 0.1f;
		vertices[6] = x1 - width / 3.0f;
		vertices[7] = y2 - length * 0.1f;
		
		for (int i = 0; i < 4; i++) {
			_tmpVector1.x = vertices[2 * i] - _screenWidth / 2.0f;
			_tmpVector1.y = vertices[2 * i + 1] - _screenHeight / 2.0f;
			_tmpVector1.rotate(-Constants.rotation);
			vertices[2 * i] = _tmpVector1.x + _screenWidth / 2.0f;
			vertices[2 * i + 1] = _tmpVector1.y + _screenHeight / 2.0f;
		}
		shape.setVertices(vertices);
		centroid(vertices, _tmpVector1);
		shape.setOrigin(_tmpVector1.x, _tmpVector1.y);
		shape.setRotation(angle - 90);
	}
	
	private void centroid(float[] vertices, Vector2 vector) {
		vector.x = (vertices[0] + vertices[2] + vertices[4] + vertices[6]) / 4.0f;
		vector.y = (vertices[1] + vertices[3] + vertices[5] + vertices[7]) / 4.0f;
	}
	
	public boolean overlaps(Polygon polygon, Circle circle) {
	    float []vertices=polygon.getTransformedVertices();
	    _tmpVector1.x = circle.x;
	    _tmpVector1.y = circle.y;
	    float squareRadius=circle.radius*circle.radius;
	    for (int i=0;i<vertices.length;i+=2){
	        if (i==0) {
	        	_tmpVector2.x = vertices[vertices.length-2];
	        	_tmpVector2.y = vertices[vertices.length-1];
	        	_tmpVector3.x = vertices[i];
	        	_tmpVector3.y = vertices[i + 1];
	            if (Intersector.intersectSegmentCircle(_tmpVector2, _tmpVector3, _tmpVector1, squareRadius))
	                return true;
	        } else {
	        	_tmpVector2.x = vertices[i-2];
	        	_tmpVector2.y = vertices[i-1];
	        	_tmpVector3.x = vertices[i];
	        	_tmpVector3.y = vertices[i+1];
	            if (Intersector.intersectSegmentCircle(_tmpVector2, _tmpVector3, _tmpVector1, squareRadius))
	                return true;
	        }
	    }
	    return false;
	}
	
	public boolean checkCollision(Circle circleShape) {
		return overlaps(shape1, circleShape) || overlaps(shape2, circleShape); 
	}
	
	public boolean checkCollision(Polygon shape) {
		return Intersector.overlapConvexPolygons(shape, shape1) || Intersector.overlapConvexPolygons(shape, shape2);
	}
}
