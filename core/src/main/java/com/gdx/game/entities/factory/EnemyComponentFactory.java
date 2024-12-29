package com.gdx.game.entities.factory;

import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.entities.npc.enemy.EnemyGraphicsComponent;
import com.gdx.game.entities.npc.enemy.EnemyInputComponent;
import com.gdx.game.entities.npc.enemy.EnemyPhysicsComponent;

/**
 * 敌人实体组件工厂
 * 负责创建敌人实体的各个组件
 */
public class EnemyComponentFactory implements EntityComponentFactory {
    @Override
    public InputComponent createInputComponent() {
        return new EnemyInputComponent();
    }

    @Override
    public PhysicsComponent createPhysicsComponent() {
        return new EnemyPhysicsComponent();
    }

    @Override
    public GraphicsComponent createGraphicsComponent() {
        return new EnemyGraphicsComponent();
    }
} 