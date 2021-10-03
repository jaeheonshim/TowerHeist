package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.bigjaeheon.game.Checkpoint;
import com.sun.tools.javac.comp.Check;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    public static final Vector2 GRAVITY = new Vector2(0, -57);

    private World physicsWorld;

    private TiledMap gameMap;
    private MapLayer staticColliderLayer;
    private MapLayer checkpointsLayer;

    private Player player;
    private List<Checkpoint> checkpoints;
    private int currentCheckpoint = -1;

    public GameWorld() {
        physicsWorld = new World(GRAVITY, true);
        physicsWorld.setContactListener(new WorldContactListener(this));

        player = new Player(physicsWorld);
//        createTestGround();
        loadMap();
        configureColliders();
        configureCheckpoints();
    }

    private void loadMap() {
        this.gameMap = new TmxMapLoader().load("map.tmx");
        this.staticColliderLayer = this.gameMap.getLayers().get("StaticColliders");
        this.checkpointsLayer = this.gameMap.getLayers().get("Checkpoints");
    }

    private void configureCheckpoints() {
        checkpoints = new ArrayList<>();

        MapObjects objects = checkpointsLayer.getObjects();
        int id = 0;
        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Checkpoint checkpoint = new Checkpoint(physicsWorld, new Rectangle(rect.x / GameScreen.PPM, rect.y / GameScreen.PPM, rect.width / GameScreen.PPM, rect.height / GameScreen.PPM), id++);
            checkpoints.add(checkpoint);
        }
    }

    private void configureColliders() {
        MapObjects objects = staticColliderLayer.getObjects();
        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            createTiledBody(object);
        }
    }

    private Body createTiledBody(RectangleMapObject object) {
        Rectangle rectangle = object.getRectangle();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((rectangle.x + rectangle.width / 2) / GameScreen.PPM, (rectangle.y + rectangle.height) / GameScreen.PPM));

        FixtureDef floorFixture = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((rectangle.width / 2) / GameScreen.PPM, 0.85f / GameScreen.PPM, new Vector2(0, -1.25f / GameScreen.PPM), 0);
        floorFixture.shape = shape;

        FixtureDef leftWallFixture = new FixtureDef();
        PolygonShape leftWallShape = new PolygonShape();
        leftWallShape.setAsBox(1 / GameScreen.PPM, rectangle.height / GameScreen.PPM / 2, new Vector2((-rectangle.width / 2 + 1.2f) / GameScreen.PPM, -(rectangle.height + 2) / GameScreen.PPM / 2), 0);
        leftWallFixture.shape = leftWallShape;

        FixtureDef rightWallFixture = new FixtureDef();
        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(1 / GameScreen.PPM, rectangle.height / GameScreen.PPM / 2, new Vector2((rectangle.width / 2 - 1.2f) / GameScreen.PPM, -(rectangle.height + 2) / GameScreen.PPM / 2), 0);
        rightWallFixture.shape = rightWallShape;

        Body body = physicsWorld.createBody(bodyDef);
        body.createFixture(floorFixture).setUserData("FLOOR");
        body.createFixture(leftWallFixture).setUserData("WALL_L");
        body.createFixture(rightWallFixture).setUserData("WALL_R");

        return body;
    }

    private void createTestGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(40, 0));

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40, 1);
        fixtureDef.shape = shape;

        Body body = physicsWorld.createBody(bodyDef);
        body.setUserData("FLOOR");
        body.createFixture(fixtureDef);
    }

    public void update(float delta) {
        physicsWorld.step(1/60f, 6, 2);
        player.update(delta);
    }

    public void render(SpriteBatch batch) {
        for(Checkpoint checkpoint : checkpoints) {
            checkpoint.draw(batch, currentCheckpoint == checkpoint.getId());
        }

        player.draw(batch);
    }

    public void renderMap(OrthogonalTiledMapRenderer mapRenderer) {
        mapRenderer.render();
    }

    public void sendInputCommand(InputCommand inputCommand) {
        switch (inputCommand) {
            case JUMP:
                player.jump();
                break;
            case MOVE_LEFT:
                player.move(true);
                break;
            case MOVE_RIGHT:
                player.move(false);
                break;
            case STOP:
                player.stop();
                break;
            case RESET_JUMP:
                player.setCanJump(true);
                break;
        }
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public TiledMap getGameMap() {
        return gameMap;
    }

    public Player getPlayer() {
        return player;
    }

    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }
}
