package com.gdx.game.screen;

import java.util.Map;
import static java.util.stream.Collectors.toMap;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.profile.ProfileManager;

public class CharacterSelectionScreen extends AbstractScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSelectionScreen.class);
    private static final Json json = new Json();

    private final OrthographicCamera camera;
    private final Map<String, Entity> characters;

    public CharacterSelectionScreen() {
        super();
        camera = new OrthographicCamera();
        
        // 设置相机
        setupViewport(10, 10);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        
        // 创建所有可选角色
        characters = createCharacters();
        
        LOGGER.debug("CharacterSelectionScreen: physical: ({},{})", VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
    }

    private Map<String, Entity> createCharacters() {
        // 创建所有可选角色
        EntityFactory.EntityType[] characterTypes = {
            EntityFactory.EntityType.WARRIOR,
            EntityFactory.EntityType.MAGE
        };

        return Stream.of(characterTypes)
            .collect(toMap(
                EntityFactory.EntityType::name,
                type -> {
                    Entity entity = EntityFactory.getInstance().getEntity(type);
                    entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
                    entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));
                    return entity;
                }
            ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // 渲染角色选择界面
    }

    @Override
    public void dispose() {
        characters.values().forEach(Entity::dispose);
    }

    public void selectCharacter(String characterType) {
        // 保存选择的角色类型
        ProfileManager.getInstance().setProperty("playerCharacter", EntityFactory.EntityType.valueOf(characterType));
    }
}
