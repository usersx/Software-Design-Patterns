package com.gdx.game.map;

import com.badlogic.gdx.utils.ObjectMap;

public class MapFactory {
    private static ObjectMap<MapType, Map> mapTable = new ObjectMap<>();
    private static ObjectMap<MapType, String> mapPathTable = new ObjectMap<>();

    public enum MapType {
        TOPPLE,         // 主城地图
        TOPPLE_ROAD_1,  // 主城道路1
        TOPPLE_ROAD_2,  // 主城道路2
        CASTLE,         // 城堡
        DUNGEON,        // 地下城
        BATTLE_FIELD    // 战场
    }

    static {
        mapPathTable.put(MapType.TOPPLE, "maps/topple.tmx");
        mapPathTable.put(MapType.TOPPLE_ROAD_1, "maps/topple_road_1.tmx");
        mapPathTable.put(MapType.TOPPLE_ROAD_2, "maps/topple_road_2.tmx");
        mapPathTable.put(MapType.CASTLE, "maps/castle.tmx");
        mapPathTable.put(MapType.DUNGEON, "maps/dungeon.tmx");
        mapPathTable.put(MapType.BATTLE_FIELD, "maps/battle_field.tmx");
    }

    public static Map getMap(MapType mapType) {
        Map map = mapTable.get(mapType);
        if (map == null) {
            map = new GameMap(mapPathTable.get(mapType));
            ((GameMap)map).setMapType(mapType);
            mapTable.put(mapType, map);
        }
        return map;
    }

    public static void clearCache() {
        for (Map map : mapTable.values()) {
            map.dispose();
        }
        mapTable.clear();
    }
}
