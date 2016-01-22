package menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Menu {
	public ArrayList<Button> buttons;
	private float _screenWidth, _screenHeight;
	public Menu(float screenWidth, float screenHeight) {
		buttons = new ArrayList<Button>();
		this._screenWidth = screenWidth;
		this._screenHeight = screenHeight;
	}
	public Button addButton(Vector2 pos, Color c, String label, int id, BitmapFont font, float f) {
		Button btn = new Button(_screenWidth * pos.x, _screenHeight * pos.y, label, c, id, font, f);
		buttons.add(btn);
		return btn;
	}
	public Button addButton(Vector2 pos, float w, float h, Sprite sprite, int id) {
		Button btn = new Button(_screenWidth * pos.x, _screenHeight * pos.y, _screenWidth * w, _screenWidth * h, sprite, id);
		buttons.add(btn);
		return btn;
	}
	public Button addText(Vector2 pos, String label, Color c, BitmapFont font) {
		Button btn = new Button(_screenWidth * pos.x, _screenHeight * pos.y, label, c, 0, font, 0.0f);
		btn.noAction = true;
		buttons.add(btn);
		return btn;
	}
	private int findIDForClick(float clickX, float clickY) {
		for (Button button: buttons) {
			if (button.contains(clickX, clickY)) {
				return button.id;
			}
		}
		return 0;
	}
	public int handleClick(float baseX, float baseY) {
		int id = findIDForClick(baseX, baseY);
		return id;
	}
	public void update() {
		
	}
	public void setButtonPosition(Button btn, float x, float y) {
		btn.centerX = _screenWidth * x;
		btn.y = _screenHeight * y;
		btn.centerY = y;
		if (!btn.isTexture) {
			btn.x = btn.x - btn.w / 2.0f;
		} else {
			btn.x = x;
		}
	}
}
