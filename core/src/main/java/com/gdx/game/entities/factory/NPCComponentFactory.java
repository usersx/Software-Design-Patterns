package com.gdx.game.entities.factory;

import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.entities.npc.NPCGraphicsComponent;
import com.gdx.game.entities.npc.NPCInputComponent;
import com.gdx.game.entities.npc.NPCPhysicsComponent;

/**
 * NPC实体组件工厂
 * 负责创建NPC实体的各个组件
 */
public class NPCComponentFactory implements EntityComponentFactory {
    @Override
    public InputComponent createInputComponent() {
        return new NPCInputComponent();
    }

    @Override
    public PhysicsComponent createPhysicsComponent() {
        return new NPCPhysicsComponent();
    }

    @Override
    public GraphicsComponent createGraphicsComponent() {
        return new NPCGraphicsComponent();
    }
} 