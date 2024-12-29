package com.gdx.game.observer;

import com.badlogic.gdx.audio.Sound;
import com.gdx.game.effect.EntityEffect;
import com.gdx.game.entities.Entity;
import com.gdx.game.resource.ResourceManager;

/**
 * 声音管理器观察者
 * 负责播放相关音效
 */
public class SoundManager implements EntityObserver {
    private ResourceManager resourceManager;

    public SoundManager() {
        resourceManager = ResourceManager.getInstance();
    }

    @Override
    public void onHealthChanged(Entity entity, float oldHealth, float newHealth) {
        if (newHealth < oldHealth) {
            // 播放受伤音效
            playSound("hit");
        }
        if (newHealth <= 0) {
            // 播放死亡音效
            playSound("death");
        }
    }

    @Override
    public void onLevelUp(Entity entity, int oldLevel, int newLevel) {
        // 播放升级音效
        playSound("levelup");
    }

    @Override
    public void onStatusEffectChanged(Entity entity, EntityEffect effect, boolean added) {
        // 根据效果类型播放相应音效
        if (added) {
            playEffectSound(effect);
        }
    }

    private void playSound(String soundId) {
        Sound sound = (Sound) resourceManager.getResource(soundId);
        if (sound != null) {
            sound.play();
        }
    }

    private void playEffectSound(EntityEffect effect) {
        // 根据效果类型选择音效
        String soundName = getEffectSoundName(effect);
        if (soundName != null) {
            playSound(soundName);
        }
    }

    private String getEffectSoundName(EntityEffect effect) {
        if (effect instanceof PoisonEffect) {
            return "effects/poison";
        } else if (effect instanceof BurningEffect) {
            return "effects/burning";
        } else if (effect instanceof StunEffect) {
            return "effects/stun";
        } else if (effect instanceof SlowEffect) {
            return "effects/slow";
        } else if (effect instanceof BuffEffect) {
            return "effects/buff";
        }
        return null;
    }

    private void loadEffectSounds() {
        // 加载所有效果音效
        assetManager.load("effects/poison.wav", Sound.class);
        assetManager.load("effects/burning.wav", Sound.class);
        assetManager.load("effects/stun.wav", Sound.class);
        assetManager.load("effects/slow.wav", Sound.class);
        assetManager.load("effects/buff.wav", Sound.class);
        
        // 等待加载完成
        assetManager.finishLoading();
    }
} 