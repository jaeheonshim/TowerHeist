package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    public static final float PPM = 50;
    private GameWorld gameWorld;

    private SpriteBatch spriteBatch;
    private Box2DDebugRenderer debugRenderer;

    private Viewport viewport;

    public GameScreen() {
        gameWorld = new GameWorld();
        viewport = new FitViewport(40, 50);

        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        processInput();
        gameWorld.update(delta);

        debugRenderer.render(gameWorld.getPhysicsWorld(), viewport.getCamera().combined);
        gameWorld.render(spriteBatch);
    }

    public void processInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            gameWorld.sendInputCommand(InputCommand.JUMP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_LEFT);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_RIGHT);
        } else {
            gameWorld.sendInputCommand(InputCommand.STOP);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
