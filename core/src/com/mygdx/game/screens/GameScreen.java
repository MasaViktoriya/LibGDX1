package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

public class GameScreen implements Screen {
    //private final float STEP = 12;
    private Main game;
    private SpriteBatch batch;
    //private Texture menuButton;
    //private Texture img;
    //private Rectangle menuRect;
    private Anim animation;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    boolean direction = true;
    boolean movingForward = true;
    float x;
    float y;
    private final int[] bg;
    private final int[] l1;
    private final int[] l2;
    //private final ShapeRenderer shapeRenderer;
    private PhysX physX;
    private Body body;
    private final Rectangle heroRect;


    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        //menuButton = new Texture("");
        //img = new Texture("x32-florest-mushroom-06.png");
        //menuRect = new Rectangle(Gdx.graphics.getWidth() - menuButton.getWidth(), Gdx.graphics.getHeight() - menuButton.getHeight(), menuButton.getWidth(), menuButton.getHeight());
        animation = new Anim("bird/bird.atlas", "skeleton-01_fly", Animation.PlayMode.LOOP, 1 / 40f);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.2f;

        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("tiles");
        l2 = new int[1];
        l2[0] = map.getLayers().getIndex("tiles2");
        //shapeRenderer = new ShapeRenderer();

        physX = new PhysX();
        RectangleMapObject hero = (RectangleMapObject) map.getLayers().get("setting").getObjects().get("hero"); //choose by object name
        heroRect = hero.getRectangle();
        body = physX.addObject(hero);
        Array<RectangleMapObject> objects =  map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class); //choose by type
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            body.applyForceToCenter(new Vector2(10000, 0), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            body.applyForceToCenter(new Vector2(-10000, 0), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            body.applyForceToCenter(new Vector2(0, 10000), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            body.applyForceToCenter(new Vector2(0, -10000), true);
        }

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();
        ScreenUtils.clear(Color.WHITE);
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            camera.zoom += 0.01f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && camera.zoom > 0) {
            camera.zoom -= 0.01f;
        }

        mapRenderer.setView(camera);
        mapRenderer.render(bg);
        mapRenderer.render(l1);

        animation.setTime(Gdx.graphics.getDeltaTime());


        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            movingForward = false;
            direction = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            movingForward = true;
            direction = true;
        }
        if (!animation.getFrame().isFlipX() && direction) {
            animation.getFrame().flip(false, false);
        }
        if (animation.getFrame().isFlipX() && direction) {
            animation.getFrame().flip(true, false);
        }
        if (animation.getFrame().isFlipX() && !direction) {
            animation.getFrame().flip(false, false);
        }
        if (!animation.getFrame().isFlipX() && !direction) {
            animation.getFrame().flip(true, false);
        }

        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
       // batch.draw(menuButton, Gdx.graphics.getWidth() - menuButton.getWidth(), Gdx.graphics.getHeight() - menuButton.getHeight());
       // batch.draw(img, heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.draw(animation.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.end();

        mapRenderer.render(l2);

        if (movingForward) {
            x += 1;
        } else {
            x -= 1;
        }
        if (movingForward && x + animation.getFrame().getRegionWidth() >= Gdx.graphics.getWidth()) {
            direction = false;
            movingForward = false;
        }
        if (!movingForward && x == 0) {
            direction = true;
            movingForward = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //int x = Gdx.input.getX();
            //int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            //if (menuRect.contains(x, y)) {
            dispose();
            game.setScreen(new MenuScreen(game));
            // }
        }
/*        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i < objects.size; i++) {
            Rectangle mapSize = objects.get(i).getRectangle();
            shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        }
        shapeRenderer.end();*/

        physX.step();
        physX.debugDraw(camera);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        //this.menuButton.dispose();
        //this.img.dispose();
        this.animation.dispose();
    }
}
