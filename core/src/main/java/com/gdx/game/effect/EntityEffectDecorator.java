package com.gdx.game.effect;

import com.gdx.game.entities.Entity;

/**
 * 实体效果装饰器基类
 * 实现效果的包装逻辑
 */
public abstract class EntityEffectDecorator implements EntityEffect {
    protected EntityEffect decoratedEffect;
    protected float duration;
    protected float remainingTime;

    public EntityEffectDecorator(EntityEffect effect, float duration) {
        this.decoratedEffect = effect;
        this.duration = duration;
        this.remainingTime = duration;
    }

    @Override
    public void apply(Entity entity, float delta) {
        if (decoratedEffect != null) {
            decoratedEffect.apply(entity, delta);
        }
        remainingTime -= delta;
    }

    @Override
    public boolean isFinished() {
        return remainingTime <= 0;
    }
} 