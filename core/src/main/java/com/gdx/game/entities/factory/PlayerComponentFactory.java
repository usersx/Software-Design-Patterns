package com.gdx.game.entities.factory;

import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.entities.player.PlayerGraphicsComponent;
import com.gdx.game.entities.player.PlayerInputComponent;
import com.gdx.game.entities.player.PlayerPhysicsComponent;

/**
 * 玩家实体组件工厂
 * 负责创建玩家实体的各个组件
 */
public class PlayerComponentFactory implements EntityComponentFactory {
    @Override
    public InputComponent createInputComponent() {
        return new PlayerInputComponent();
    }

    @Override
    public PhysicsComponent createPhysicsComponent() {
        return new PlayerPhysicsComponent();
    }

    @Override
    public GraphicsComponent createGraphicsComponent() {
        return new PlayerGraphicsComponent();
    }
} 