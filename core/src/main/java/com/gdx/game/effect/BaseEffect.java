package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 效果基础抽象类
 * 实现了EntityEffect接口的通用功能
 */
public abstract class BaseEffect implements EntityEffect {
    protected float duration;
    protected float remainingTime;
    protected float strength;
    protected Entity target;
    protected boolean isFinished;
    protected EffectType type;
    
    public BaseEffect(float duration, float strength, EffectType type) {
        this.duration = duration;
        this.remainingTime = duration;
        this.strength = strength;
        this.type = type;
        this.isFinished = false;
    }
    
    @Override
    public void update(float delta) {
        if (isFinished) {
            return;
        }
        
        remainingTime -= delta;
        if (remainingTime <= 0) {
            isFinished = true;
            return;
        }
        
        applyEffect(delta);
    }
    
    @Override
    public void onApply(Entity target) {
        this.target = target;
        onEffectStart();
    }
    
    @Override
    public void onRemove(Entity target) {
        onEffectEnd();
        this.target = null;
    }
    
    @Override
    public float getStrength() {
        return strength;
    }
    
    @Override
    public float getRemainingTime() {
        return remainingTime;
    }
    
    @Override
    public boolean isFinished() {
        return isFinished;
    }
    
    @Override
    public EffectType getType() {
        return type;
    }
    
    @Override
    public String getDescription() {
        return String.format("%s (%.1f秒)", type.getDescription(), remainingTime);
    }
    
    /**
     * 应用效果的具体逻辑
     * @param delta 时间增量
     */
    protected abstract void applyEffect(float delta);
    
    /**
     * 效果开始时的额外逻辑
     */
    protected void onEffectStart() {
        // 默认空实现
    }
    
    /**
     * 效果结束时的额外逻辑
     */
    protected void onEffectEnd() {
        // 默认空实现
    }
} 