package com.gdx.game.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.MathUtils;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.events.BattleEndEvent;
import com.gdx.game.events.EventManager;
import com.gdx.game.gameState.GameState;
import com.gdx.game.gameState.GameStateManager;
import com.gdx.game.items.Item;
import com.gdx.game.quests.QuestManager;
import com.gdx.game.sound.AudioManager;
import com.gdx.game.ui.BattleUI;
import com.gdx.game.components.CombatComponent;

import java.util.List;

public class BattleState {
    private static final Logger LOGGER = LoggerFactory.getLogger(BattleState.class);
    private static final float TURN_DELAY = 1.0f;
    private static final float CRITICAL_MULTIPLIER = 1.5f;

    private final BattleHUD battleHUD;
    private final Entity player;
    private final Entity enemy;
    private BattleTurn currentTurn;
    private float turnTimer;
    private boolean isProcessingTurn;
    private final EventManager eventManager;
    private final GameStateManager gameStateManager;
    private final BattleUI battleUI;

    public enum BattleTurn {
        PLAYER,
        ENEMY
    }

    public BattleState(BattleHUD battleHUD, EventManager eventManager, GameStateManager gameStateManager, BattleUI battleUI) {
        this.battleHUD = battleHUD;
        this.player = battleHUD.getPlayer();
        this.enemy = battleHUD.getEnemy();
        this.currentTurn = BattleTurn.PLAYER;
        this.turnTimer = 0;
        this.isProcessingTurn = false;
        this.eventManager = eventManager;
        this.gameStateManager = gameStateManager;
        this.battleUI = battleUI;
    }

    public void update(float delta) {
        if (isProcessingTurn) {
            turnTimer += delta;
            if (turnTimer >= TURN_DELAY) {
                completeTurn();
            }
            return;
        }

        switch (currentTurn) {
            case PLAYER:
                updatePlayerTurn(delta);
                break;
            case ENEMY:
                updateEnemyTurn(delta);
                break;
        }
    }

    private void updatePlayerTurn(float delta) {
        // 玩家回合逻辑在这里处理
        // 实际的动作（攻击等）会由外部触发
    }

    private void updateEnemyTurn(float delta) {
        if (!isProcessingTurn) {
            performEnemyAction();
        }
    }

    public void performPlayerAction(BattleAction action) {
        if (currentTurn != BattleTurn.PLAYER || isProcessingTurn) {
            return;
        }

        isProcessingTurn = true;
        turnTimer = 0;

        switch (action) {
            case ATTACK:
                performAttack(player, enemy);
                break;
            case DEFEND:
                performDefend(player);
                break;
            case FLEE:
                attemptFlee();
                break;
        }
    }

    private void performEnemyAction() {
        isProcessingTurn = true;
        turnTimer = 0;

        // 简单AI：随机选择攻击或防御
        if (MathUtils.randomBoolean(0.7f)) { // 70%概率攻击
            performAttack(enemy, player);
        } else {
            performDefend(enemy);
        }
    }

    private void performAttack(Entity attacker, Entity target) {
        int attackerAP = Integer.parseInt(attacker.getEntityConfig().getPropertyValue("ENTITY_ATTACK_POINTS"));
        int targetDP = Integer.parseInt(target.getEntityConfig().getPropertyValue("ENTITY_DEFENSE_POINTS"));
        
        // 计算伤害
        int damage = Math.max(1, attackerAP - targetDP);
        
        // 暴击检查
        if (MathUtils.randomBoolean(0.1f)) { // 10%暴击率
            damage = (int)(damage * CRITICAL_MULTIPLIER);
            LOGGER.debug("Critical hit! Damage multiplied by {}", CRITICAL_MULTIPLIER);
        }

        // 应用伤害
        int targetHP = Integer.parseInt(target.getEntityConfig().getPropertyValue("ENTITY_HEALTH_POINTS"));
        targetHP = Math.max(0, targetHP - damage);
        target.getEntityConfig().setPropertyValue("ENTITY_HEALTH_POINTS", String.valueOf(targetHP));

        LOGGER.debug("{} attacks {} for {} damage", attacker.getEntityConfig().getEntityID(), 
            target.getEntityConfig().getEntityID(), damage);
    }

