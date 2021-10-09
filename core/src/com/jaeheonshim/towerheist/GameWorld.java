package com.jaeheonshim.towerheist;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
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
import com.jaeheonshim.towerheist.game.objects.*;
import com.jaeheonshim.towerheist.game.GameObject;
import com.jaeheonshim.towerheist.game.physics.FixtureType;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;
import com.jaeheonshim.towerheist.game.physics.WorldContactListener;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    public static final Vector2 GRAVITY = new Vector2(0, -57);

    private World physicsWorld;

    private TiledMap gameMap;
    private MapLayer staticColliderLayer;
    private MapLayer checkpointsLayer;
    private MapLayer sawLayer;
    private MapLayer cannonLayer;
    private MapLayer lavaLayer;
    private MapLayer laserLayer;

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

        this.playerTrail = new PlayerTrail(this, currentZ++);
        this.renderManager.addItem(this.playerTrail);
        this.gameObjects.add(this.playerTrail);

        configureLava();
        configureSaws();
        configureMap();
        configureCannons();
        configureLaser();
        configureCheckpoints();
        configureColliders();

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
    }

    public void configureMap() {
        renderManager.addItem(new MapRenderer(() -> (this.mapRenderer), currentZ++));
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

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((rectangle.x + rectangle.width / 2) / GameScreen.PPM, (rectangle.y + rectangle.height / 2) / GameScreen.PPM));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((rectangle.width / 2f) / GameScreen.PPM, (rectangle.height / 2f) / GameScreen.PPM);

        FixtureDef floorFixture = new FixtureDef();
        floorFixture.shape = shape;

        Body body = physicsWorld.createBody(bodyDef);
        body.createFixture(floorFixture).setUserData(new FixtureUserData(FixtureType.BLOCK));

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

        for(GameObject gameObject : gameObjects) {
            gameObject.update(delta);
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
        renderManager.render(batch);

        player.draw(batch);
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
