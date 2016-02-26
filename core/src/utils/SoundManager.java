package utils;

import com.wave.ball.WaveBall.GameState;

public class SoundManager {
	private AssetLoader _assetLoader;
	private float _gameMusicVolume = 0.5f;
	private float _menuMusicVolume = 0.0f;
	private boolean _gameMusicVolumeDecreasing = false;
	private boolean _menuMusicVolumeDecreasing = false;
	public SoundManager(AssetLoader assetLoader) {
		_assetLoader = assetLoader;
	}
	public void changeMusic(GameState state) {
		if (!_assetLoader.gameMusic.isPlaying()) {
			_assetLoader.gameMusic.play();
		}
		return;
//		if (state == GameState.PLAYING) {
//			_gameMusicVolume = 0.5f;
//			_assetLoader.gameMusic.setVolume(_gameMusicVolume);
//			_assetLoader.gameMusic.play();
//			_menuMusicVolumeDecreasing = true;
//			_gameMusicVolumeDecreasing = false;
//		} else {
//			_menuMusicVolume = 0.0f;
//			_assetLoader.menuMusic.setVolume(_menuMusicVolume);
//			_assetLoader.menuMusic.play();
//			_gameMusicVolumeDecreasing = true;
//			_menuMusicVolumeDecreasing = false;
//		}
	}
	public void playWhoosh() {
		_assetLoader.whooshSound.play();
	}
	public void playApplause() {
		_assetLoader.applauseSound.play();
	}
	public void playButton() {
		_assetLoader.buttonSound.play();
	}
	public void update() {
		if (_gameMusicVolumeDecreasing) {
			_gameMusicVolume -= 0.03f;
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
		}
		_assetLoader.gameMusic.setVolume(_gameMusicVolume);
	}
}
