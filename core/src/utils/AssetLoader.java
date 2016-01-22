package utils;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;

public class AssetLoader {
	public AssetManager manager = new AssetManager();
	
	public Sprite ball, enemy;
	public Sprite diamond, hero;
	public Sprite wave1, wave2, shadow;
	public Sprite beam;
	public Sprite background;
	public Sprite plusOne;
	
	public Sprite play;
	public Sprite title;
	public Sprite share;
	public Sprite rate;
	
	public int fontSize1, fontSize2, fontSize3, fontSize4;
	public float screenWidth, screenHeight;
	public int SMALL_FONT = 0;
	public int MEDIUM_FONT = 1;
	public int LARGE_FONT = 2;
	public int EXTRA_LARGE_FONT = 3;
	public BitmapFont[] ubuntuFont = new BitmapFont[4];
	public BitmapFont[] otherFont = new BitmapFont[4];
	public BitmapFont[] gocaFont = new BitmapFont[4];
	public BitmapFont[] goodFont = new BitmapFont[4];

	public TextureRegion[] diamonds = new TextureRegion[7];
	public Animation diamondAnimation;
	public BitmapFont splashFont;
	
	public I18NBundle i18nBundle;
	
	public float backgroundWidth, backgroundHeight;
	
	public void load(float screenWidth, float screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		manager.load("ball.png", Texture.class);
		manager.load("diamond.png", Texture.class);
		manager.load("wave1.png", Texture.class);
		manager.load("wave2.png", Texture.class);
		manager.load("shadow.png", Texture.class);
		manager.load("enemy.png", Texture.class);
		manager.load("beam.png", Texture.class);
		manager.load("star.png", Texture.class);
		manager.load("background.png", Texture.class);
		manager.load("+1.png", Texture.class);
		manager.load("play.png", Texture.class);
		manager.load("title.png", Texture.class);
		manager.load("share.png", Texture.class);
		manager.load("rate.png", Texture.class);
		
		manager.load("diamonds/1.png", Texture.class);
		manager.load("diamonds/2.png", Texture.class);
		manager.load("diamonds/3.png", Texture.class);
		manager.load("diamonds/4.png", Texture.class);
		manager.load("diamonds/5.png", Texture.class);
		manager.load("diamonds/6.png", Texture.class);
		manager.load("diamonds/7.png", Texture.class);
		
		loadFonts();
	}

