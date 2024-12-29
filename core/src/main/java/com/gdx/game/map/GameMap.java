package com.gdx.game.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.gdx.game.audio.AudioManager;
import com.gdx.game.audio.AudioObserver;

public class GameMap extends Map {
    private TiledMap tiledMap;
    private String mapPath;
    private Vector2 playerStartPosition;
    private Array<Entity> mapEntities;
    private Array<Entity> mapQuestEntities;
    private MapFactory.MapType mapType;

    public GameMap(String mapPath) {
        this.mapPath = mapPath;
        this.playerStartPosition = new Vector2(0, 0);
        this.mapEntities = new Array<>();
        this.mapQuestEntities = new Array<>();
        loadMap();
    }

    private void loadMap() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
        
        tiledMap = new TmxMapLoader().load(mapPath);
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
    }

    @Override
    public TiledMap getCurrentTiledMap() {
        return tiledMap;
    }

    @Override
    public Vector2 getPlayerStart() {
        return playerStartPosition;
    }

    @Override
    public void setPlayerStart(Vector2 position) {
        this.playerStartPosition = position;
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
    public MapLayer getCollisionLayer() {
        return tiledMap.getLayers().get("collision");
    }

    @Override
    public MapLayer getPortalLayer() {
        return tiledMap.getLayers().get("portal");
    }

    @Override
    public MapLayer getQuestDiscoverLayer() {
        return tiledMap.getLayers().get("quest_discover");
    }

    @Override
    public MapLayer getEnemySpawnLayer() {
        return tiledMap.getLayers().get("enemy_spawn");
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
    public void setMapType(MapFactory.MapType type) {
        this.mapType = type;
    }

    @Override
    public MapFactory.MapType getMapType() {
        return mapType;
    }

    @Override
    public Vector2 getPlayerStartUnitScaled() {
        return super.getPlayerStartUnitScaled();
    }

    @Override
    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        super.setClosestStartPositionFromScaledUnits(position);
    }

    @Override
    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectType) {
        Array<Vector2> positions = new Array<>();
        MapLayer questLayer = tiledMap.getLayers().get("quest_items");
        
        if (questLayer != null && questLayer.getObjects() != null) {
            // 遍历所有地图对象
            questLayer.getObjects().forEach(mapObject -> {
                // 检查对象类型和名称是否匹配
                if (mapObject.getProperties().containsKey("type") && 
                    mapObject.getProperties().containsKey("name")) {
                    
                    String type = mapObject.getProperties().get("type", String.class);
                    String name = mapObject.getProperties().get("name", String.class);
                    
                    if ((objectType == null || objectType.equals(type)) && 
                        (objectName == null || objectName.equals(name))) {
                        
                        // 获取对象位置（Tiled中的坐标）
                        float x = mapObject.getProperties().get("x", Float.class);
                        float y = mapObject.getProperties().get("y", Float.class);
                        
                        // 转换为游戏世界坐标（像素坐标）
                        Vector2 position = new Vector2(x, y);
                        positions.add(position);
                    }
                }
            });
        }
        
        return positions;
    }

    @Override
    public void loadMusic() {
        AudioManager.getInstance().onNotify(AudioObserver.AudioCommand.MUSIC_LOAD, getMusicTheme());
        AudioManager.getInstance().onNotify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, getMusicTheme());
    }

    @Override
    public void unloadMusic() {
        AudioManager.getInstance().onNotify(AudioObserver.AudioCommand.MUSIC_STOP, getMusicTheme());
    }

    private AudioObserver.AudioTypeEvent getMusicTheme() {
        // 根据地图类型返回对应的音乐主题
        return switch (mapType) {
            case TOPPLE -> AudioObserver.AudioTypeEvent.TOPPLE_THEME;
            case BATTLE_FIELD -> AudioObserver.AudioTypeEvent.BATTLE_THEME;
            default -> AudioObserver.AudioTypeEvent.TOPPLE_THEME;
        };
    }
} 