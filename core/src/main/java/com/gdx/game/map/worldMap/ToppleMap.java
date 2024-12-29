package com.gdx.game.map.worldMap;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.map.GameMap;
import com.gdx.game.map.MapFactory;

public class ToppleMap extends GameMap {
    private static final String MAP_PATH = "maps/topple.tmx";
    
    public ToppleMap() {
        super(MAP_PATH, MapFactory.MapType.TOPPLE);
    }
    
    @Override
    public MapLayer getCollisionLayer() {
        return currentMap.getLayers().get("collision");
    }
    
    @Override
    public MapLayer getPortalLayer() {
        return currentMap.getLayers().get("portal");
    }
    
    @Override
    public MapLayer getQuestDiscoverLayer() {
        return currentMap.getLayers().get("quest_discover");
    }
    
    @Override
    public MapLayer getEnemySpawnLayer() {
        return currentMap.getLayers().get("enemy_spawn");
    }
    
    @Override
    public Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID) {
        Array<Vector2> positions = new Array<>();
        MapLayer questLayer = currentMap.getLayers().get("quest_items");
        
        if (questLayer != null && questLayer.getObjects() != null) {
            for (MapObject mapObject : questLayer.getObjects()) {
                if (mapObject.getProperties().containsKey("type") && 
                    mapObject.getProperties().containsKey("name")) {
                    
                    String type = mapObject.getProperties().get("type", String.class);
                    String name = mapObject.getProperties().get("name", String.class);
                    
                    if ((objectTaskID == null || objectTaskID.equals(type)) && 
                        (objectName == null || objectName.equals(name))) {
                        
                        float x = mapObject.getProperties().get("x", Float.class);
                        float y = mapObject.getProperties().get("y", Float.class);
                        
                        positions.add(new Vector2(x, y));
                    }
                }
            }
        }
        
        return positions;
    }
} 