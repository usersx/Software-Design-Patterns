package com.gdx.game.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.game.entities.Entity;

public interface Map {
    void updateMapEntities(MapManager mapMgr, Batch batch, float delta);
    Array<Entity> getMapEntities();
    Array<Entity> getMapQuestEntities();
    MapLayer getCollisionLayer();
    MapLayer getPortalLayer();
    MapLayer getQuestDiscoverLayer();
    MapLayer getEnemySpawnLayer();
    Vector2 getPlayerStart();
    void setPlayerStart(Vector2 position);
    void setClosestStartPositionFromScaledUnits(Vector2 position);
    Array<Vector2> getQuestItemSpawnPositions(String objectName, String objectTaskID);
    TiledMap getCurrentTiledMap();
    void loadMusic();
    void unloadMusic();
    void dispose();
    MapFactory.MapType getMapType();
    float getTileWidth();
    float getTileHeight();
    Vector2 getPlayerStartUnitScaled();
}
