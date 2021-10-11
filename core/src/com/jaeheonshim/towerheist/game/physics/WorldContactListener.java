package com.jaeheonshim.towerheist.game.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.towerheist.game.GameWorld;
import com.jaeheonshim.towerheist.game.Player;

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
        Fixture fixture;

        if((fixture = checkXOR(contact, FixtureClass.DOOR)) != null) {
            DoorFixtureUserData doorFixtureUserData = ((DoorFixtureUserData) fixture.getUserData());
            if(!doorFixtureUserData.getDoor().isClosed()) {
                contact.setEnabled(false);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixture;

        if((fixture = checkXOR(contact, FixtureClass.DOOR)) != null) {
            DoorFixtureUserData doorFixtureUserData = ((DoorFixtureUserData) fixture.getUserData());
            if(!doorFixtureUserData.getDoor().isClosed()) {
                contact.setEnabled(false);
            }
        }
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

    public Fixture checkXOR(Contact contact, FixtureType type) {
        if(fixtureIsType(contact.getFixtureA(), type) && fixtureIsType(contact.getFixtureB(), type)) {
            return null;
        }

        if(fixtureIsType(contact.getFixtureA(), type)) {
            return contact.getFixtureA();
        }

        if(fixtureIsType(contact.getFixtureB(), type)) {
            return contact.getFixtureB();
        }

        return null;
    }

    public Fixture checkXOR(Contact contact, FixtureClass type) {
        if(fixtureIsClass(contact.getFixtureA(), type) && fixtureIsClass(contact.getFixtureB(), type)) {
            return null;
        }

        if(fixtureIsClass(contact.getFixtureA(), type)) {
            return contact.getFixtureA();
        }

        if(fixtureIsClass(contact.getFixtureB(), type)) {
            return contact.getFixtureB();
        }

        return null;
    }

    public boolean fixtureIsType(Fixture fixture, FixtureType fixtureType) {
        return fixture.getUserData() instanceof FixtureUserData && ((FixtureUserData) fixture.getUserData()).getFixtureType() == fixtureType;
    }

    public boolean fixtureIsClass(Fixture fixture, FixtureClass clazz) {
        return fixture.getUserData() instanceof FixtureUserData && ((FixtureUserData) fixture.getUserData()).getFixtureClass() == clazz;
    }
}
