package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    public final float PPM = 100;
    public ContList contactListener;

    public PhysX() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(contactListener);
    }

    public Body addObject(RectangleMapObject object){
        Rectangle rectangle = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        if(type.equals("StaticBody")){
            def.type = BodyDef.BodyType.StaticBody;
        }
        if(type.equals("DynamicBody")){
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set((rectangle.x + rectangle.width/2)/PPM, (rectangle.y +rectangle.height/2)/PPM);
        def.gravityScale = (float) object.getProperties().get("gravityScale");
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width/2/PPM, rectangle.height/2/PPM);
        fDef.shape = polygonShape;
        fDef.friction = 0.3f;
        fDef.density = 0.1f;
        fDef.restitution = (float) object.getProperties().get("restitution");
        Body body = world.createBody(def);
        String name = object.getName();
        if (name!= null && name.equals("hero")){
            polygonShape.setAsBox(rectangle.width/3/PPM, rectangle.height/12/PPM, new Vector2(0, -rectangle.width/2/PPM), 0);
            body.createFixture(fDef).setUserData("sensor");
            body.getFixtureList().get(body.getFixtureList().size-1).setSensor(true);
        }
        body.createFixture(fDef).setUserData(name);
        polygonShape.dispose();
        return body;
    }

    public void destroyBody(Body body){
        world.destroyBody(body);
    }

    public void setGravity(Vector2 gravity){
        world.setGravity(gravity);
    }

    public void step(){
        world.step(1/60.0f, 3,3);
    }

    public void debugDraw(OrthographicCamera camera){
        debugRenderer.render(world, camera.combined);
    }
    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }
}
