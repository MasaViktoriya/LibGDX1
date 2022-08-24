package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    int click;
    Anim animation;
    boolean direction = true;
    boolean movingForward = true;
    float x;
    float y;

    @Override
    public void create() {
        batch = new SpriteBatch();
        animation = new Anim("cardinal.jpg", 5, 2, Animation.PlayMode.LOOP);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        animation.setTime(Gdx.graphics.getDeltaTime());

        // float x = Gdx.input.getX() - animation.getFrame().getRegionWidth() / 2;
        // float y = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.getFrame().getRegionHeight() / 2;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            click++;
        }
        Gdx.graphics.setTitle("Clicked " + click + " times!");
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            direction = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            direction = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            movingForward = false;
            direction = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
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

        batch.begin();
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        animation.dispose();
    }
}
