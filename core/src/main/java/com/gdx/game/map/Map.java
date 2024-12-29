package com.gdx.game.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Map {
    public abstract void dispose();
    public abstract TiledMap getCurrentTiledMap();
    public abstract Vector2 getPlayerStart();
    public abstract void setPlayerStart(Vector2 position);
    public abstract Array<Entity> getMapEntities();
    public abstract Array<Entity> getMapQuestEntities();
    public abstract MapLayer getCollisionLayer();
    public abstract MapLayer getPortalLayer();
    public abstract MapLayer getQuestDiscoverLayer();
    public abstract MapLayer getEnemySpawnLayer();
    public abstract void updateMapEntities(MapManager mapMgr, Batch batch, float delta);
    public abstract void setMapType(MapFactory.MapType type);
    public abstract MapFactory.MapType getMapType();
    
    // 音乐相关方法
    public void loadMusic() {
        // 默认实现为空
    }
    
    public void unloadMusic() {
        // 默认实现为空
    }

    // 新增方法
    public Vector2 getPlayerStartUnitScaled() {
        Vector2 pos = getPlayerStart();
        return new Vector2(pos.x / 16, pos.y / 16);
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        setPlayerStart(new Vector2(position.x * 16, position.y * 16));
    }

    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectType) {
        Array<Vector2> positions = new Array<>();
        // 默认实现返回空数组
        return positions;
    }
}
