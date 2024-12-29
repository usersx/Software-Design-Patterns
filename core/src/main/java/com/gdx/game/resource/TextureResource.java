package com.gdx.game.resource;

import com.badlogic.gdx.graphics.Texture;

/**
 * 纹理资源实现类
 */
public class TextureResource implements GameResource {
    private Texture texture;
    private String path;
    private boolean loaded;

    public TextureResource(String path) {
        this.path = path;
        this.loaded = false;
    }

    @Override
    public void load() {
        if (!loaded) {
            texture = new Texture(path);
            loaded = true;
        }
    }

    @Override
    public void unload() {
        if (loaded && texture != null) {
            texture.dispose();
            texture = null;
            loaded = false;
        }
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public Texture getTexture() {
        if (!loaded) {
            load();
        }
        return texture;
    }
} 