package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.physics.box2d.*;
import com.jaeheonshim.bigjaeheon.game.Checkpoint;

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
        Player player;

        if((Objects.equals(fA.getUserData(), "FLOOR") ^ Objects.equals(fB.getUserData(), "FLOOR")) && (player = getPlayer(contact)) != null) {
            player.setTouchingGround(true);
        }

        if(checkXOR(contact, "WALL_L") != null && (player = getPlayer(contact)) != null) {
            player.setTouchingWallL(true);
        }

        if(checkXOR(contact, "WALL_R") != null && (player = getPlayer(contact)) != null) {
            player.setTouchingWallR(true);
        }

        Fixture fixture;
        if((fixture = checkXOR(contact, "CHECKPOINT")) != null && (player = getPlayer(contact)) != null) {
            world.setCurrentCheckpoint(((Checkpoint) fixture.getBody().getUserData()).getId());
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        Player player;

        if((Objects.equals(fA.getUserData(), "FLOOR") ^ Objects.equals(fB.getUserData(), "FLOOR")) && (player = getPlayer(contact)) != null) {
            player.setTouchingGround(false);
        }

        if(checkXOR(contact, "WALL_L") != null && (player = getPlayer(contact)) != null) {
            player.setTouchingWallL(false);
        }

        if(checkXOR(contact, "WALL_R") != null && (player = getPlayer(contact)) != null) {
            player.setTouchingWallR(false);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Player getPlayer(Contact contact) {
        if(contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureA().getBody().getUserData() instanceof Player) {
            return (Player) contact.getFixtureA().getBody().getUserData();
        }

        if(contact.getFixtureB().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() instanceof Player) {
            return (Player) contact.getFixtureB().getBody().getUserData();
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
