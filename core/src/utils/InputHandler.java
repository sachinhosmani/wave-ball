package utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.wave.ball.WaveBall;

public class InputHandler implements InputProcessor {
    public static enum UserInput {
    	LEFT,
    	RIGHT
    };
	private float _screenWidth, _screenHeight;
	private WaveBall _game;
    public InputHandler(float screenWidth, float screenHeight, WaveBall game) {
    	_screenWidth = screenWidth;
    	_screenHeight = screenHeight;
    	_game = game;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
    	if (keycode == Input.Keys.LEFT) {
    		_game.touchDown(UserInput.LEFT);
    	} else if (keycode == Input.Keys.RIGHT) {
    		_game.touchDown(UserInput.RIGHT);
    	}
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
    	if (keycode == Input.Keys.LEFT) {
    		_game.touchUp(UserInput.LEFT);
    	}
    	if (keycode == Input.Keys.RIGHT) {
    		_game.touchUp(UserInput.RIGHT);
    	}
    	return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
