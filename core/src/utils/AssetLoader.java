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
	
	public Sprite pause, back;
	public Sprite circle1, circle2;
	public Sprite rectangle1, rectangle2;
	public Sprite chain;
	public Sprite hollow1, hollow2;
	public Sprite background;
	public Sprite button1, button2, button3;
	public Sprite shareButton, creditsButton;
	public Sprite goal;
	public Sprite protagonist;
	
	public Sound goalSound;
	public Sound waterDropSound;
	public Music cleanSoul;
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

	public TextureRegion[] unsafeCircle = new TextureRegion[11];
	public Animation unsafeCircleAnimation;
	public TextureRegion[] unsafeRectangle = new TextureRegion[20];
	public Animation unsafeRectangleAnimation;
	public BitmapFont splashFont;
	
	public I18NBundle i18nBundle;
	
	public void load(float screenWidth, float screenHeight) {
		manager.load("spritesheet.txt", TextureAtlas.class);
		if (screenWidth <= 0) {
			manager.load("500/spritesheet.txt", TextureAtlas.class);
			manager.load("500/background.jpg", Texture.class);
		} else if (screenWidth <= 1000) {
			manager.load("1000/spritesheet.txt", TextureAtlas.class);
			manager.load("1000/background.jpg", Texture.class);
			manager.load("1000/hollow2.png", Texture.class);
		} else if (screenWidth <= 1500) {
			manager.load("1500/spritesheet.txt", TextureAtlas.class);
			manager.load("1500/background.jpg", Texture.class);
			manager.load("1500/hollow1.png", Texture.class);
			manager.load("1500/hollow2.png", Texture.class);
		} else if (screenWidth <= 2000) {
			manager.load("2000/spritesheet.txt", TextureAtlas.class);
			manager.load("2000/hollow1.png", Texture.class);
			manager.load("2000/hollow2.png", Texture.class);
			manager.load("2000/background.jpg", Texture.class);
		} else {
			manager.load("2500/spritesheet.txt", TextureAtlas.class);
			manager.load("2500/hollow1.png", Texture.class);
			manager.load("2500/hollow2.png", Texture.class);
			manager.load("2500/background.jpg", Texture.class);
		}
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		manager.load("goal.wav", Sound.class);
		manager.load("water_drop.mp3", Sound.class);
		manager.load("music.mp3", Music.class);
		
		manager.load("circle.png", Texture.class);
		loadFonts();
	}

	public void assignAssets() {
		TextureAtlas spriteSheet = manager.get("spritesheet.txt", TextureAtlas.class);
		TextureAtlas screenDependentSpriteSheet;
		if (screenWidth <= 0) {
			screenDependentSpriteSheet = manager.get("500/spritesheet.txt", TextureAtlas.class);
			hollow1 = screenDependentSpriteSheet.createSprite("hollow1");
			hollow2 = screenDependentSpriteSheet.createSprite("hollow2");
			background = new Sprite(manager.get("500/background.jpg", Texture.class));
		} else if (screenWidth <= 1000) {
			screenDependentSpriteSheet = manager.get("1000/spritesheet.txt", TextureAtlas.class);
			hollow1 = screenDependentSpriteSheet.createSprite("hollow1");
			hollow2 = new Sprite(manager.get("1000/hollow2.png", Texture.class));
			background = new Sprite(manager.get("1000/background.jpg", Texture.class));
		} else if (screenWidth <= 1500) {
			screenDependentSpriteSheet = manager.get("1500/spritesheet.txt", TextureAtlas.class);
			hollow1 = new Sprite(manager.get("1500/hollow1.png", Texture.class));
			hollow2 = new Sprite(manager.get("1500/hollow2.png", Texture.class));
			background = new Sprite(manager.get("1500/background.jpg", Texture.class));
		} else if (screenWidth <= 2000) {
			screenDependentSpriteSheet = manager.get("2000/spritesheet.txt", TextureAtlas.class);
			hollow1 = new Sprite(manager.get("2000/hollow1.png", Texture.class));
			hollow2 = new Sprite(manager.get("2000/hollow2.png", Texture.class));
			background = new Sprite(manager.get("2000/background.jpg", Texture.class));
		} else {
			screenDependentSpriteSheet = manager.get("2500/spritesheet.txt", TextureAtlas.class);
			hollow1 = new Sprite(manager.get("2500/hollow1.png", Texture.class));
			hollow2 = new Sprite(manager.get("2500/hollow2.png", Texture.class));
			background = new Sprite(manager.get("2500/background.jpg", Texture.class));
		}
		
		pause = spriteSheet.createSprite("pause");
		back = spriteSheet.createSprite("back");
		circle1 = new Sprite(manager.get("circle.png", Texture.class));
		circle2 = screenDependentSpriteSheet.createSprite("circle2");
		rectangle1 = screenDependentSpriteSheet.createSprite("rectangle1");
		rectangle2 = screenDependentSpriteSheet.createSprite("rectangle2");
		chain = spriteSheet.createSprite("chain2");
		protagonist = screenDependentSpriteSheet.createSprite("protagonist");
		button1 = spriteSheet.createSprite("button1");
		button2 = spriteSheet.createSprite("button2");
		button3 = spriteSheet.createSprite("button3");
		goal = screenDependentSpriteSheet.createSprite("light");
		creditsButton = spriteSheet.createSprite("info");
		shareButton = spriteSheet.createSprite("share");
		
		goal.flip(false, true);
		
		Sprite[] sprites = {pause, back, circle1, circle2, rectangle1, rectangle2, chain, background, protagonist,
				button1, button2, button3, goal, creditsButton, shareButton, hollow1, hollow2};
		for (Sprite sprite: sprites) {
			sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		for (int i = 0; i < 11; i++) {
			unsafeCircle[i] = spriteSheet.findRegion("spiral" + i);
		}
		unsafeCircleAnimation = new Animation(0.04f, unsafeCircle);
		
		for (int i = 0; i < 20; i++) {
			unsafeRectangle[i] = spriteSheet.findRegion("rect_spiral" + i);
		}
		unsafeRectangleAnimation = new Animation(0.04f, unsafeRectangle);
		goalSound = manager.get("goal.wav", Sound.class);
		waterDropSound = manager.get("water_drop.mp3", Sound.class);
		cleanSoul = manager.get("music.mp3", Music.class);
		
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
		Sprite[] sprites = {pause, back, circle1, circle2, rectangle1, rectangle2, chain, background, protagonist,
				button1, button2, button3, goal, creditsButton, shareButton, hollow1, hollow2};
		for (Sprite sprite: sprites) {
			sprite.getTexture().dispose();
		}
		
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
		
		goalSound.dispose();
		waterDropSound.dispose();
		cleanSoul.dispose();
	}
	public boolean update() {
		return manager.update();
	}
}
