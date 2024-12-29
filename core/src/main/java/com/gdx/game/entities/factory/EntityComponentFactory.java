package com.gdx.game.entities.factory;

import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;

/**
 * 实体组件工厂接口
 * 定义了创建实体各个组件的方法
 */
public interface EntityComponentFactory {
    /**
     * 创建输入组件
     */
    InputComponent createInputComponent();

    /**
     * 创建物理组件
     */
    PhysicsComponent createPhysicsComponent();

    /**
     * 创建图形组件
     */
    GraphicsComponent createGraphicsComponent();
} 