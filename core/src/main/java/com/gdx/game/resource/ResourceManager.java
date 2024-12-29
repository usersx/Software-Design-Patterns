package com.gdx.game.resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 资源管理器
 * 负责管理所有游戏资源的加载和卸载
 */
public class ResourceManager {
    private static final int MAX_LOADS_PER_FRAME = 1;
    private static ResourceManager instance;
    
    private Map<String, GameResource> resources;
    private Queue<String> loadingQueue;
    private Set<String> loadedResources;

    private ResourceManager() {
        resources = new HashMap<>();
        loadingQueue = new LinkedList<>();
        loadedResources = new HashSet<>();
        initializeResources();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    private void initializeResources() {
        // 只创建代理，不实际加载资源
        addResource("player", new TextureResourceProxy("textures/player.png"));
        addResource("enemy", new TextureResourceProxy("textures/enemy.png"));
        addResource("npc", new TextureResourceProxy("textures/npc.png"));
    }

    public void addResource(String id, GameResource resource) {
        resources.put(id, resource);
    }

    public void preloadResources(String... resourceIds) {
        for (String id : resourceIds) {
            if (!loadedResources.contains(id)) {
                loadingQueue.offer(id);
            }
        }
    }

    public void update() {
        int loadedThisFrame = 0;
        while (!loadingQueue.isEmpty() && loadedThisFrame < MAX_LOADS_PER_FRAME) {
            String id = loadingQueue.poll();
            GameResource resource = resources.get(id);
            if (resource != null && !resource.isLoaded()) {
                resource.load();
                loadedResources.add(id);
                loadedThisFrame++;
            }
        }
    }

    public GameResource getResource(String id) {
        return resources.get(id);
    }

    public void unloadResource(String id) {
        GameResource resource = resources.get(id);
        if (resource != null) {
            resource.unload();
            loadedResources.remove(id);
        }
    }

    public void unloadAll() {
        for (GameResource resource : resources.values()) {
            resource.unload();
        }
        loadedResources.clear();
        loadingQueue.clear();
    }
} 