package com.jaeheonshim.towerheist.game.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.GameWorld;
import com.jaeheonshim.towerheist.Player;
import com.jaeheonshim.towerheist.game.objects.Bullet;
import com.jaeheonshim.towerheist.game.objects.Checkpoint;

import java.util.Objects;

public class WorldContactListener implements ContactListener {
    private GameWorld world;

    public WorldContactListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        Fixture player = getPlayer(contact);

        if(player != null) {
            Fixture otherFixture = (player == fA ? fB : fA);
            changePlayerContact(contact, player, otherFixture, ((PlayerFixtureUserData) player.getUserData()), ((FixtureUserData) otherFixture.getUserData()), true);
        }

        checkBulletCollision(fA);
        checkBulletCollision(fB);
    }

    public void changePlayerContact(Contact contact, Fixture player, Fixture other, PlayerFixtureUserData playerData, FixtureUserData otherData, boolean begin) {
        if(otherData.getFixtureType() == FixtureType.BLOCK) {
            switch(playerData.getDirection()) {
                case BOTTOM: {
                    if(begin) {
                        world.getPlayer().setTouchingGround(true);
                        playerData.setCurrentBlock(other);
                    } else if(playerData.getCurrentBlock() == null || other == playerData.getCurrentBlock()) {
                        world.getPlayer().setTouchingGround(false);
                    }

                    break;
                }
                case LEFT:
                    world.getPlayer().setTouchingWallR(begin);
                    break;
                case RIGHT:
                    world.getPlayer().setTouchingWallL(begin);
                    break;
            }
        } else if(otherData.getFixtureType() == FixtureType.CHECKPOINT && begin) {
            CheckpointFixtureUserData checkpointData = ((CheckpointFixtureUserData) otherData);
            world.setCurrentCheckpoint(checkpointData.getCheckpoint());
        } else if(otherData.getFixtureType() == FixtureType.DEATH && begin) {
            world.queueDeath();
        }
    }

    public void checkBulletCollision(Fixture fixture) {
        if(fixture.getUserData() instanceof BulletFixtureUserData) {
            BulletFixtureUserData bulletFixtureUserData = ((BulletFixtureUserData) fixture.getUserData());
            bulletFixtureUserData.getBullet().setQueueDestroy(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        Fixture player = getPlayer(contact);

        if(player != null) {
            Fixture otherFixture = (player == fA ? fB : fA);
            changePlayerContact(contact, player, otherFixture, ((PlayerFixtureUserData) player.getUserData()), ((FixtureUserData) otherFixture.getUserData()), false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Fixture getPlayer(Contact contact) {
        if(contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureA().getBody().getUserData() instanceof Player) {
            return contact.getFixtureA();
        }

        if(contact.getFixtureB().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() instanceof Player) {
            return contact.getFixtureB();
        }

        return null;
    }

    public Fixture checkXOR(Contact contact, Object userData) {
        if(Objects.equals(contact.getFixtureA().getUserData(), userData) && Objects.equals(contact.getFixtureB().getUserData(), userData)) {
            return null;
        }

        if(Objects.equals(contact.getFixtureA().getUserData(), userData)) {
            return contact.getFixtureA();
        }

        if(Objects.equals(contact.getFixtureB().getUserData(), userData)) {
            return contact.getFixtureB();
        }

        return null;
    }
}
