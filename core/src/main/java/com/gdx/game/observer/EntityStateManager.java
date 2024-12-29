package com.gdx.game.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gdx.game.effect.EntityEffect;
import com.gdx.game.entities.Entity;

/**
 * 实体状态管理器
 * 负责管理实体状态并通知观察者
 */
public class EntityStateManager {
    private Entity entity;
    private List<EntityObserver> observers;
    private float health;
    private int level;
    private Set<EntityEffect> statusEffects;

    public EntityStateManager(Entity entity) {
        this.entity = entity;
        this.observers = new ArrayList<>();
        this.statusEffects = new HashSet<>();
        this.health = 100f;
        this.level = 1;
    }

    public void addObserver(EntityObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(EntityObserver observer) {
        observers.remove(observer);
    }

    public void setHealth(float newHealth) {
        float oldHealth = this.health;
        this.health = newHealth;
        notifyHealthChanged(oldHealth, newHealth);
    }

    public void levelUp() {
        int oldLevel = this.level;
        this.level++;
        notifyLevelUp(oldLevel, this.level);
    }

    public void addStatusEffect(EntityEffect effect) {
        if (statusEffects.add(effect)) {
            notifyStatusEffectChanged(effect, true);
        }
    }

    public void removeStatusEffect(EntityEffect effect) {
        if (statusEffects.remove(effect)) {
            notifyStatusEffectChanged(effect, false);
        }
    }

    private void notifyHealthChanged(float oldHealth, float newHealth) {
        for (EntityObserver observer : observers) {
            observer.onHealthChanged(entity, oldHealth, newHealth);
        }
    }

    private void notifyLevelUp(int oldLevel, int newLevel) {
        for (EntityObserver observer : observers) {
            observer.onLevelUp(entity, oldLevel, newLevel);
        }
    }

    private void notifyStatusEffectChanged(EntityEffect effect, boolean added) {
        for (EntityObserver observer : observers) {
            observer.onStatusEffectChanged(entity, effect, added);
        }
    }

    public float getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public Set<EntityEffect> getStatusEffects() {
        return Collections.unmodifiableSet(statusEffects);
    }
} 