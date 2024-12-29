package com.gdx.game.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gdx.game.GdxGame;
import com.gdx.game.battle.BattleHUD;
import com.gdx.game.battle.BattleState;
import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.entities.player.PlayerHUD;
import com.gdx.game.manager.ResourceManager;
import com.gdx.game.map.MapManager;

public class BattleScreen extends BaseScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger(BattleScreen.class);
    private static final Json json = new Json();
    private static final float VIEWPORT_WIDTH = 10f;
    private static final float VIEWPORT_HEIGHT = 10f;

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BattleHUD battleHUD;
    private final BattleState battleState;
    private Entity player;
    private Entity enemy;

    public BattleScreen(GdxGame gdxGame, PlayerHUD playerHUD, MapManager mapManager, ResourceManager resourceManager) {
        super(gdxGame, resourceManager);
        
        // 设置相机和视口
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        viewport.apply(true);
        
        // 从PlayerHUD获取玩家实体
        this.player = playerHUD.getPlayer();
        
        // 创建敌人实体
        this.enemy = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.ENEMY);
        enemy.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(enemy.getEntityConfig().getState()));
        enemy.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(enemy.getEntityConfig().getDirection()));
        
        // 创建战斗HUD和状态
        battleHUD = new BattleHUD(player, enemy);
        battleState = new BattleState(battleHUD);
        
        LOGGER.debug("BattleScreen: viewport: ({},{})", VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        viewport.apply();
        battleState.update(delta);
        battleHUD.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        battleHUD.dispose();
        if (player != null) {
            player.dispose();
        }
        if (enemy != null) {
            enemy.dispose();
        }
    }
}
