package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {
    private final float STEP = 12;
    private Main game;
    private SpriteBatch batch;
    //private Texture menuButton;
    //private Rectangle menuRect;
    private Anim animation;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Rectangle mapSize;
    boolean direction = true;
    boolean movingForward = true;
    float x;
    float y;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        //menuButton = new Texture("");
        //menuRect = new Rectangle(Gdx.graphics.getWidth() - menuButton.getWidth(), Gdx.graphics.getHeight() - menuButton.getHeight(), menuButton.getWidth(), menuButton.getHeight());
        animation = new Anim("bird/bird.atlas", "skeleton-01_fly", Animation.PlayMode.LOOP, 1 / 40f);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        //Array<RectangleMapObject> mapObject = map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class); // choose by type
        RectangleMapObject mapObject = (RectangleMapObject) map.getLayers().get("objects").getObjects().get("camera"); //choose by object name
        camera.position.x =  mapObject.getRectangle().x;
        camera.position.y = mapObject.getRectangle().y;
        camera.zoom = 1.2f;
        RectangleMapObject mapBoard = (RectangleMapObject) map.getLayers().get("objects").getObjects().get("mapsize");
        mapSize = mapBoard.getRectangle();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && mapSize.x < camera.position.x -1 ){
            camera.position.x -= STEP;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && mapSize.x + mapSize.width > camera.position.x +1 ){
            camera.position.x += STEP;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            camera.position.y += STEP;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            camera.position.y -= STEP;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)){
            camera.zoom += 0.01f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && camera.zoom >0){
            camera.zoom -= 0.01f;
        }
        camera.update();
        ScreenUtils.clear(Color.WHITE);
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
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //batch.draw(menuButton, Gdx.graphics.getWidth() - menuButton.getWidth(), Gdx.graphics.getHeight() - menuButton.getHeight());
        batch.setProjectionMatrix(camera.combined);
        batch.draw(animation.getFrame(), x, y);
        batch.end();

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
        this.animation.dispose();
    }
}
