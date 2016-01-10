package menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {
	public float x, y, w, h;
	public float angle = 0.0f;
	public float centerX, centerY;
	public float r;
	public Color color;
	public String label;
	public boolean isTexture;
	public TextureRegion textureRegion;
	private Button attachedButton = null;
	public boolean noAction = false;
	public int id;
	public Sprite sprite;
	public BitmapFont font;
	public float paddingFractionX;
	public float boxWidth = 0.0f;
	public float scale = 1.0f;
	public Button(float x, float y, String label, Color color, int id, BitmapFont font, float paddingFractionX) {
		this.x = x;
		this.y = y;
		this.label = label;
		this.id = id;
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(font, label);
		this.w = Math.abs(bounds.width);
		this.h = Math.abs(bounds.height);
		centerX = x;
		centerY = y;
		this.x -= this.w / 2;
		this.y -= this.h / 2;
		this.font = font;
		this.color = color;
		this.paddingFractionX = paddingFractionX;
	}

	public Button(float x, float y, float w, float h, Sprite sprite, int id) {
		this.x = x - w / 2;
		this.y = y - h / 2;
		this.w = w;
		this.h = h;
		isTexture = true;
		this.label = "";
		this.sprite = sprite;
		this.id = id;
		this.font = null;
		this.color = null;
		centerX = x;
		centerY = y;
	}

	public boolean contains(float baseX, float baseY) {
		float buttonCenterX = x + w / 2;
		float buttonCenterY = y + h / 2;

		return (buttonCenterX - w / 2 - w * paddingFractionX <= baseX && buttonCenterX + w / 2 + w * paddingFractionX >= baseX) &&
				(buttonCenterY - h / 2 - h * 0.52f <= baseY && buttonCenterY + h / 2 + h * 0.52f >= baseY); 
	}
	public void attachButton(Button btn) {
		attachedButton = btn;
	}
	public Button getAttachedButton() {
		return attachedButton;
	}
	public void changeLabel(String label) {
		if (isTexture) {
			return;
		}
		this.label = label;
		this.x += this.w / 2;
		GlyphLayout bounds = new GlyphLayout();
		bounds.setText(font, label);
		this.w = Math.abs(bounds.width);
		this.h = Math.abs(bounds.height);
		this.x -= this.w / 2;
	}
	public void setCenterX(float x) {
		centerX = x;
		this.x = centerX - w / 2;
	}
	public void setCenterY(float y) {
		centerY = y;
		this.y = centerY - h / 2;
	}
	public void resetScale() {
		this.w = this.w / scale;
		this.h = this.h / scale;
		this.x = this.centerX - w / 2;
		this.y = this.centerY - h / 2;
		scale = 1.0f;
	}
	public void scale(float fraction) {
		resetScale();
		this.w = this.w * fraction;
		this.h = this.h * fraction;
		this.x = this.centerX - w / 2;
		this.y = this.centerY - h / 2;
		scale = fraction;
	}
}
