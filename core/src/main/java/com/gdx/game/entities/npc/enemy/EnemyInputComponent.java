package com.gdx.game.entities.npc.enemy;

import com.gdx.game.component.InputComponent;
import com.gdx.game.entities.Entity;

/**
 * 敌人输入组件
 * 负责处理敌人的输入逻辑，如AI行为等
 */
public class EnemyInputComponent extends InputComponent {
    private float updateTime = 0;
    private static final float UPDATE_RATE = 1.0f;

    @Override
    public void update(Entity entity, float delta) {
        // 更新AI逻辑
        updateTime += delta;
        if (updateTime >= UPDATE_RATE) {
            updateTime = 0;
            // 在这里实现敌人的AI行为
            // 例如：随机移动、追踪玩家等
        }
    }

    @Override
    public void dispose() {
        // 清理资源
    }

    @Override
    public void receiveMessage(String message) {
        // 处理消息
    }

    // 敌人不需要处理键盘和鼠标输入
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
} 