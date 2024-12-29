package com.gdx.game.component.ability;

import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;

/**
 * 飞行能力组件
 * 处理实体的飞行相关逻辑
 */
public class FlyingComponent implements Component {
    private Entity entity;
    private float maxAltitude = 100f;
    private float currentAltitude = 0f;
    private float flyingSpeed = 5f;
    private boolean isFlying = false;

    @Override
    public void initialize(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void update(float delta) {
        if (isFlying) {
            // 更新飞行高度和位置
            updateFlyingState(delta);
        }
    }

    @Override
    public void dispose() {
        entity = null;
    }

    @Override
    public void receiveMessage(MESSAGE messageType, String... args) {
        switch (messageType) {
            case FLYING_START:
                startFlying();
                break;
            case FLYING_STOP:
                stopFlying();
                break;
            case ALTITUDE_CHANGE:
                if (args.length > 0) {
                    changeAltitude(Float.parseFloat(args[0]));
                }
                break;
        }
    }

    /**
     * 开始飞行
     */
    public void startFlying() {
        isFlying = true;
    }

    /**
     * 停止飞行
     */
    public void stopFlying() {
        isFlying = false;
        currentAltitude = 0f;
    }

    /**
     * 改变飞行高度
     */
    public void changeAltitude(float delta) {
        currentAltitude = Math.max(0, Math.min(maxAltitude, currentAltitude + delta));
    }

    /**
     * 更新飞行状态
     */
    private void updateFlyingState(float delta) {
        // 在这里实现具体的飞行物理逻辑
        // 比如重力影响、风阻等
    }

    public float getCurrentAltitude() {
        return currentAltitude;
    }

    public boolean isFlying() {
        return isFlying;
    }
} 