	public void assignAssets() {
		ball = new Sprite(manager.get("ball.png", Texture.class));
		diamond = new Sprite(manager.get("diamonds/1.png", Texture.class));
		hero = new Sprite(manager.get("star.png", Texture.class));
		enemy = new Sprite(manager.get("enemy.png", Texture.class));
		beam = new Sprite(manager.get("beam.png", Texture.class));
		background = new Sprite(manager.get("background.png", Texture.class));
		wave1 = new Sprite(manager.get("wave1.png", Texture.class));
		wave2 = new Sprite(manager.get("wave2.png", Texture.class));
		shadow = new Sprite(manager.get("shadow.png", Texture.class));
		plusOne = new Sprite(manager.get("+1.png", Texture.class));
		play = new Sprite(manager.get("play.png", Texture.class));
		title = new Sprite(manager.get("title.png", Texture.class));
		share = new Sprite(manager.get("share.png", Texture.class));
		rate = new Sprite(manager.get("rate.png", Texture.class));
		
		for (int i = 0; i < 7; i++) {
			diamonds[i] = new Sprite(manager.get("diamonds/" + (i + 1) + ".png", Texture.class));
		}
		
		diamondAnimation = new Animation(0.09f, diamonds);
		
		ball.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		diamond.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		hero.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		enemy.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		beam.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		wave1.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		wave2.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shadow.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		plusOne.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		play.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		title.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		share.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rate.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	
		backgroundHeight = background.getHeight();
		backgroundWidth = background.getHeight();
				
		ubuntuFont[SMALL_FONT] = manager.get("ubuntu1.ttf", BitmapFont.class);
		ubuntuFont[MEDIUM_FONT] = manager.get("ubuntu2.ttf", BitmapFont.class);
		ubuntuFont[LARGE_FONT] = manager.get("ubuntu3.ttf", BitmapFont.class);
		ubuntuFont[EXTRA_LARGE_FONT] = manager.get("ubuntu4.ttf", BitmapFont.class);
		
		otherFont[SMALL_FONT] = manager.get("other1.ttf", BitmapFont.class);
		otherFont[MEDIUM_FONT] = manager.get("other2.ttf", BitmapFont.class);
		otherFont[LARGE_FONT] = manager.get("other3.ttf", BitmapFont.class);
		otherFont[EXTRA_LARGE_FONT] = manager.get("other4.ttf", BitmapFont.class);
		
		gocaFont[MEDIUM_FONT] = manager.get("goca1.ttf", BitmapFont.class);
		gocaFont[LARGE_FONT] = manager.get("goca2.ttf", BitmapFont.class);
		
		goodFont[MEDIUM_FONT] = manager.get("good1.ttf", BitmapFont.class);
		goodFont[LARGE_FONT] = manager.get("good2.ttf", BitmapFont.class);
		
		ubuntuFont[SMALL_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ubuntuFont[SMALL_FONT].getData().setScale(1.0f, 1.0f);
		ubuntuFont[MEDIUM_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ubuntuFont[MEDIUM_FONT].getData().setScale(1.0f, 1.0f);
		ubuntuFont[LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ubuntuFont[LARGE_FONT].getData().setScale(1.0f, 1.0f);
		ubuntuFont[EXTRA_LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ubuntuFont[EXTRA_LARGE_FONT].getData().setScale(1.0f, 1.0f);
		
		otherFont[SMALL_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		otherFont[SMALL_FONT].getData().setScale(1.0f, 1.0f);
		otherFont[MEDIUM_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		otherFont[MEDIUM_FONT].getData().setScale(1.0f, 1.0f);
		otherFont[LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		otherFont[LARGE_FONT].getData().setScale(1.0f, 1.0f);
		otherFont[EXTRA_LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		otherFont[EXTRA_LARGE_FONT].getData().setScale(1.0f, 1.0f);
		
		gocaFont[MEDIUM_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gocaFont[MEDIUM_FONT].getData().setScale(1.0f, 1.0f);
		gocaFont[LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gocaFont[LARGE_FONT].getData().setScale(1.0f, 1.0f);
		
		goodFont[MEDIUM_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		goodFont[MEDIUM_FONT].getData().setScale(1.0f, 1.0f);
		goodFont[LARGE_FONT].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		goodFont[LARGE_FONT].getData().setScale(1.0f, 1.0f);
		
		FileHandle i18nHandle = Gdx.files.internal("i18n/MyBundle");
		i18nBundle = I18NBundle.createBundle(i18nHandle, Locale.getDefault());
	}
	public void loadFonts() {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		fontSize1 = 40 * (int) screenWidth / 600;
		fontSize2 = 60 * (int) screenWidth / 600;
		fontSize3 = 70 * (int) screenWidth / 600;
		fontSize4 = 90 * (int) screenWidth / 600;
		FreeTypeFontLoaderParameter parameter1 = new FreeTypeFontLoaderParameter();
		parameter1.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter1.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter1.fontParameters.size = fontSize1;
		parameter1.fontFileName = "UbuntuMono-R.ttf";
		manager.load("ubuntu1.ttf", BitmapFont.class, parameter1);
		
		FreeTypeFontLoaderParameter parameter2 = new FreeTypeFontLoaderParameter();
		parameter2.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter2.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter2.fontParameters.size = fontSize2;
		parameter2.fontFileName = "UbuntuMono-R.ttf";
		manager.load("ubuntu2.ttf", BitmapFont.class, parameter2);
		
		FreeTypeFontLoaderParameter parameter3 = new FreeTypeFontLoaderParameter();
		parameter3.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter3.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter3.fontParameters.size = fontSize3;
		parameter3.fontFileName = "UbuntuMono-R.ttf";
		manager.load("ubuntu3.ttf", BitmapFont.class, parameter3);
		
		FreeTypeFontLoaderParameter parameter4 = new FreeTypeFontLoaderParameter();
		parameter4.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter4.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter4.fontParameters.size = fontSize4;
		parameter4.fontFileName = "UbuntuMono-R.ttf";
		manager.load("ubuntu4.ttf", BitmapFont.class, parameter4);
		
		fontSize1 = 22 * (int) screenWidth / 600;
		fontSize2 = 27 * (int) screenWidth / 600;
		fontSize3 = 32 * (int) screenWidth / 600;
		fontSize4 = 36 * (int) screenWidth / 600;
		FreeTypeFontLoaderParameter parameter5 = new FreeTypeFontLoaderParameter();
		parameter5.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter5.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter5.fontParameters.size = fontSize1;
		parameter5.fontFileName = "good times rg.ttf";		
		manager.load("other1.ttf", BitmapFont.class, parameter5);
		
		FreeTypeFontLoaderParameter parameter6 = new FreeTypeFontLoaderParameter();
		parameter6.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter6.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter6.fontParameters.size = fontSize2;
		parameter6.fontFileName = "good times rg.ttf";		
		manager.load("other2.ttf", BitmapFont.class, parameter6);
		
		FreeTypeFontLoaderParameter parameter7 = new FreeTypeFontLoaderParameter();
		parameter7.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter7.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter7.fontParameters.size = fontSize3;
		parameter7.fontFileName = "good times rg.ttf";		
		manager.load("other3.ttf", BitmapFont.class, parameter7);
		
		FreeTypeFontLoaderParameter parameter8 = new FreeTypeFontLoaderParameter();
		parameter8.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter8.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter8.fontParameters.size = fontSize4;
		parameter8.fontFileName = "good times rg.ttf";		
		manager.load("other4.ttf", BitmapFont.class, parameter8);
		
		fontSize1 = 20 * (int) screenWidth / 600;
		fontSize2 = 27 * (int) screenWidth / 600;
		fontSize3 = 37 * (int) screenWidth / 600;
		fontSize4 = 50 * (int) screenWidth / 600;
		FreeTypeFontLoaderParameter parameter9 = new FreeTypeFontLoaderParameter();
		parameter9.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter9.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter9.fontParameters.size = fontSize3;
		parameter9.fontFileName = "Comfortaa_Regular.ttf";		
		manager.load("goca1.ttf", BitmapFont.class, parameter9);
		
		FreeTypeFontLoaderParameter parameter10 = new FreeTypeFontLoaderParameter();
		parameter10.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter10.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter10.fontParameters.size = fontSize4;
		parameter10.fontFileName = "Comfortaa_Regular.ttf";
		manager.load("goca2.ttf", BitmapFont.class, parameter10);
		
		fontSize1 = 20 * (int) screenWidth / 600;
		fontSize2 = 27 * (int) screenWidth / 600;
		fontSize3 = 34 * (int) screenWidth / 600;
		fontSize4 = 38 * (int) screenWidth / 600;
		FreeTypeFontLoaderParameter parameter11 = new FreeTypeFontLoaderParameter();
		parameter11.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter11.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter11.fontParameters.size = fontSize1;
		parameter11.fontFileName = "GOODDP__.TTF";		
		manager.load("good1.ttf", BitmapFont.class, parameter11);
		
		FreeTypeFontLoaderParameter parameter12 = new FreeTypeFontLoaderParameter();
		parameter12.fontParameters.minFilter = Texture.TextureFilter.Nearest;
		parameter12.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter12.fontParameters.size = fontSize2;
		parameter12.fontFileName = "GOODDP__.TTF";		
		manager.load("good2.ttf", BitmapFont.class, parameter12);
	}
	public void loadSplashFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UbuntuMono-R.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter.size = fontSize1;
		splashFont = generator.generateFont(parameter);

		splashFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		splashFont.getData().setScale(1.0f, -1.0f);
		generator.dispose();
	}
	public void dispose() {
		
		ubuntuFont[SMALL_FONT].dispose();
		ubuntuFont[MEDIUM_FONT].dispose();
		ubuntuFont[LARGE_FONT].dispose();
		ubuntuFont[EXTRA_LARGE_FONT].dispose();
		
		otherFont[SMALL_FONT].dispose();
		otherFont[MEDIUM_FONT].dispose();
		otherFont[LARGE_FONT].dispose();
		otherFont[EXTRA_LARGE_FONT].dispose();
		
		gocaFont[MEDIUM_FONT].dispose();
		gocaFont[LARGE_FONT].dispose();
		
		goodFont[MEDIUM_FONT].dispose();
		goodFont[LARGE_FONT].dispose();
		
	}
	public boolean update() {
		return manager.update();
	}
}
