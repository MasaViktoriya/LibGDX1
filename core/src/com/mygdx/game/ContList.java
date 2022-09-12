package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameScreen;

public class ContList implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData()!= null && b.getUserData()!= null){
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if(tmpA.equals("hero")&& tmpB.equals("coin")){
                GameScreen.bodies.add(b.getBody());
            }
            if(tmpB.equals("hero")&&tmpA.equals("coin")){
                GameScreen.bodies.add(a.getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
