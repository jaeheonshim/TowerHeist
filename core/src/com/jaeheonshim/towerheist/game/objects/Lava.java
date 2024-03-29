package com.jaeheonshim.towerheist.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jaeheonshim.towerheist.Assets;
import com.jaeheonshim.towerheist.game.GameScreen;
import com.jaeheonshim.towerheist.game.GameWorld;
import com.jaeheonshim.towerheist.game.physics.FixtureType;
import com.jaeheonshim.towerheist.game.physics.FixtureUserData;

public class Lava extends GameObject {
    private TextureRegion texture;
    private Rectangle rectangle;

    public Lava(GameWorld gameWorld, RectangleMapObject object, int zIndex) {
        super(gameWorld, zIndex);

        texture = Assets.instance().fromAtlas("lava");
        rectangle = new Rectangle(
                object.getRectangle().x / GameScreen.PPM,
                object.getRectangle().y / GameScreen.PPM,
                object.getRectangle().width / GameScreen.PPM,
                object.getRectangle().height / GameScreen.PPM
                );

        setupPhysics(gameWorld);
    }

    private void setupPhysics(GameWorld gameWorld) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(rectangle.getX() + rectangle.width / 2, rectangle.getY() + rectangle.height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        gameWorld.getPhysicsWorld().createBody(bodyDef).createFixture(fixtureDef).setUserData(new FixtureUserData(FixtureType.DEATH));

        shape.dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
