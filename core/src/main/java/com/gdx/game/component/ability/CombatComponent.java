package com.gdx.game.component.ability;

import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;
import java.util.List;
import java.util.stream.Collectors;
import com.badlogic.gdx.math.Vector2;

/**
 * 战斗能力组件
 * 处理实体的战斗相关逻辑
 */
public class CombatComponent implements Component {
    private Entity entity;
    private float attackDamage = 10f;
    private float attackRange = 2f;
    private float attackCooldown = 1f;
    private float currentCooldown = 0f;
    private boolean canAttack = true;

    @Override
    public void initialize(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void update(float delta) {
        if (!canAttack) {
            currentCooldown -= delta;
            if (currentCooldown <= 0) {
                canAttack = true;
                currentCooldown = 0f;
            }
        }
    }

    @Override
    public void dispose() {
        entity = null;
    }

    @Override
    public void receiveMessage(MESSAGE message, String data) {
        if (message == MESSAGE.ATTACK) {
            attack();
        }
    }

    /**
     * 执行攻击
     */
    public void attack() {
        if (canAttack) {
            // 在这里实现具体的攻击逻辑
            // 比如检测范围内的敌人并造成伤害
            performAttack();
            
            // 重置冷却
            canAttack = false;
            currentCooldown = attackCooldown;
        }
    }

    /**
     * 执行具体的攻击逻辑
     */
    private void performAttack() {
        // TODO: 实现具体的攻击检测和伤害计算
        entity.sendMessage(MESSAGE.ATTACK_PERFORMED, String.valueOf(attackDamage));
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float damage) {
        this.attackDamage = damage;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float range) {
        this.attackRange = range;
    }

    public boolean isReady() {
        return canAttack;
    }

    private float calculateDamage(Entity attacker, Entity target) {
        // 获取基础攻击力
        float baseDamage = attacker.getAttackPower();
        
        // 获取目标防御力
        float targetDefense = target.getDefense();
        
        // 计算暴击
        boolean isCritical = calculateCriticalHit(attacker);
        float criticalMultiplier = isCritical ? 1.5f : 1.0f;
        
        // 应用效果修正
        float effectModifier = calculateEffectModifier(attacker);
        
        // 计算最终伤害
        float finalDamage = (baseDamage * effectModifier * criticalMultiplier) - targetDefense;
        
        // 确保最小伤害为1
        return Math.max(1, finalDamage);
    }

    private boolean calculateCriticalHit(Entity attacker) {
        float criticalChance = attacker.getCriticalChance();
        return Math.random() < criticalChance;
    }

    private float calculateEffectModifier(Entity entity) {
        float modifier = 1.0f;
        
        // 获取所有伤害修正效果
        List<DamageModifierEffect> effects = entity.getEffectManager()
            .getActiveEffects()
            .stream()
            .filter(effect -> effect instanceof DamageModifierEffect)
            .map(effect -> (DamageModifierEffect) effect)
            .collect(Collectors.toList());
        
        // 应用所有修正
        for (DamageModifierEffect effect : effects) {
            modifier *= effect.getDamageMultiplier();
        }
        
        return modifier;
    }

    private boolean checkAttackRange(Entity attacker, Entity target) {
        // 获取攻击者位置
        Vector2 attackerPos = attacker.getPosition();
        
        // 获取目标位置
        Vector2 targetPos = target.getPosition();
        
        // 计算距离
        float distance = attackerPos.dst(targetPos);
        
        // 获取攻击范围
        float attackRange = attacker.getAttackRange();
        
        // 检查是否在攻击范围内
        return distance <= attackRange;
    }
} 