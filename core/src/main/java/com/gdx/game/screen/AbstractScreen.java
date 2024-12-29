package com.gdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractScreen implements Screen {
    protected static class ViewportData {
        public float physicalWidth;
        public float physicalHeight;
        public float viewportWidth;
        public float viewportHeight;
    }

    protected static final ViewportData VIEWPORT = new ViewportData();
    protected Viewport viewport;

    protected void setupViewport(float worldWidth, float worldHeight) {
        // 计算视口尺寸
        float aspectRatio = (float)worldHeight/(float)worldWidth;
        VIEWPORT.viewportWidth = worldWidth;
        VIEWPORT.viewportHeight = worldHeight;
        VIEWPORT.physicalWidth = worldWidth;
        VIEWPORT.physicalHeight = worldHeight * aspectRatio;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height);
        }
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
        if (viewport != null) {
            viewport = null;
        }
    }
} 