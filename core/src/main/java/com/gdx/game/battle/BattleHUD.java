package com.gdx.game.battle;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.game.entities.Entity;
import com.gdx.game.manager.ResourceManager;

public class BattleHUD implements Disposable {
    private final Entity player;
    private final Entity enemy;
    private final Stage stage;
    private final Table playerStats;
    private final Table enemyStats;
    private final Label playerHP;
    private final Label playerMP;
    private final Label enemyHP;

    public BattleHUD(Entity player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;
        this.stage = new Stage();

        // 创建玩家状态面板
        playerStats = new Table(ResourceManager.skin);
        playerStats.setPosition(10, 10);
        playerStats.add(new Label("Player", ResourceManager.skin)).row();
        playerHP = new Label("HP: " + getPlayerHP(), ResourceManager.skin);
        playerStats.add(playerHP).row();
        playerMP = new Label("MP: " + getPlayerMP(), ResourceManager.skin);
        playerStats.add(playerMP);

        // 创建敌人状态面板
        enemyStats = new Table(ResourceManager.skin);
        enemyStats.setPosition(stage.getWidth() - 110, 10);
        enemyStats.add(new Label("Enemy", ResourceManager.skin)).row();
        enemyHP = new Label("HP: " + getEnemyHP(), ResourceManager.skin);
        enemyStats.add(enemyHP);

        // 添加到舞台
        stage.addActor(playerStats);
        stage.addActor(enemyStats);
    }

    public void render(float delta) {
        // 更新状态显示
        playerHP.setText("HP: " + getPlayerHP());
        playerMP.setText("MP: " + getPlayerMP());
        enemyHP.setText("HP: " + getEnemyHP());

        // 渲染舞台
        stage.act(delta);
        stage.draw();
    }

    private int getPlayerHP() {
        return Integer.parseInt(player.getEntityConfig().getPropertyValue("ENTITY_HEALTH_POINTS"));
    }

    private int getPlayerMP() {
        return Integer.parseInt(player.getEntityConfig().getPropertyValue("ENTITY_MAGIC_POINTS"));
    }

    private int getEnemyHP() {
        return Integer.parseInt(enemy.getEntityConfig().getPropertyValue("ENTITY_HEALTH_POINTS"));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Entity getPlayer() {
        return player;
    }

    public Entity getEnemy() {
        return enemy;
    }

    public Stage getStage() {
        return stage;
    }
}
