package com.jaeheonshim.towerheist.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.jaeheonshim.towerheist.InputCommand;
import com.jaeheonshim.towerheist.game.render.BackgroundRenderer;
import com.jaeheonshim.towerheist.game.render.CameraManager;
import com.jaeheonshim.towerheist.ui.GameStage;

public class GameScreen implements Screen {
    public static final float PPM = 32;

    private GameStage gameStage;
    private GameWorld gameWorld;

    private boolean doVfx = false;
    private VfxManager vfxManager;
    private BloomEffect bloomEffect;

    private SpriteBatch spriteBatch;
    private OrthogonalTiledMapRenderer mapRenderer;
    private BackgroundRenderer backgroundRenderer;
    private Box2DDebugRenderer debugRenderer;

    private CameraManager gameCamera;

    private boolean debugMode = false;

    public GameScreen() {
        gameWorld = new GameWorld();
        gameCamera = new CameraManager();

        spriteBatch = new SpriteBatch();
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        bloomEffect = new BloomEffect();
        vfxManager.addEffect(bloomEffect);

        debugRenderer = new Box2DDebugRenderer();
        mapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getGameMap(), 1 / PPM);
        backgroundRenderer = new BackgroundRenderer();

        gameWorld.setMapRenderer(mapRenderer);

        gameStage = new GameStage();
        Gdx.input.setInputProcessor(gameStage.getStage());
    }

    @Override
    public void show() {
//        Gdx.input.setInputProcessor(new GameInputProcessor(this, gameWorld));
        System.gc();
    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(gameCamera.getCamera().combined);

        processInput();
        gameWorld.update(delta);
        gameCamera.followPosition(gameWorld.getPlayer().getPosition());

        if(doVfx) {
            vfxManager.cleanUpBuffers();
            vfxManager.beginInputCapture();
        }

        ScreenUtils.clear(1, 1, 1, 1);

        backgroundRenderer.draw(spriteBatch);

        gameCamera.getViewport().apply();
        mapRenderer.setView(gameCamera.getCamera());
        spriteBatch.setProjectionMatrix(gameCamera.getCamera().combined);

        gameWorld.render(spriteBatch);

        if(doVfx) {
            vfxManager.endInputCapture();
            vfxManager.applyEffects();

            vfxManager.renderToScreen();
        }

        if(debugMode) {
            debugRenderer.render(gameWorld.getPhysicsWorld(), gameCamera.getCamera().combined);
        }

        gameStage.render();
    }

    public void processInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            gameWorld.sendInputCommand(InputCommand.JUMP);
        } else {
            gameWorld.sendInputCommand(InputCommand.RESET_JUMP);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_LEFT);
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            gameWorld.sendInputCommand(InputCommand.MOVE_RIGHT);
        } else {
            gameWorld.sendInputCommand(InputCommand.STOP);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            debugMode = !debugMode;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gameWorld.sendInputCommand(InputCommand.RESET);
        }
    }

    @Override
    public void resize(int width, int height) {
        vfxManager.resize(width, height);
        gameCamera.getViewport().update(width, height, true);
        gameStage.resize(width, height);
        backgroundRenderer.resize(width, height);
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

    public CameraManager getGameCamera() {
        return gameCamera;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}
