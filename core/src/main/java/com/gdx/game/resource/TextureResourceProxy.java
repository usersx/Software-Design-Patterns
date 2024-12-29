package com.gdx.game.resource;

import com.badlogic.gdx.graphics.Texture;

/**
 * 纹理资源代理类
 * 实现延迟加载和资源管理
 */
public class TextureResourceProxy implements GameResource {
    private TextureResource realResource;
    private String path;

    public TextureResourceProxy(String path) {
        this.path = path;
    }

    @Override
    public void load() {
        if (realResource == null) {
            realResource = new TextureResource(path);
        }
        realResource.load();
    }

    @Override
    public void unload() {
        if (realResource != null) {
            realResource.unload();
            realResource = null;
        }
    }

    @Override
    public boolean isLoaded() {
        return realResource != null && realResource.isLoaded();
    }

    public Texture getTexture() {
        if (realResource == null) {
            load();
        }
        return realResource.getTexture();
    }
} 