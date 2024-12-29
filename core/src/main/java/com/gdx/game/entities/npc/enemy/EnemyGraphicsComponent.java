package com.gdx.game.entities.npc.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.entities.Entity;
import com.gdx.game.entities.EntityConfig;
import com.gdx.game.map.MapManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.assets.AssetManager;

/**
 * 敌人图形组件
 * 负责渲染敌人的图形
 */
public class EnemyGraphicsComponent extends GraphicsComponent {
    private Animation<TextureRegion> currentAnimation;
    private float frameTime = 0f;
    private Json json;
    private AssetManager assetManager;
    private String currentAnimationType;
    private Array<Animation<TextureRegion>> animations;

    public EnemyGraphicsComponent() {
        json = new Json();
        assetManager = new AssetManager();
        animations = new Array<>();
    }

    @Override
    public void update(Entity entity, MapManager mapManager, Batch batch, float delta) {
        // 更新动画
        frameTime = (frameTime + delta) % 5;
        
        // 更新当前动画
        updateCurrentAnimation(entity);
        
        // 渲染
        if (currentAnimation != null && batch != null) {
            TextureRegion currentFrame = currentAnimation.getKeyFrame(frameTime, true);
            Vector2 position = entity.getCurrentPosition();
            batch.draw(currentFrame, position.x, position.y, 1, 1);
        }
    }

    private void updateCurrentAnimation(Entity entity) {
        // 根据实体状态更新动画
        String state = entity.getEntityConfig().getState().toString();
        
        // 根据状态设置相应的动画
        switch (state) {
            case "IDLE":
                // 设置待机动画
                break;
            case "WALKING":
                // 设置行走动画
                break;
            case "IMMOBILE":
                // 设置静止动画
                break;
            default:
                // 默认使用待机动画
                break;
        }
    }

    @Override
    public void dispose() {
        // 清理资源
        currentAnimation = null;
    }

    @Override
    public void receiveMessage(String message) {
        // 处理消息，如加载动画等
        if (message != null) {
            EntityConfig config = json.fromJson(EntityConfig.class, message);
            // TODO: 根据配置加载动画资源
        }
    }

    private void loadAnimations(EntityConfig config) {
        // 获取动画配置
        JsonValue animationConfigs = config.getAnimationConfig();
        if (animationConfigs == null) {
            return;
        }
        
        // 清除现有动画
        animations.clear();
        
        // 遍历所有动画配置
        for (JsonValue anim = animationConfigs.child; anim != null; anim = anim.next) {
            String animationType = anim.getString("type");
            float frameDuration = anim.getFloat("frameDuration", 0.1f);
            
            // 获取动画帧
            Array<TextureRegion> frames = new Array<>();
            JsonValue frameConfigs = anim.get("frames");
            for (JsonValue frame = frameConfigs.child; frame != null; frame = frame.next) {
                String framePath = frame.asString();
                TextureRegion region = assetManager.get(framePath, TextureRegion.class);
                if (region != null) {
                    frames.add(region);
                }
            }
            
            // 创建动画
            if (frames.size > 0) {
                Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
                
                // 设置播放模式
                String playMode = anim.getString("playMode", "NORMAL");
                switch (playMode.toUpperCase()) {
                    case "LOOP":
                        animation.setPlayMode(Animation.PlayMode.LOOP);
                        break;
                    case "LOOP_REVERSED":
                        animation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
                        break;
                    case "LOOP_PINGPONG":
                        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                        break;
                    default:
                        animation.setPlayMode(Animation.PlayMode.NORMAL);
                        break;
                }
                
                // 存储动画
                animations.add(animation);
            }
        }
        
        // 设置默认动画
        String defaultAnimation = config.getDefaultAnimation();
        if (defaultAnimation != null && animations.size > 0) {
            currentAnimation = animations.get(0);
            currentAnimationType = defaultAnimation;
        }
    }
} 