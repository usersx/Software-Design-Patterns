package com.gdx.game.resource;

/**
 * 游戏资源接口
 * 定义了资源的基本操作
 */
public interface GameResource {
    /**
     * 加载资源
     */
    void load();

    /**
     * 卸载资源
     */
    void unload();

    /**
     * 检查资源是否已加载
     */
    boolean isLoaded();
} 