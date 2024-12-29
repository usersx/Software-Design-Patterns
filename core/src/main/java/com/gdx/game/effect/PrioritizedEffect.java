package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 带优先级的效果包装类
 * 为效果添加优先级属性
 */
public class PrioritizedEffect implements EntityEffect {
    private EntityEffect effect;
    private EffectPriority priority;

    public PrioritizedEffect(EntityEffect effect, EffectPriority priority) {
        this.effect = effect;
        this.priority = priority;
    }

    @Override
    public void apply(Entity entity, float delta) {
        effect.apply(entity, delta);
    }

    @Override
    public boolean isFinished() {
        return effect.isFinished();
    }

    public EffectPriority getPriority() {
        return priority;
    }

    public EntityEffect getEffect() {
        return effect;
    }
} 