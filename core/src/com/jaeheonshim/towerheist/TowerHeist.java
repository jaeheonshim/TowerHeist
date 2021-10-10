package com.jaeheonshim.towerheist;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.jaeheonshim.towerheist.game.GameScreen;

public class TowerHeist extends Game {
	@Override
	public void create () {
		Assets.instance().loadAssets();
		Box2D.init();
		setScreen(new GameScreen());
	}
	
	@Override
	public void dispose () {

	}
}
