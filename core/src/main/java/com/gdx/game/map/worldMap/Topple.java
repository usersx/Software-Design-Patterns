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

public class Topple extends Map {
    private static final String TAG = Topple.class.getSimpleName();
    private final Json json;

    private static final String MAP_PATH = "maps/topple.tmx";
    private static final Vector2 PLAYER_START = new Vector2(10, 10);

    public Topple() {
        super(MapFactory.MapType.TOPPLE, MAP_PATH);
        json = new Json();

        // 创建NPC实体
        Entity innKeeper = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        initSpecialEntityPosition(innKeeper, new Vector2(15, 15));
        mapEntities.add(innKeeper);

        Entity townfolk1 = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        initSpecialEntityPosition(townfolk1, new Vector2(20, 10));
        mapEntities.add(townfolk1);

        Entity townfolk2 = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        initSpecialEntityPosition(townfolk2, new Vector2(25, 12));
        mapEntities.add(townfolk2);

        Entity townfolk3 = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        initSpecialEntityPosition(townfolk3, new Vector2(30, 15));
        mapEntities.add(townfolk3);

        Entity townfolk4 = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        initSpecialEntityPosition(townfolk4, new Vector2(35, 18));
        mapEntities.add(townfolk4);

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
