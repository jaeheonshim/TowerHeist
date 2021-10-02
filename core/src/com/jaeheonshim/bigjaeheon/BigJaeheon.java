package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.ScreenUtils;

public class BigJaeheon extends Game {
	@Override
	public void create () {
		Box2D.init();
		setScreen(new GameScreen());
	}
	
	@Override
	public void dispose () {

	}
}
