package com.gdx.game.component;

import com.badlogic.gdx.math.Vector2;
import com.gdx.game.component.ability.CombatComponent;
import com.gdx.game.component.ability.FlyingComponent;
import com.gdx.game.component.ability.SwimmingComponent;

/**
 * 实体构建器
 * 用于简化实体的创建和组件配置
 */
public class EntityBuilder {
    private CompositeEntity entity;

    public EntityBuilder() {
        entity = new CompositeEntity();
    }

    /**
     * 添加飞行能力
     */
    public EntityBuilder withFlying() {
        entity.addComponent(new FlyingComponent());
        return this;
    }

    /**
     * 添加游泳能力
     */
    public EntityBuilder withSwimming() {
        entity.addComponent(new SwimmingComponent());
        return this;
    }

    /**
     * 添加战斗能力
     */
    public EntityBuilder withCombat() {
        entity.addComponent(new CombatComponent());
        return this;
    }

    /**
     * 设置初始位置
     */
    public EntityBuilder atPosition(float x, float y) {
        entity.setPosition(new Vector2(x, y));
        return this;
    }

    /**
     * 设置初始朝向
     */
    public EntityBuilder withRotation(float rotation) {
        entity.setRotation(rotation);
        return this;
    }

    /**
     * 构建实体
     */
    public CompositeEntity build() {
        return entity;
    }
} 