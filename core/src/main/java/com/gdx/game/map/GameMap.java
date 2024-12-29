package com.gdx.game.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;

public abstract class GameMap implements Map {
    protected final String mapPath;
    protected MapFactory.MapType mapType;
    protected final Array<Entity> mapEntities;
    protected final Array<Entity> mapQuestEntities;
    protected Vector2 playerStart;
    protected TiledMap currentMap;
    
    public GameMap(String mapPath, MapFactory.MapType mapType) {
        this.mapPath = mapPath;
        this.mapType = mapType;
        this.mapEntities = new Array<>();
        this.mapQuestEntities = new Array<>();
        this.playerStart = new Vector2(0, 0);
        loadMap();
    }
    
    private void loadMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
        currentMap = new TmxMapLoader().load(mapPath);
    }
    
    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {
        for (Entity entity : mapEntities) {
            entity.update(mapMgr, batch, delta);
        }
        for (Entity entity : mapQuestEntities) {
            entity.update(mapMgr, batch, delta);
        }
    }
    
    @Override
    public Array<Entity> getMapEntities() {
        return mapEntities;
    }
    
    @Override
    public Array<Entity> getMapQuestEntities() {
        return mapQuestEntities;
    }
    
    @Override
    public Vector2 getPlayerStart() {
        return playerStart;
    }
    
    @Override
    public void setPlayerStart(Vector2 position) {
        this.playerStart = position;
    }
    
    @Override
    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        setPlayerStart(new Vector2(position.x * getTileWidth(), position.y * getTileHeight()));
    }
    
    @Override
    public TiledMap getCurrentTiledMap() {
        return currentMap;
    }
    
    @Override
    public MapFactory.MapType getMapType() {
        return mapType;
    }
    
    @Override
    public float getTileWidth() {
        return currentMap != null ? currentMap.getProperties().get("tilewidth", Integer.class) : 16f;
    }
    
    @Override
    public float getTileHeight() {
        return currentMap != null ? currentMap.getProperties().get("tileheight", Integer.class) : 16f;
    }
    
    @Override
    public void loadMusic() {
        // 默认实现为空，子类可以根据需要重写
    }
    
    @Override
    public void unloadMusic() {
        // 默认实现为空，子类可以根据需要重写
    }
    
    @Override
    public void dispose() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }
    
    @Override
    public Vector2 getPlayerStartUnitScaled() {
        return new Vector2(
            playerStart.x / getTileWidth(),
            playerStart.y / getTileHeight()
        );
    }
    
    // 抽象方法，需要子类实现
    @Override
    public abstract MapLayer getCollisionLayer();
    
    @Override
    public abstract MapLayer getPortalLayer();
    
    @Override
    public abstract MapLayer getQuestDiscoverLayer();
    
    @Override
    public abstract MapLayer getEnemySpawnLayer();
    
    @Override
    public abstract Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID);
} 