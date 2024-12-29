package com.gdx.game.component;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.gdx.game.entities.Entity;

/**
 * 组合实体类
 * 使用组件组合而不是继承来实现实体功能
 */
public class CompositeEntity extends Entity {
    private Map<Class<? extends Component>, Component> components;
    private Vector2 position;
    private float rotation;

    public CompositeEntity() {
        super(null, null, null); // 调用父类构造函数
        components = new HashMap<>();
        position = new Vector2();
    }

    /**
     * 添加组件
     * @param component 要添加的组件
     */
    public void addComponent(Component component) {
        component.initialize(this);
        components.put(component.getClass(), component);
    }

    /**
     * 获取指定类型的组件
     * @param componentClass 组件类型
     * @return 组件实例，如果不存在则返回null
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    /**
     * 移除组件
     * @param componentClass 要移除的组件类型
     */
    public void removeComponent(Class<? extends Component> componentClass) {
        Component component = components.remove(componentClass);
        if (component != null) {
            component.dispose();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Component component : components.values()) {
            component.update(delta);
        }
    }

    /**
     * 发送消息给所有组件
     * @param message 消息类型
     * @param data 消息数据
     */
    @Override
    public void sendMessage(Component.MESSAGE message, String... args) {
        super.sendMessage(message, args);
        String data = String.join("::", args);
        for (Component component : components.values()) {
            component.receiveMessage(message, data);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Component component : components.values()) {
            component.dispose();
        }
        components.clear();
    }

    @Override
    public Vector2 getCurrentPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
} 