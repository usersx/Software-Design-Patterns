package com.gdx.game.screen.cutscene;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.screen.AbstractScreen;

public abstract class CutSceneBaseScreen extends AbstractScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger(CutSceneBaseScreen.class);
    private static final Json json = new Json();

    protected final OrthographicCamera camera;
    protected Entity entity;

    public CutSceneBaseScreen() {
        super();
        camera = new OrthographicCamera();
        
        // 设置相机
        setupViewport(10, 10);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        
        LOGGER.debug("CutSceneBaseScreen: physical: ({},{})", VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
    }

    protected void createEntity(EntityFactory.EntityType entityType) {
        // 使用工厂创建实体
        entity = EntityFactory.getInstance().getEntity(entityType);
        
        // 初始化实体状态
        entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
        entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // 渲染过场动画
    }

    @Override
    public void dispose() {
        if (entity != null) {
            entity.dispose();
        }
    }
}
