package com.gdx.game.component.ability;

import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;

/**
 * 游泳能力组件
 * 处理实体的游泳相关逻辑
 */
public class SwimmingComponent implements Component {
    private Entity entity;
    private float maxOxygen = 100f;
    private float currentOxygen;
    private float oxygenDepletionRate = 5f;
    private float swimSpeed = 3f;
    private boolean isSwimming = false;

    @Override
    public void initialize(Entity entity) {
        this.entity = entity;
        this.currentOxygen = maxOxygen;
    }

    @Override
    public void update(float delta) {
        if (isSwimming) {
            updateSwimmingState(delta);
        }
    }

    @Override
    public void dispose() {
        entity = null;
    }

    @Override
    public void receiveMessage(MESSAGE message, String data) {
        if (message == MESSAGE.START_SWIMMING) {
            startSwimming();
        } else if (message == MESSAGE.STOP_SWIMMING) {
            stopSwimming();
        }
    }

    /**
     * 开始游泳
     */
    public void startSwimming() {
        isSwimming = true;
    }

    /**
     * 停止游泳
     */
    public void stopSwimming() {
        isSwimming = false;
        replenishOxygen();
    }

    /**
     * 更新游泳状态
     */
    private void updateSwimmingState(float delta) {
        if (currentOxygen > 0) {
            currentOxygen -= oxygenDepletionRate * delta;
            if (currentOxygen < 0) {
                currentOxygen = 0;
                // 触发氧气耗尽事件
                entity.sendMessage(MESSAGE.OXYGEN_DEPLETED);
            }
        }
    }

    /**
     * 补充氧气
     */
    private void replenishOxygen() {
        currentOxygen = maxOxygen;
    }

    public float getCurrentOxygen() {
        return currentOxygen;
    }

    public boolean isSwimming() {
        return isSwimming;
    }
} 