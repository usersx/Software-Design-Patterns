package com.gdx.game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.gdx.game.entities.Entity;

/**
 * 地图管理器
 * 负责管理地图上的实体和空间查询
 */
public class MapManager {
    private List<Entity> entities;
    private Map<String, Entity> entityMap;

    public MapManager() {
        entities = new ArrayList<>();
        entityMap = new HashMap<>();
    }

    /**
     * 添加实体到地图
     * @param entity 要添加的实体
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * 获取指定位置周围的实体
     * @param position 中心位置
     * @param radius 搜索半径
     * @return 在范围内的实体列表
     */
    public List<Entity> getNearbyEntities(Vector2 position, float radius) {
        List<Entity> result = new ArrayList<>();
        float radiusSquared = radius * radius;

        for (Entity entity : entities) {
            Vector2 entityPos = entity.getCurrentPosition();
            if (position.dst2(entityPos) <= radiusSquared) {
                result.add(entity);
            }
        }

        return result;
    }

    /**
     * 更新所有实体
     * @param delta 时间增量
     */
    public void update(float delta) {
        for (Entity entity : entities) {
            entity.update(delta);
        }
    }

    /**
     * 移除实体
     * @param entity 要移除的实体
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * 清除所有实体
     */
    public void clearEntities() {
        entities.clear();
    }
} 