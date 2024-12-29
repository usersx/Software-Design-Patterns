package com.gdx.game.map.worldMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.audio.AudioObserver;
import static com.gdx.game.audio.AudioObserver.AudioTypeEvent.TOPPLE_THEME;
import com.gdx.game.component.Component;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityFactory;
import com.gdx.game.map.Map;
import com.gdx.game.map.MapFactory;

public class ToppleRoad1 extends Map {
    private static final String TAG = ToppleRoad1.class.getSimpleName();
    private final Json json;

    private static final String MAP_PATH = "maps/topple_road1.tmx";
    private static final Vector2 PLAYER_START = new Vector2(10, 10);

    public ToppleRoad1() {
        super(MapFactory.MapType.TOPPLE_ROAD1, MAP_PATH);
        json = new Json();

        // 创建敌人实体
        Entity rabite = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.ENEMY);
        initSpecialEntityPosition(rabite, new Vector2(15, 15));
        mapEntities.add(rabite);

        Entity rabite2 = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.ENEMY);
        initSpecialEntityPosition(rabite2, new Vector2(20, 20));
        mapEntities.add(rabite2);

        playerStart = PLAYER_START;
    }

    private void initSpecialEntityPosition(Entity entity, Vector2 position) {
        entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(position));
        entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
        entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));
    }

    @Override
    public AudioObserver.AudioTypeEvent getMusicTheme() {
        return TOPPLE_THEME;
    }

    @Override
    public void unloadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_STOP, getMusicTheme());
    }

    @Override
    public void loadMusic() {
        notify(AudioObserver.AudioCommand.MUSIC_LOAD, getMusicTheme());
        notify(AudioObserver.AudioCommand.MUSIC_PLAY_LOOP, getMusicTheme());
    }
}
