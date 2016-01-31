package com.wave.ball.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wave.ball.WaveBall;

import cross.CrossRate;
import cross.CrossShare;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height = 667;
		new LwjglApplication(new WaveBall(new CrossRate() {
			
			@Override
			public void rate() {
				// TODO Auto-generated method stub
				
			}
		}, new CrossShare() {
			
			@Override
			public void share(int score) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void share() {
				// TODO Auto-generated method stub
				
			}
		}), config);
	}
}
