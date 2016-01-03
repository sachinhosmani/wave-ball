package menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Menu {
	public ArrayList<Button> buttons;
	private float _screenWidth, _screenHeight;
	public Menu(float screenWidth, float screenHeight) {
		buttons = new ArrayList<Button>();
		this._screenWidth = screenWidth;
		this._screenHeight = screenHeight;
	}
	public Button addButton(float x, float y, Color c, String label, int id, BitmapFont font, float f, float baseWidth) {
		Button btn = new Button(x, y, label, c, id, font, f, baseWidth);
		buttons.add(btn);
		return btn;
	}
	public Button addButton(float x, float y, Color c, String label, int id, BitmapFont font, float f, float baseWidth, float w) {
		Button btn = new Button(x, y, label, c, id, font, f, baseWidth, w);
		buttons.add(btn);
		return btn;
	}
	public Button addButton(float x, float y, float w, float h, Sprite sprite, int id) {
		Button btn = new Button(x, y, w, h, sprite, id);
		buttons.add(btn);
		return btn;
	}
	public Button addText(float x, float y, String label, Color c, BitmapFont font, float baseWidth) {
		Button btn = new Button(x, y, label, c, 0, font, 0.0f, baseWidth);
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
}
