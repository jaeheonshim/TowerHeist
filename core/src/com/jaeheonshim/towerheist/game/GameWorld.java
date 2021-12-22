package com.jaeheonshim.towerheist.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.InputCommand;
import com.jaeheonshim.towerheist.game.objects.*;
import com.jaeheonshim.towerheist.game.objects.GameObject;
import com.jaeheonshim.towerheist.game.physics.FixtureType;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;
import com.jaeheonshim.towerheist.game.physics.WorldContactListener;
import com.jaeheonshim.towerheist.game.render.MapRenderer;
import com.jaeheonshim.towerheist.game.render.RenderManager;
import com.jaeheonshim.towerheist.util.Countdown;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class GameWorld {
    public static final Vector2 GRAVITY = new Vector2(0, -67);

    private World physicsWorld;

    private TiledMap gameMap;
    private MapLayer staticColliderLayer;
    private MapLayer checkpointsLayer;
    private MapLayer sawLayer;
    private MapLayer cannonLayer;
    private MapLayer lavaLayer;
    private MapLayer laserLayer;
    private MapLayer doorLayer;
    private MapLayer keyLayer;

    private Player player;
    private Vector2 initialPos = new Vector2(5, 10);
    private Checkpoint currentCheckpoint;

    private OrthogonalTiledMapRenderer mapRenderer;
    private RenderManager renderManager;
    private int currentZ = 0;

    private PlayerTrail playerTrail;
    private DeathParticles deathParticles;

    private List<GameObject> gameObjects = new ArrayList<>();

    private Countdown deathCountdown = new Countdown(1.5f);
    private boolean queueDeath = false;
    private boolean isDead = false;

    public GameWorld() {
        physicsWorld = new World(GRAVITY, true);
        physicsWorld.setContactListener(new WorldContactListener(this));

        renderManager = new RenderManager();

        player = new Player(this, initialPos);
//        createTestGround();
        loadMap();

        configureLava();
        configureSaws();
        configureMap();
        configureCannons();
        configureLaser();
        configureCheckpoints();
        configureDoors();
        configureKeys();
        configureColliders();

        this.playerTrail = new PlayerTrail(this, currentZ++);
        this.renderManager.addItem(this.playerTrail);
        this.gameObjects.add(this.playerTrail);

        this.deathParticles = new DeathParticles(this, 100);
        this.renderManager.addItem(this.deathParticles);
        this.gameObjects.add(this.deathParticles);

        renderManager.initialize();
    }

    private void loadMap() {
        this.gameMap = new TmxMapLoader().load("map.tmx");
        this.staticColliderLayer = this.gameMap.getLayers().get("StaticColliders");
        this.checkpointsLayer = this.gameMap.getLayers().get("Checkpoints");
        this.sawLayer = this.gameMap.getLayers().get("Saw");
        this.cannonLayer = this.gameMap.getLayers().get("Cannon");
        this.lavaLayer = this.gameMap.getLayers().get("Lava");
        this.laserLayer = this.gameMap.getLayers().get("Laser");
        this.doorLayer = this.gameMap.getLayers().get("Door");
        this.keyLayer = this.gameMap.getLayers().get("Key");
    }

    public void configureMap() {
        renderManager.addItem(new MapRenderer(() -> (this.mapRenderer), currentZ++));
    }

    private void configureKeys() {
        MapObjects objects = keyLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();
            Vector2 position = new Vector2(rectangle.x / GameScreen.PPM, rectangle.y / GameScreen.PPM);
            Key key = new Key(this, position, currentZ);
            gameObjects.add(key);
            renderManager.addItem(key);
        }

        currentZ++;
    }

    private void configureDoors() {
        MapObjects objects = doorLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();
            Door door = new Door(this, new Vector2(rectangle.x / GameScreen.PPM, rectangle.y / GameScreen.PPM), rectangle.height / GameScreen.PPM, currentZ);
            gameObjects.add(door);
            renderManager.addItem(door);
        }

        currentZ++;
    }

    private void configureLaser() {
        MapObjects objects = laserLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Vector2 point = new Vector2(object.getRectangle().x / GameScreen.PPM, object.getRectangle().y / GameScreen.PPM);
            Laser saw = new Laser(this, point, currentZ);
            gameObjects.add(saw);
            renderManager.addItem(saw);
        }

        currentZ++;
    }

    private void configureLava() {
        MapObjects objects = lavaLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Lava lava = new Lava(this, object, currentZ);
            renderManager.addItem(lava);
        }

        currentZ++;
    }

    private void configureSaws() {
        MapObjects objects = sawLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Vector2 point = new Vector2(object.getRectangle().x / GameScreen.PPM, object.getRectangle().y / GameScreen.PPM);
            Saw saw = new Saw(this, point, currentZ);
            gameObjects.add(saw);
            renderManager.addItem(saw);
        }

        for(PolylineMapObject object : objects.getByType(PolylineMapObject.class)) {
            Polyline polyline = object.getPolyline();
            Vector2 start = new Vector2(polyline.getX(), polyline.getY()).scl(1 / GameScreen.PPM);
            Vector2 end = new Vector2(polyline.getX() + polyline.getVertices()[2], polyline.getY() + polyline.getVertices()[3]).scl(1 / GameScreen.PPM);

            Saw saw = new Saw(this, start, end, currentZ);
            gameObjects.add(saw);
            renderManager.addItem(saw);
        }

        currentZ++;
    }

    private void configureCannons() {
        MapObjects objects = cannonLayer.getObjects();

        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Vector2 point = new Vector2(object.getRectangle().x / GameScreen.PPM, object.getRectangle().y / GameScreen.PPM);
            Cannon cannon = new Cannon(this, point, currentZ);
            gameObjects.add(cannon);
            renderManager.addItem(cannon);
        }

        currentZ++;
    }

    private void configureCheckpoints() {
        MapObjects objects = checkpointsLayer.getObjects();

        int id = 0;
        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();
            Checkpoint checkpoint = new Checkpoint(this, new Rectangle(rect.x / GameScreen.PPM, rect.y / GameScreen.PPM, rect.width / GameScreen.PPM, rect.height / GameScreen.PPM), id++, currentZ);
            gameObjects.add(checkpoint);
            renderManager.addItem(checkpoint);
        }

        MapObject endObject = objects.get("END");
        if(endObject == null) {
            throw new RuntimeException("Error: Map contains no END checkpoint");
        }

        Rectangle endPoint = ((RectangleMapObject) endObject).getRectangle();

        Elevator elevator = new Elevator(this, new Vector2(endPoint.x / GameScreen.PPM, endPoint.y / GameScreen.PPM), currentZ);
        gameObjects.add(elevator);
        renderManager.addItem(elevator);

        currentZ++;
    }

    private void configureColliders() {
        MapObjects objects = staticColliderLayer.getObjects();
        for(RectangleMapObject object : objects.getByType(RectangleMapObject.class)) {
            createTiledBody(object);
        }
    }

    private Body createTiledBody(RectangleMapObject object) {
        Rectangle rectangle = object.getRectangle();
        boolean platform = Objects.equals(object.getProperties().get("platform", Boolean.class), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((rectangle.x + rectangle.width / 2) / GameScreen.PPM, (rectangle.y + rectangle.height / 2) / GameScreen.PPM));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((rectangle.width / 2f) / GameScreen.PPM, (rectangle.height / 2f) / GameScreen.PPM);

        FixtureDef floorFixture = new FixtureDef();
        floorFixture.shape = shape;

        Body body = physicsWorld.createBody(bodyDef);
        body.createFixture(floorFixture).setUserData(new FixtureUserData(platform ? FixtureType.PLATFORM : FixtureType.BLOCK));

        shape.dispose();
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
        deathCountdown.update(delta);

        physicsWorld.step(1/60f, 6, 2);

        player.update(delta);

        Iterator<GameObject> gameObjectIterator = gameObjects.iterator();
        while(gameObjectIterator.hasNext()) {
            GameObject gameObject = gameObjectIterator.next();
            gameObject.update(delta);

            if(gameObject.isQueueDestroy()) {
                gameObject.destroy();
                renderManager.remove(gameObject);
                gameObjectIterator.remove();

                if(player.getCurrentCarryObject() == gameObject) {
                    player.stopCarry();
                }
            }
        }

        if(queueDeath) {
            this.queueDeath = false;
            this.death();
        }

        if(isDead) {
            whileDead();
        }
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        renderManager.render(batch);
        player.draw(batch);

        batch.end();
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
            case RESET:
                death();
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

    public void setCurrentCheckpoint(Checkpoint currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    public void death() {
        this.isDead = true;
        deathCountdown.reset();
        player.stop();
        player.getBody().setActive(false);
        deathParticles.doEffect(player.getPosition());
    }

    public void queueDeath() {
        this.queueDeath = true;
    }

    private void whileDead() {
        if(deathCountdown.isFinished()) {
            deathCountdown.reset();
            resetAll();
            tpToLastCheckpoint();
            this.isDead = false;
            player.getBody().setActive(true);
        }
    }

    public void tpToLastCheckpoint() {
        if(currentCheckpoint != null) {
            Vector2 position = currentCheckpoint.getBody().getPosition();
            position.x += Player.WIDTH;
            position.y += Player.WIDTH;

            player.setPosition(position);
        } else {
            player.setPosition(initialPos);
        }

        player.getBody().setAwake(true);
    }

    public GameObject getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setMapRenderer(OrthogonalTiledMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public PlayerTrail getPlayerTrail() {
        return playerTrail;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public boolean isDead() {
        return isDead;
    }

    public void resetAll() {
        for(GameObject gameObject : gameObjects) {
            gameObject.reset();
        }
    }
}