    private void performDefend(Entity entity) {
        // 增加临时防御力
        int currentDP = Integer.parseInt(entity.getEntityConfig().getPropertyValue("ENTITY_DEFENSE_POINTS"));
        entity.getEntityConfig().setPropertyValue("ENTITY_DEFENSE_POINTS", String.valueOf(currentDP + 5));
        
        LOGGER.debug("{} takes defensive stance", entity.getEntityConfig().getEntityID());
    }

    private void attemptFlee() {
        // 50%概率逃跑成功
        if (MathUtils.randomBoolean()) {
            LOGGER.debug("Player successfully fled from battle");
            // TODO: 处理战斗结束
        } else {
            LOGGER.debug("Player failed to flee");
            completeTurn();
        }
    }

    private void completeTurn() {
        isProcessingTurn = false;
        turnTimer = 0;
        
        // 重置防御加成
        if (currentTurn == BattleTurn.PLAYER) {
            resetDefenseBonus(player);
        } else {
            resetDefenseBonus(enemy);
        }
        
        // 切换回合
        currentTurn = (currentTurn == BattleTurn.PLAYER) ? BattleTurn.ENEMY : BattleTurn.PLAYER;
        
        // 检查战斗是否结束
        checkBattleEnd();
    }

    private void resetDefenseBonus(Entity entity) {
        // 重置防御力到基础值
        EntityConfig config = entity.getEntityConfig();
        String baseDP = config.getPropertyValue("ENTITY_BASE_DEFENSE_POINTS");
        config.setPropertyValue("ENTITY_DEFENSE_POINTS", baseDP);
    }

    private void checkBattleEnd() {
        int playerHP = Integer.parseInt(player.getEntityConfig().getPropertyValue("ENTITY_HEALTH_POINTS"));
        int enemyHP = Integer.parseInt(enemy.getEntityConfig().getPropertyValue("ENTITY_HEALTH_POINTS"));

        if (playerHP <= 0) {
            LOGGER.debug("Player defeated");
            // TODO: 处理玩家失败
        } else if (enemyHP <= 0) {
            LOGGER.debug("Enemy defeated");
            // TODO: 处理敌人失败
        }
    }

    public enum BattleAction {
        ATTACK,
        DEFEND,
        FLEE
    }

    public BattleTurn getCurrentTurn() {
        return currentTurn;
    }

    public boolean isProcessingTurn() {
        return isProcessingTurn;
    }

    public Entity getCurrentPlayer() {
        return player;
    }

    public Entity getCurrentEnemy() {
        return enemy;
    }

    private void handleBattleEnd() {
        // 清理战斗状态
        clearBattleEffects();
        
        // 重置战斗组件
        resetCombatComponents();
        
        // 发送战斗结束事件
        eventManager.dispatchEvent(new BattleEndEvent(this));
        
        // 切换回游戏状态
        gameStateManager.changeState(GameState.RUNNING);
    }

    private void handlePlayerDefeat() {
        // 播放失败音效
        AudioManager.getInstance().playSound("defeat");
        
        // 显示失败界面
        battleUI.showDefeatScreen();
        
        // 恢复玩家状态
        player.revive();
        
        // 返回最近的存档点
        player.returnToLastCheckpoint();
        
        // 结束战斗
        handleBattleEnd();
    }

    private void handleEnemyDefeat() {
        // 播放胜利音效
        AudioManager.getInstance().playSound("victory");
        
        // 计算并给予奖励
        calculateAndGrantRewards();
        
        // 显示胜利界面
        battleUI.showVictoryScreen();
        
        // 更新任务进度
        updateQuestProgress();
        
        // 结束战斗
        handleBattleEnd();
    }

    private void clearBattleEffects() {
        player.getEffectManager().clearAllEffects();
        enemy.getEffectManager().clearAllEffects();
    }

    private void resetCombatComponents() {
        player.getComponent(CombatComponent.class).reset();
        enemy.getComponent(CombatComponent.class).reset();
    }

    private void calculateAndGrantRewards() {
        // 计算经验值
        int expGained = enemy.getExpValue();
        player.gainExperience(expGained);
        
        // 掉落物品
        List<Item> drops = enemy.generateDrops();
        for (Item item : drops) {
            player.getInventory().addItem(item);
        }
        
        // 获得金币
        int goldGained = enemy.getGoldValue();
        player.gainGold(goldGained);
    }

    private void updateQuestProgress() {
        QuestManager.getInstance().notifyEnemyDefeated(enemy.getEntityType());
    }
}
