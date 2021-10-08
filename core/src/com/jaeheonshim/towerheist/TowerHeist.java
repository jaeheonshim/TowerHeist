package com.jaeheonshim.towerheist;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2D;

public class TowerHeist extends Game {
	@Override
	public void create () {
		Box2D.init();
		setScreen(new GameScreen());
	}
	
	@Override
	public void dispose () {

	}
}
