package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

import java.awt.*;

public class MenuScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture img;
    private Rectangle startRect;
    private ShapeRenderer shapeRenderer;
    private final Music music;
    private final Sound sound;

    public MenuScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        img = new Texture("localbird.png");
        startRect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        shapeRenderer = new ShapeRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal("Fooled Around and Fell In Love   Soundtrack Wonder Band.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        sound = Gdx.audio.newSound(Gdx.files.internal("353227__tec-studio__brd17.mp3"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.SKY);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(startRect.x, startRect.y, startRect.width, startRect.height);
        shapeRenderer.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if(startRect.contains(x, y)){
                dispose();
                game.setScreen(new GameScreen(game));
            }else {
                sound.play();
            }
        }

    }

    @Override
    public void resize(int width, int height) {

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
        this.img.dispose();
        this.shapeRenderer.dispose();
        this.music.dispose();
    }
}
