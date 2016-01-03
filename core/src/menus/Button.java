package menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
	public float x, y, w, h;
	public float r;
	public Color color;
	public String label;
	public boolean isTexture;
	public TextureRegion textureRegion;
	private Button attachedButton = null;
	public boolean noAction = false;
	int id;
	public Sprite sprite;
	public BitmapFont font;
	public float paddingFractionX;
	public float boxWidth = 0.0f;
	public float baseWidth;
	public Button(float x, float y, String label, Color color, int id, BitmapFont font, float paddingFractionX, float baseWidth) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.id = id;
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(font, label);
		this.w = Math.abs(bounds.width);
		this.h = Math.abs(bounds.height);
		this.x -= this.w / 2;
		this.font = font;
		this.color = color;
		this.paddingFractionX = paddingFractionX;
		this.baseWidth = baseWidth;
	}
	public Button(float x, float y, String label, Color color, int id, BitmapFont font, float paddingFractionX, float baseWidth, float w) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.id = id;
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(font, label);
		this.w = w;
		this.h = Math.abs(bounds.height);
		this.x -= this.w / 2;
		this.font = font;
		this.color = color;
		this.paddingFractionX = paddingFractionX;
		this.baseWidth = baseWidth;
	}
	public Button(float x, float y, float w, float h, Sprite sprite, int id) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		isTexture = true;
		this.label = "";
		this.sprite = sprite;
		this.id = id;
		this.font = null;
		this.color = null;
		this.baseWidth = w;
	}
	public Button(float x, float y, float w, float h, Sprite sprite, String label, Color c, int id, BitmapFont font, float baseWidth) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		isTexture = true;
		this.label = label;
		this.sprite = sprite;
		this.id = id;
		this.font = font;
		this.color = c;
		this.baseWidth = baseWidth;
	}
	public boolean contains(float baseX, float baseY) {
		float pw = w, ph = h;
		float buttonCenterX = x + w / 2;
		float buttonCenterY = y + h / 2;
		if (w < baseWidth / 600 * 40) {
			pw = baseWidth / 600 * 40;
		}
		if (h < baseWidth / 600 * 40) {
			ph = baseWidth / 600 * 40;
		}
		if (boxWidth != 0.0f) {
			return (buttonCenterX - boxWidth / 2 - pw * paddingFractionX <= baseX && buttonCenterX + boxWidth / 2 + pw * paddingFractionX >= baseX) &&
					(buttonCenterY - h / 2 - h * 0.52f <= baseY && buttonCenterY + h / 2 + h * 0.52f >= baseY);
		}
		return (buttonCenterX - pw / 2 - w * paddingFractionX <= baseX && buttonCenterX + pw / 2 + w * paddingFractionX >= baseX) &&
				(buttonCenterY - ph / 2 - h * 0.52f <= baseY && buttonCenterY + ph / 2 + h * 0.52f >= baseY); 
	}
	public void attachButton(Button btn) {
		attachedButton = btn;
	}
	public Button getAttachedButton() {
		return attachedButton;
	}
	public void changeLabel(String label) {
		this.label = label;
		this.x += this.w / 2;
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(font, label);
		this.w = Math.abs(bounds.width);
		this.h = Math.abs(bounds.height);
		this.x -= this.w / 2;
	}
}
