package com.gdx.game.entities;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.component.Component;
import com.gdx.game.entities.factory.EnemyComponentFactory;
import com.gdx.game.entities.factory.EntityComponentFactory;
import com.gdx.game.entities.factory.NPCComponentFactory;
import com.gdx.game.entities.factory.PlayerComponentFactory;

/**
 * 实体工厂类
 * 使用抽象工厂模式创建不同类型的实体
 */
public class EntityFactory {
    private static EntityFactory instance = null;
    private final Map<EntityType, EntityComponentFactory> componentFactories;
    private final Map<String, EntityConfig> entityConfigs;
    private final Json json;

    public enum EntityType {
        WARRIOR,
        MAGE,
        THIEF,
        GRAPPLER,
        CLERIC,
        PLAYER_DEMO,
        ENEMY,
        NPC
    }

    public enum EntityName {
        TOWN_GUARD_WALKING,
        TOWN_BLACKSMITH,
        TOWN_MAGE,
        TOWN_INNKEEPER,
        TOWN_FOLK1, TOWN_FOLK2, TOWN_FOLK3, TOWN_FOLK4, TOWN_FOLK5,
        TOWN_FOLK6, TOWN_FOLK7, TOWN_FOLK8, TOWN_FOLK9, TOWN_FOLK10,
        TOWN_FOLK11, TOWN_FOLK12, TOWN_FOLK13, TOWN_FOLK14, TOWN_FOLK15,
        RABITE, RABITE2,
        QUEST003_TASK002,
        FIRE
    }

    public static final String PLAYER_WARRIOR_CONFIG = "scripts/player_warrior.json";
    public static final String PLAYER_MAGE_CONFIG = "scripts/player_mage.json";
    public static final String PLAYER_THIEF_CONFIG = "scripts/player_thief.json";
    public static final String PLAYER_GRAPPLER_CONFIG = "scripts/player_grappler.json";
    public static final String PLAYER_CLERIC_CONFIG = "scripts/player_cleric.json";

    public static final String TOWN_GUARD_WALKING_CONFIG = "scripts/town_guard_walking.json";
    public static final String TOWN_BLACKSMITH_CONFIG = "scripts/town_blacksmith.json";
    public static final String TOWN_MAGE_CONFIG = "scripts/town_mage.json";
    public static final String TOWN_INNKEEPER_CONFIG = "scripts/town_innkeeper.json";
    public static final String TOWN_FOLK_CONFIGS = "scripts/town_folk.json";
    public static final String ENEMY_CONFIG = "scripts/enemies.json";
    public static final String ENVIRONMENTAL_ENTITY_CONFIGS = "scripts/environmental_entities.json";

    private EntityFactory() {
        componentFactories = new HashMap<>();
        entityConfigs = new HashMap<>();
        json = new Json();
        
        // 注册组件工厂
        componentFactories.put(EntityType.WARRIOR, new PlayerComponentFactory());
        componentFactories.put(EntityType.MAGE, new PlayerComponentFactory());
        componentFactories.put(EntityType.NPC, new NPCComponentFactory());
        componentFactories.put(EntityType.ENEMY, new EnemyComponentFactory());
        
        // 加载实体配置
        loadEntityConfigs();
    }

    private void loadEntityConfigs() {
        // 加载NPC配置
        Array<EntityConfig> townFolkConfigs = Entity.getEntityConfigs(TOWN_FOLK_CONFIGS);
        for(EntityConfig config: townFolkConfigs) {
            entityConfigs.put(config.getEntityID(), config);
        }

        // 加载敌人配置
        Array<EntityConfig> enemyConfigs = Entity.getEntityConfigs(ENEMY_CONFIG);
        for(EntityConfig config: enemyConfigs) {
            entityConfigs.put(config.getEntityID(), config);
        }

        // 加载环境实体配置
        Array<EntityConfig> environmentalEntityConfigs = Entity.getEntityConfigs(ENVIRONMENTAL_ENTITY_CONFIGS);
        for(EntityConfig config: environmentalEntityConfigs) {
            entityConfigs.put(config.getEntityID(), config);
        }

        // 加载特殊NPC配置
        entityConfigs.put(EntityName.TOWN_GUARD_WALKING.toString(), Entity.loadEntityConfigByPath(TOWN_GUARD_WALKING_CONFIG));
        entityConfigs.put(EntityName.TOWN_BLACKSMITH.toString(), Entity.loadEntityConfigByPath(TOWN_BLACKSMITH_CONFIG));
        entityConfigs.put(EntityName.TOWN_MAGE.toString(), Entity.loadEntityConfigByPath(TOWN_MAGE_CONFIG));
        entityConfigs.put(EntityName.TOWN_INNKEEPER.toString(), Entity.loadEntityConfigByPath(TOWN_INNKEEPER_CONFIG));
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    private EntityConfig getEntityConfig(EntityType entityType) {
        String configPath = getConfigPath(entityType);
        if (configPath == null) {
            return null;
        }
        return entityConfigs.get(configPath);
    }

    public Entity getEntity(EntityType entityType) {
        EntityComponentFactory factory = componentFactories.get(entityType);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }

        // 使用工厂创建组件
        Entity entity = new Entity(
            factory.createInputComponent(),
            factory.createPhysicsComponent(),
            factory.createGraphicsComponent()
        );

        // 加载实体配置
        EntityConfig config = getEntityConfig(entityType);
        if (config != null) {
            entity.setEntityConfig(config);
            entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(config));
        }

        return entity;
    }

    private String getConfigPath(EntityType entityType) {
        return switch (entityType) {
            case WARRIOR -> PLAYER_WARRIOR_CONFIG;
            case MAGE -> PLAYER_MAGE_CONFIG;
            case THIEF -> PLAYER_THIEF_CONFIG;
            case GRAPPLER -> PLAYER_GRAPPLER_CONFIG;
            case CLERIC -> PLAYER_CLERIC_CONFIG;
            default -> null;
        };
    }

    public Entity getEntityByName(EntityName entityName) {
        EntityConfig config = entityConfigs.get(entityName.toString());
        if (config == null) {
            throw new IllegalArgumentException("Unknown entity name: " + entityName);
        }

        // 根据配置确定实体类型
        EntityType entityType = "FOE".equals(config.getEntityStatus()) ? 
            EntityType.ENEMY : 
            EntityType.NPC;

        // 使用对应的组件工厂创建实体
        EntityComponentFactory factory = componentFactories.get(entityType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for entity type: " + entityType);
        }

        // 创建实体
        Entity entity = new Entity(
            factory.createInputComponent(),
            factory.createPhysicsComponent(),
            factory.createGraphicsComponent()
        );

        // 设置配置
        entity.setEntityConfig(new EntityConfig(config));
        entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(config));

        return entity;
    }
}
