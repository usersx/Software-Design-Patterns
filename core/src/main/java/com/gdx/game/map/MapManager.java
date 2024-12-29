package com.gdx.game.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.component.Component;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.entities.Entity;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.profile.ProfileObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class MapManager implements ProfileObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    private Camera camera;
    private boolean mapChanged = false;
    private com.gdx.game.map.Map currentMap;  // 使用自定义Map类型
    private Entity player;
    private Entity currentSelectedEntity = null;
    private static MapManager instance;
    private HashMap<String, Entity> combatEntities;  // 战斗中的实体

    public MapManager() {
        combatEntities = new HashMap<>();
        instance = this;
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch (event) {
            case PROFILE_LOADED -> {
                String currentMapName = profileManager.getProperty("currentMapType", String.class);
                MapFactory.MapType mapType;
                if (currentMapName == null || currentMapName.isEmpty()) {
                    mapType = MapFactory.MapType.TOPPLE;
                } else {
                    mapType = MapFactory.MapType.valueOf(currentMapName);
                }
                loadMap(mapType);
                
                // 加载位置信息
                loadPositions(profileManager);
            }
            case SAVING_PROFILE -> {
                savePositions(profileManager);
            }
            case CLEAR_CURRENT_PROFILE -> {
                clearCurrentProfile(profileManager);
            }
            default -> {
            }
        }
    }

    private void loadPositions(ProfileManager profileManager) {
        // 加载道路1起始位置
        Vector2 toppleRoad1Pos = profileManager.getProperty("toppleRoad1MapStartPosition", Vector2.class);
        if (toppleRoad1Pos != null) {
            MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).setPlayerStart(toppleRoad1Pos);
        }

        // 加载主城起始位置
        Vector2 topplePos = profileManager.getProperty("toppleMapStartPosition", Vector2.class);
        if (topplePos != null) {
            MapFactory.getMap(MapFactory.MapType.TOPPLE).setPlayerStart(topplePos);
        }

        // 加载当前玩家位置
        Vector2 playerPos = profileManager.getProperty("currentPlayerPosition", Vector2.class);
        if (playerPos != null && !playerPos.equals(new Vector2(0, 0))) {
            Vector2 scaledPos = new Vector2(playerPos.x * 16, playerPos.y * 16);
            if (currentMap != null) {
                MapFactory.getMap(currentMap.getMapType()).setPlayerStart(scaledPos);
            }
        }
    }

    private void savePositions(ProfileManager profileManager) {
        if (currentMap != null) {
            profileManager.setProperty("currentMapType", currentMap.getMapType().toString());
        }
        if (player != null) {
            profileManager.setProperty("currentPlayerPosition", player.getCurrentPosition());
        }
        profileManager.setProperty("toppleMapStartPosition", 
            MapFactory.getMap(MapFactory.MapType.TOPPLE).getPlayerStart());
        profileManager.setProperty("toppleRoad1MapStartPosition", 
            MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).getPlayerStart());
    }

    private void clearCurrentProfile(ProfileManager profileManager) {
        currentMap = null;
        profileManager.setProperty("currentPlayerPosition", null);
        profileManager.setProperty("currentMapType", MapFactory.MapType.TOPPLE.toString());
        MapFactory.clearCache();
        profileManager.setProperty("toppleMapStartPosition", 
            MapFactory.getMap(MapFactory.MapType.TOPPLE).getPlayerStart());
        profileManager.setProperty("toppleRoad1MapStartPosition", 
            MapFactory.getMap(MapFactory.MapType.TOPPLE_ROAD_1).getPlayerStart());
    }

    public void loadMap(MapFactory.MapType mapType) {
        com.gdx.game.map.Map map = MapFactory.getMap(mapType);  // 使用自定义Map类型
        if (map == null) {
            LOGGER.debug("Map does not exist!");
            return;
        }

        // 处理音乐
        if (currentMap != null) {
            currentMap.unloadMusic();
        }
        map.loadMusic();

        // 更新当前地图
        currentMap = map;
        mapChanged = true;
        clearCurrentSelectedMapEntity();
    }

    public void unregisterCurrentMapEntityObservers() {
        if (currentMap != null) {
            Array<Entity> entities = currentMap.getMapEntities();
            for(Entity entity: entities) {
                entity.unregisterObservers();
            }

            Array<Entity> questEntities = currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities) {
                questEntity.unregisterObservers();
            }
        }
    }

    public void registerCurrentMapEntityObservers(ComponentObserver observer) {
        if (currentMap != null) {
            Array<Entity> entities = currentMap.getMapEntities();
            for(Entity entity: entities) {
                entity.registerObserver(observer);
            }

            Array<Entity> questEntities = currentMap.getMapQuestEntities();
            for(Entity questEntity: questEntities) {
                questEntity.registerObserver(observer);
            }
        }
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer() {
        return currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer() {
        return currentMap.getPortalLayer();
    }

    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID) {
        return currentMap.getQuestItemSpawnPositions(objectName, objectTaskID);
    }

    public MapLayer getQuestDiscoverLayer() {
        return currentMap.getQuestDiscoverLayer();
    }

    public MapLayer getEnemySpawnLayer() {
        return currentMap.getEnemySpawnLayer();
    }

    public MapFactory.MapType getCurrentMapType() {
        return currentMap.getCurrentMapType();
    }

    public Vector2 getPlayerStartUnitScaled() {
        return currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap() {
        if (currentMap == null) {
            loadMap(MapFactory.MapType.TOPPLE);
        }
        return currentMap.getCurrentTiledMap();
    }

    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta) {
        currentMap.updateMapEntities(mapMgr, batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities() {
        return currentMap.getMapEntities();
    }

    public final Array<Entity> getCurrentMapQuestEntities() {
        return currentMap.getMapQuestEntities();
    }

    public void addMapQuestEntities(Array<Entity> entities) {
        currentMap.getMapQuestEntities().addAll(entities);
    }

    public void removeMapQuestEntity(Entity entity) {
        entity.unregisterObservers();

        Array<Vector2> positions = ProfileManager.getInstance().getProperty(entity.getEntityConfig().getEntityID(), Array.class);
        if (positions == null) {
            return;
        }

        for(Vector2 position : positions) {
            if (position.x == entity.getCurrentPosition().x && position.y == entity.getCurrentPosition().y) {
                positions.removeValue(position, true);
                break;
            }
        }
        currentMap.getMapQuestEntities().removeValue(entity, true);
        ProfileManager.getInstance().setProperty(entity.getEntityConfig().getEntityID(), positions);
    }

    public void addMapEntity(Entity entity) {
        currentMap.getMapEntities().add(entity);
    }

    public void removeMapEntity(Entity entity) {
        entity.unregisterObservers();

        Vector2 position = entity.getCurrentPosition();
        if (position == null) {
            return;
        }

        currentMap.getMapEntities().removeValue(entity, true);
    }

    public void clearAllMapQuestEntities() {
        if (currentMap != null) {
            currentMap.getMapQuestEntities().clear();
        }
    }

    public Entity getCurrentSelectedMapEntity() {
        return currentSelectedEntity;
    }

    public void setCurrentSelectedMapEntity(Entity currentSelectedEntity) {
        this.currentSelectedEntity = currentSelectedEntity;
    }

    public void clearCurrentSelectedMapEntity() {
        if (currentSelectedEntity == null) {
            return;
        }
        currentSelectedEntity.sendMessage(Component.MESSAGE.ENTITY_DESELECTED);
        currentSelectedEntity = null;
    }

    public void disableCurrentMapMusic() {
        if (currentMap != null) {
            currentMap.unloadMusic();
        }
    }

    public void setPlayer(Entity entity) {
        this.player = entity;
    }

    public Entity getPlayer() {
        return this.player;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean hasMapChanged() {
        return mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged) {
        this.mapChanged = hasMapChanged;
    }

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    /**
     * 获取指定范围内的所有实体
     */
    public List<Entity> getNearbyEntities(Vector2 position, float range) {
        List<Entity> result = new ArrayList<>();
        
        // 检查地图实体
        if (currentMap != null) {
            Array<Entity> mapEntities = currentMap.getMapEntities();
            for (Entity entity : mapEntities) {
                float distance = position.dst(entity.getCurrentPosition());
                if (distance <= range) {
                    result.add(entity);
                }
            }
        }
        
        // 检查战斗实体
        for (Entity entity : combatEntities.values()) {
            float distance = position.dst(entity.getCurrentPosition());
            if (distance <= range) {
                result.add(entity);
            }
        }
        
        return result;
    }
    
    /**
     * 添加战斗实体
     */
    public void addCombatEntity(Entity entity) {
        if (entity != null && entity.getEntityConfig() != null) {
            combatEntities.put(entity.getEntityConfig().getEntityID(), entity);
        }
    }
    
    /**
     * 移除战斗实体
     */
    public void removeCombatEntity(Entity entity) {
        if (entity != null && entity.getEntityConfig() != null) {
            combatEntities.remove(entity.getEntityConfig().getEntityID());
        }
    }
    
    /**
     * 清除所有战斗实体
     */
    public void clearCombatEntities() {
        combatEntities.clear();
    }

    /**
     * 获取当前选中的实体
     */
    public Entity getCurrentSelectedEntity() {
        return currentSelectedEntity;
    }
    
    /**
     * 设置当前选中的实体
     */
    public void setCurrentSelectedEntity(Entity entity) {
        currentSelectedEntity = entity;
    }
}
