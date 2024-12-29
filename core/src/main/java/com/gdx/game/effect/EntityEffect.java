package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 实体效果接口
 * 定义了所有效果必须实现的基本方法
 */
public interface EntityEffect {
    /**
     * 获取效果的强度
     * @return 效果强度值
     */
    float getStrength();
    
    /**
     * 获取效果的剩余时间
     * @return 剩余时间(秒)
     */
    float getRemainingTime();
    
    /**
     * 更新效果状态
     * @param delta 时间增量
     */
    void update(float delta);
    
    /**
     * 效果被应用时调用
     * @param target 目标实体
     */
    void onApply(Entity target);
    
    /**
     * 效果被移除时调用
     * @param target 目标实体
     */
    void onRemove(Entity target);
    
    /**
     * 检查效果是否已结束
     * @return true 如果效果已结束
     */
    boolean isFinished();
    
    /**
     * 获取效果的类型
     * @return 效果类型
     */
    EffectType getType();
    
    /**
     * 获取效果的描述
     * @return 效果描述文本
     */
    String getDescription();
} 