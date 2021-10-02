package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    public static final float PPM = 32;
    public static final float CAM_LERP = 0.4f;
    public static final float MAX_WIDTH = 60;
    public static final float MAX_HEIGHT = 50;

    private GameWorld gameWorld;

    private SpriteBatch spriteBatch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera camera;
    private Viewport viewport;

    private boolean renderDebug = false;

    public GameScreen() {
        gameWorld = new GameWorld();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(30, 24, MAX_WIDTH, MAX_HEIGHT, camera);

        camera.update();

        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        mapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getGameMap(), 1 / PPM);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        mapRenderer.setView(camera);
        spriteBatch.setProjectionMatrix(camera.combined);

        processInput();
        gameWorld.update(delta);

        Vector2 cameraDelta = new Vector2(MathUtils.clamp(gameWorld.getPlayer().getPosition().x, viewport.getWorldWidth() / 2f, MAX_WIDTH - viewport.getWorldWidth() / 2f) - camera.position.x, MathUtils.clamp(gameWorld.getPlayer().getPosition().y, viewport.getWorldHeight() / 2f, MAX_HEIGHT - viewport.getWorldHeight() / 2f) - camera.position.y);
        cameraDelta.scl(CAM_LERP);
        camera.position.add(new Vector3(cameraDelta.x, cameraDelta.y, camera.position.z));

        camera.update();

        gameWorld.renderMap(mapRenderer);
        gameWorld.render(spriteBatch);

        if(renderDebug) {
            debugRenderer.render(gameWorld.getPhysicsWorld(), camera.combined);
        }
    }

    public void processInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            gameWorld.sendInputCommand(InputCommand.JUMP);
        } else {
            gameWorld.sendInputCommand(InputCommand.RESET_JUMP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_LEFT);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_RIGHT);
        } else {
            gameWorld.sendInputCommand(InputCommand.STOP);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            renderDebug = !renderDebug;
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
