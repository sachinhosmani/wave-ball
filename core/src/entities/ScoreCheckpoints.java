package entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import utils.AssetLoader;
import utils.Constants;
import utils.PreferenceManager;

public class ScoreCheckpoints {
	private long _lastScore;
	private long _bestScore;
	private long _newBestScore;
	private float _lastScoreX, _lastScoreY;
	private float _bestScoreX, _bestScoreY;
	private float _newBestScoreX, _newBestScoreY;
	private PreferenceManager _preferenceManager;
	private AssetLoader _assetLoader;
	private int n = 0;
	private float _screenWidth;
	private float _screenHeight;
	private Vector2 _tmpVector = new Vector2();
	private final String LAST_SCORE = "last score";
	private final String BEST_SCORE = "best score";
	private final String NEW_BEST_SCORE = "new best score";
	public void ScoreCheckpoints() {
	}
	public void init(PreferenceManager prefManager, float screenWidth, float screenHeight, AssetLoader assetLoader) {
		_preferenceManager = prefManager;
		_lastScore = _preferenceManager.getLastScore();
		_bestScore = _preferenceManager.getMaxScore();
		_newBestScore = _bestScore + 1;
		n = 0;
		_assetLoader = assetLoader;
		_screenWidth = screenWidth;
		_screenHeight = screenHeight;
		_lastScoreX = -100000.0f;
		_lastScoreY = -100000.0f;
		_bestScoreX = -100000.0f;
		_bestScoreY = -100000.0f;
		_newBestScoreX = -100000.0f;
		_newBestScoreY = -100000.0f;
	}
	public void render(SpriteBatch renderer, float cameraX) {
		if (_lastScore != _bestScore) {
			if (cameraX - _screenWidth * 0.5f < _lastScoreX && cameraX + _screenWidth * 1.5f > _lastScoreX) {
				_assetLoader.line.setBounds(_lastScoreX, _lastScoreY, _screenWidth / 200.0f,  _screenWidth / 7.0f);
				_assetLoader.line.draw(renderer);
				_assetLoader.ubuntuFont[_assetLoader.SMALL_FONT].draw(renderer, LAST_SCORE, _lastScoreX + _screenWidth / 50.0f, _lastScoreY + _screenWidth / 7.0f);
			}
		}
		if (cameraX - _screenWidth * 0.5f < _bestScoreX && cameraX + _screenWidth * 1.5f > _bestScoreX) {
			_assetLoader.line.setBounds(_bestScoreX, _bestScoreY, _screenWidth / 200.0f,  _screenWidth / 7.0f);
			_assetLoader.line.draw(renderer);
			_assetLoader.ubuntuFont[_assetLoader.SMALL_FONT].draw(renderer, BEST_SCORE, _bestScoreX + _screenWidth / 50.0f, _bestScoreY + _screenWidth / 14.0f);
		}
		if (_newBestScore > 1) {
			if (cameraX - _screenWidth * 0.5f < _newBestScoreX && cameraX + _screenWidth * 1.5f > _newBestScoreX) {
				_assetLoader.line.setBounds(_newBestScoreX, _newBestScoreY, _screenWidth / 200.0f,  _screenWidth / 7.0f);
				_assetLoader.line.draw(renderer);
				_assetLoader.ubuntuFont[_assetLoader.SMALL_FONT].draw(renderer, NEW_BEST_SCORE, _newBestScoreX + _screenWidth / 50.0f, _newBestScoreY + _screenWidth / 14.0f);
			}
		}
	}
	public void nextCheckpoint(float x) {
		float y;
		_tmpVector.x = x - _screenWidth / 2.0f;
		_tmpVector.y = _screenHeight * 0.75f - _screenHeight / 2.0f;
		_tmpVector.rotate(Constants.rotation);
		x = _screenWidth / 2.0f + _tmpVector.x;
		y = _screenHeight / 2.0f + _tmpVector.y;
		n++;
		if (n == _lastScore) {
			_lastScoreX = x;
			_lastScoreY = y;
		}
		if (n == _bestScore) {
			_bestScoreX = x;
			_bestScoreY = y;
		}
		if (n == _newBestScore) {
			_newBestScoreX = x;
			_newBestScoreY = y;
		}
	}
}
