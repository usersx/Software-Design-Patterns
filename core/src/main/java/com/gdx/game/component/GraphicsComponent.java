package com.gdx.game.component;

import java.util.Hashtable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gdx.game.entities.Entity;
import com.gdx.game.manager.ResourceManager;
import com.gdx.game.map.MapManager;
import com.gdx.game.entities.Entity.AnimationType;
import java.util.HashMap;
import java.util.Map;

public abstract class GraphicsComponent extends ComponentSubject implements Component {
    protected TextureRegion currentFrame = null;
    protected float frameTime = 0f;
    protected Entity.State currentState;
    protected Entity.Direction currentDirection;
    protected Json json;
    protected Vector2 currentPosition;
    protected Hashtable<Entity.AnimationType, Animation<TextureRegion>> animations;
    protected ShapeRenderer shapeRenderer;
    private Map<AnimationType, Animation<TextureRegion>> animations;
    private Animation<TextureRegion> currentAnimation;
    private float stateTime = 0f;

    protected GraphicsComponent() {
        currentPosition = new Vector2(0,0);
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        json = new Json();
        animations = new Hashtable<>();
        shapeRenderer = new ShapeRenderer();
    }

    public Vector2 getCurrentPosition() {
        return currentPosition;
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta) {
        frameTime = (frameTime + delta)%5; //Want to avoid overflow

        //Look into the appropriate variable when changing position
        switch(currentDirection) {
            case DOWN -> {
                if (currentState == Entity.State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == Entity.State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_DOWN);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == Entity.State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                }
            }
            case LEFT -> {
                if (currentState == Entity.State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == Entity.State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_LEFT);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == Entity.State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                }
            }
            case UP -> {
                if (currentState == Entity.State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_UP);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == Entity.State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_UP);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == Entity.State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                }
            }
            case RIGHT -> {
                if (currentState == Entity.State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == Entity.State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.WALK_RIGHT);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == Entity.State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(Entity.AnimationType.IMMOBILE);
                    if (animation == null) {
                        return;
                    }
                    currentFrame = animation.getKeyFrame(frameTime);
                }
            }
            default -> {
            }
        }
    }

    //Specific to two frame animations where each frame is stored in a separate texture
    protected Animation<TextureRegion> loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration) {
        ResourceManager.loadTextureAsset(firstTexture);
        Texture texture1 = ResourceManager.getTextureAsset(firstTexture);

        ResourceManager.loadTextureAsset(secondTexture);
        Texture texture2 = ResourceManager.getTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        GridPoint2 point = points.first();

		Animation<TextureRegion> animation = new Animation<>(frameDuration, texture1Frames[point.x][point.y],texture2Frames[point.x][point.y]);
		animation.setPlayMode(Animation.PlayMode.LOOP);

        return animation;
    }

    protected Animation<TextureRegion> loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration) {
        ResourceManager.loadTextureAsset(textureName);
        Texture texture = ResourceManager.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        TextureRegion[] animationKeyFrames = new TextureRegion[points.size];

        for(int i=0; i < points.size; i++) {
			animationKeyFrames[i] = textureFrames[points.get(i).x][points.get(i).y];
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, animationKeyFrames);
		animation.setPlayMode(Animation.PlayMode.LOOP);

        return animation;
    }

    public Animation<TextureRegion> getAnimation(Entity.AnimationType type) {
        return animations.get(type);
    }

    /**
     * 触发指定的动画
     * @param animationName 动画名称
     */
    public void triggerAnimation(String animationName) {
        // 根据动画名称获取对应的动画状态
        AnimationType animationType = getAnimationTypeByName(animationName);
        if (animationType != null) {
            currentAnimation = animations.get(animationType);
            stateTime = 0f;
        }
    }

    /**
     * 根据动画名称获取动画类型
     */
    private AnimationType getAnimationTypeByName(String name) {
        switch (name.toLowerCase()) {
            case "attack":
                return AnimationType.LOOK_RIGHT; // 暂时使用LOOK_RIGHT作为攻击动画
            case "walk_left":
                return AnimationType.WALK_LEFT;
            case "walk_right":
                return AnimationType.WALK_RIGHT;
            case "walk_up":
                return AnimationType.WALK_UP;
            case "walk_down":
                return AnimationType.WALK_DOWN;
            case "idle":
                return AnimationType.IDLE;
            case "immobile":
                return AnimationType.IMMOBILE;
            default:
                return null;
        }
    }
}
