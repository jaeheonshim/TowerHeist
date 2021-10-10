package com.jaeheonshim.towerheist.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.jaeheonshim.towerheist.TowerHeist;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.useIndexes = false;

		TexturePacker.process(settings, "./images", "./", "images");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TowerHeist(), config);
	}
}
