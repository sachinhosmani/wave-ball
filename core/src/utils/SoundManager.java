package utils;

import com.wave.ball.WaveBall.GameState;

public class SoundManager {
	private AssetLoader _assetLoader;
	private float _gameMusicVolume = 1.0f;
	private float _menuMusicVolume = 1.0f;
	private boolean _gameMusicVolumeDecreasing = false;
	private boolean _menuMusicVolumeDecreasing = false;
	public SoundManager(AssetLoader assetLoader) {
		_assetLoader = assetLoader;
	}
	public void changeMusic(GameState state) {
		if (state == GameState.PLAYING) {
			_gameMusicVolume = 1.0f;
			_assetLoader.gameMusic.setVolume(_gameMusicVolume);
			_assetLoader.gameMusic.setLooping(true);
			_assetLoader.gameMusic.play();
			_menuMusicVolumeDecreasing = true;
			_gameMusicVolumeDecreasing = false;
		} else {
			_menuMusicVolume = 0.7f;
			_assetLoader.menuMusic.setVolume(_menuMusicVolume);
			_assetLoader.menuMusic.setLooping(true);
			_assetLoader.menuMusic.play();
			_gameMusicVolumeDecreasing = true;
			_menuMusicVolumeDecreasing = false;
		}
	}
	public void update() {
		if (_gameMusicVolumeDecreasing) {
			_gameMusicVolume -= 0.02f;
		}
		if (_menuMusicVolumeDecreasing) {
			_menuMusicVolume -= 0.01f;
		}
		if (_gameMusicVolume <= 0.0f) {
			_gameMusicVolumeDecreasing = false;
			_assetLoader.gameMusic.stop();
		}
		if (_menuMusicVolume <= 0.0f) {
			_menuMusicVolumeDecreasing = false;
			_assetLoader.menuMusic.stop();
		}
		_assetLoader.gameMusic.setVolume(_gameMusicVolume);
		_assetLoader.menuMusic.setVolume(_menuMusicVolume);
	}
}
