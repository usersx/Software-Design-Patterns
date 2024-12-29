package com.gdx.game.observer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gdx.game.effect.EntityEffect;
import com.gdx.game.entities.Entity;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * 玩家HUD观察者
 * 负责更新UI显示
 */
public class PlayerHUD implements EntityObserver {
    private ProgressBar healthBar;
    private Label levelLabel;
    private Table statusEffectTable;
    private Table statusEffectsContainer;
    private Skin skin;
    private java.util.Map<EntityEffect, StatusEffectUI> effectIcons = new java.util.HashMap<>();

    public PlayerHUD() {
        // 初始化UI组件
        // 这里应该由UI管理器提供具体实现
    }

    @Override
    public void onHealthChanged(Entity entity, float oldHealth, float newHealth) {
        // 更新血条
        healthBar.setValue(newHealth / 100f);
        
        // 如果受到伤害，显示红色闪烁效果
        if (newHealth < oldHealth) {
            healthBar.setColor(Color.RED);
            // TODO: 添加闪烁动画
        }
    }

    @Override
    public void onLevelUp(Entity entity, int oldLevel, int newLevel) {
        // 更新等级显示
        levelLabel.setText("Level: " + newLevel);
        // TODO: 添加升级特效
    }

    @Override
    public void onStatusEffectChanged(Entity entity, EntityEffect effect, boolean added) {
        if (added) {
            // 添加状态效果图标
            addStatusEffectIcon(effect);
        } else {
            // 移除状态效果图标
            removeStatusEffectIcon(effect);
        }
    }

    private void addBlinkAnimation(Label label) {
        // 创建闪烁动画
        Action blinkAction = Actions.sequence(
            Actions.alpha(0.3f, 0.3f),
            Actions.alpha(1f, 0.3f)
        );
        
        // 重复执行闪烁动画
        label.addAction(Actions.forever(blinkAction));
    }

    private void addLevelUpEffect() {
        // 创建升级特效图片
        Image levelUpEffect = new Image(new Texture("effects/level_up.png"));
        levelUpEffect.setPosition(
            player.getX() + player.getWidth() / 2 - levelUpEffect.getWidth() / 2,
            player.getY() + player.getHeight()
        );
        
        // 添加特效动画
        levelUpEffect.addAction(
            Actions.sequence(
                Actions.parallel(
                    Actions.moveBy(0, 50, 1.5f),
                    Actions.sequence(
                        Actions.alpha(1, 0.3f),
                        Actions.alpha(0, 1.2f)
                    )
                ),
                Actions.removeActor()
            )
        );
        
        // 添加到舞台
        getStage().addActor(levelUpEffect);
        
        // 播放升级音效
        AudioManager.getInstance().playSound("level_up");
    }

    private void addStatusEffectIcon(EntityEffect effect) {
        // 创建效果图标
        Image effectIcon = new Image(getEffectIcon(effect));
        effectIcon.setSize(32, 32);
        
        // 设置图标位置
        float x = statusEffectsContainer.getX() + statusEffectsContainer.getChildren().size * 40;
        float y = statusEffectsContainer.getY();
        effectIcon.setPosition(x, y);
        
        // 添加持续时间标签
        Label durationLabel = new Label(
            String.format("%.1f", effect.getRemainingTime()),
            skin
        );
        durationLabel.setPosition(
            effectIcon.getX() + effectIcon.getWidth() / 2 - durationLabel.getWidth() / 2,
            effectIcon.getY() - durationLabel.getHeight()
        );
        
        // 添加到容器
        statusEffectsContainer.addActor(effectIcon);
        statusEffectsContainer.addActor(durationLabel);
        
        // 存储引用
        effectIcons.put(effect, new StatusEffectUI(effectIcon, durationLabel));
    }

    private void removeStatusEffectIcon(EntityEffect effect) {
        StatusEffectUI ui = effectIcons.get(effect);
        if (ui != null) {
            // 添加消失动画
            ui.icon.addAction(
                Actions.sequence(
                    Actions.parallel(
                        Actions.alpha(0, 0.3f),
                        Actions.scaleBy(-0.3f, -0.3f, 0.3f)
                    ),
                    Actions.removeActor()
                )
            );
            
            ui.durationLabel.addAction(
                Actions.sequence(
                    Actions.alpha(0, 0.3f),
                    Actions.removeActor()
                )
            );
            
            // 移除引用
            effectIcons.remove(effect);
        }
    }

    private Texture getEffectIcon(EntityEffect effect) {
        // 根据效果类型返回对应图标
        String iconPath = "effects/";
        if (effect instanceof PoisonEffect) {
            iconPath += "poison.png";
        } else if (effect instanceof BurningEffect) {
            iconPath += "burning.png";
        } else if (effect instanceof StunEffect) {
            iconPath += "stun.png";
        } else if (effect instanceof SlowEffect) {
            iconPath += "slow.png";
        } else if (effect instanceof BuffEffect) {
            iconPath += "buff.png";
        } else {
            iconPath += "default.png";
        }
        return new Texture(iconPath);
    }

    private static class StatusEffectUI {
        final Image icon;
        final Label durationLabel;
        
        StatusEffectUI(Image icon, Label durationLabel) {
            this.icon = icon;
            this.durationLabel = durationLabel;
        }
    }
} 