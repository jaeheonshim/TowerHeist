package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.physics.box2d.*;

import java.util.Objects;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body fA = contact.getFixtureA().getBody();
        Body fB = contact.getFixtureB().getBody();
        Player player;

        if((Objects.equals(fA.getUserData(), "FLOOR") ^ Objects.equals(fB.getUserData(), "FLOOR")) && (player = getPlayer(contact)) != null) {
            player.setTouchingGround(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body fA = contact.getFixtureA().getBody();
        Body fB = contact.getFixtureB().getBody();
        Player player;

        if((Objects.equals(fA.getUserData(), "FLOOR") ^ Objects.equals(fB.getUserData(), "FLOOR")) && (player = getPlayer(contact)) != null) {
            player.setTouchingGround(false);
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
}
