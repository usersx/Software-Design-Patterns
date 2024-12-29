package com.gdx.game.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gdx.game.component.Component;
import com.gdx.game.component.ComponentObserver;
import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.effect.DamageModifierEffect;
import com.gdx.game.effect.EffectPriority;
import com.gdx.game.effect.EntityEffect;
import com.gdx.game.effect.PriorityEffectManager;
import com.gdx.game.event.EntityEventManager;
import com.gdx.game.map.GameMap;
import com.gdx.game.map.MapManager;
import com.gdx.game.profile.ProfileManager;
import com.gdx.game.state.EntityState;
import com.gdx.game.state.IdleState;
import com.gdx.game.state.ImmobileState;
import com.gdx.game.state.WalkingState;

public class Entity {

	public enum Direction {
		UP,
		RIGHT,
		DOWN,
		LEFT;

		static public Direction getRandomNext() {
			return Direction.values()[MathUtils.random(Direction.values().length - 1)];
		}
	}

	public enum State {
		IDLE,
		WALKING,
		IMMOBILE;//This should always be last

		static public State getRandomNext() {
			//Ignore IMMOBILE which should be last state
			return State.values()[MathUtils.random(State.values().length - 2)];
		}
	}

	public enum AnimationType {
		WALK_LEFT,
		WALK_RIGHT,
		WALK_UP,
		WALK_DOWN,
		IDLE,
		IMMOBILE,
		LOOK_RIGHT
	}

	public static final int FRAME_WIDTH = 16;
	public static final int FRAME_HEIGHT = 16;
	private static final int MAX_COMPONENTS = 5;

	private Json json;
	private EntityConfig entityConfig;
	private Array<Component> components;
	private InputComponent inputComponent;
	private GraphicsComponent graphicsComponent;
	private PhysicsComponent physicsComponent;

	private State currentState = State.IDLE;
	private boolean isInCombat = false;
	private boolean isMoving = false;
	private EntityState entityState;
	private EntityEventManager eventManager;
	private PriorityEffectManager effectManager;

	private float health = 100f;
	private final float baseSpeed = 5f;
	private float currentSpeed;
	private final HashMap<String, Boolean> visualEffects = new HashMap<>();

	// 攻击相关配置
	private float attackRange = 2.0f;     // 攻击范围
	private float attackAngle = 90f;      // 攻击角度范围(度)
	private float attackDamage = 10f;     // 基础攻击伤害

	private GameMap currentMap;

	public Entity(Entity entity) {
		set(entity);
		eventManager = new EntityEventManager();
		effectManager = new PriorityEffectManager(this);
	}

	private void set(Entity entity) {
		inputComponent = entity.inputComponent;
		graphicsComponent = entity.graphicsComponent;
		physicsComponent = entity.physicsComponent;

		if (components == null) {
			components = new Array<>(MAX_COMPONENTS);
		}
		components.clear();
		components.add(inputComponent);
		components.add(physicsComponent);
		components.add(graphicsComponent);

		json = entity.json;
		entityConfig = new EntityConfig(entity.entityConfig);
	}

	public Entity(InputComponent inputCpnt, PhysicsComponent physicsCpnt, GraphicsComponent graphicsCpnt) {
		entityConfig = new EntityConfig();
		json = new Json();

		components = new Array<>(MAX_COMPONENTS);

		inputComponent = inputCpnt;
		physicsComponent = physicsCpnt;
		graphicsComponent = graphicsCpnt;

		components.add(inputComponent);
		components.add(physicsCpnt);
		components.add(graphicsCpnt);
	}

	public EntityConfig getEntityConfig() {
		return entityConfig;
	}

	public void sendMessage(Component.MESSAGE messageType, String ... args) {
		StringBuilder fullMessage = new StringBuilder(messageType.toString());

		for(String string : args) {
			fullMessage.append(Component.MESSAGE_TOKEN).append(string);
		}

		for(Component component: components) {
			component.receiveMessage(fullMessage.toString());
		}
	}

	public void registerObserver(ComponentObserver observer) {
		inputComponent.addObserver(observer);
		physicsComponent.addObserver(observer);
		graphicsComponent.addObserver(observer);
	}

	public void unregisterObservers() {
		inputComponent.removeAllObservers();
		physicsComponent.removeAllObservers();
		graphicsComponent.removeAllObservers();
	}

	public void update(MapManager mapMgr, Batch batch, float delta) {
		inputComponent.update(this, delta);
		physicsComponent.update(this, mapMgr, delta);
		graphicsComponent.update(this, mapMgr, batch, delta);
		effectManager.update(delta);
	}

	public void updateInput(float delta) {
		inputComponent.update(this, delta);
	}

	public void dispose() {
		for(Component component: components) {
			component.dispose();
		}
	}

	public Rectangle getCurrentBoundingBox() {
		return physicsComponent.boundingBox;
	}

	public EntityFactory.EntityName getEntityEncounteredType() {
		return physicsComponent.entityEncounteredType;
	}

	public void setEntityEncounteredType(EntityFactory.EntityName entityName) {
		this.physicsComponent.entityEncounteredType = entityName;
	}

	public Vector2 getCurrentPosition() {
		return graphicsComponent.getCurrentPosition();
	}

	public InputProcessor getInputProcessor() {
		return inputComponent;
	}

	public void setEntityConfig(EntityConfig entityConfig) {
		this.entityConfig = entityConfig;
	}

	public Animation<TextureRegion> getAnimation(AnimationType type) {
		return graphicsComponent.getAnimation(type);
	}

	public static EntityConfig getEntityConfig(String configFilePath) {
		Json json = new Json();
		return json.fromJson(EntityConfig.class, Gdx.files.internal(configFilePath));
	}

	@SuppressWarnings("unchecked")
	public static Array<EntityConfig> getEntityConfigs(String configFilePath) {
		Json json = new Json();
		Array<EntityConfig> configs = new Array<>();

		ArrayList<JsonValue> list = json.fromJson(ArrayList.class, Gdx.files.internal(configFilePath));

		for(JsonValue jsonVal : list) {
			configs.add(json.readValue(EntityConfig.class, jsonVal));
		}

		return configs;
	}

	public static EntityConfig loadEntityConfigByPath(String entityConfigPath) {
		EntityConfig entityConfig = Entity.getEntityConfig(entityConfigPath);
		return Objects.requireNonNullElse(
			ProfileManager.getInstance().getProperty(entityConfig.getEntityID(), EntityConfig.class),
			entityConfig
		);
	}

	public static Entity initEntity(EntityConfig entityConfig, Vector2 position) {
		Json json = new Json();
		Entity entity = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
		entity.setEntityConfig(entityConfig);

		entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(entity.getEntityConfig()));
		entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(position));
		entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
		entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));

		return entity;
	}

	public void setState(State newState) {
		if (this.currentState != newState) {
			State oldState = this.currentState;
			this.currentState = newState;
			eventManager.dispatchEvent("stateChange", this, oldState, newState);
			switch (newState) {
				case IDLE -> setEntityState(new IdleState());
				case WALKING -> setEntityState(new WalkingState());
				case IMMOBILE -> setEntityState(new ImmobileState());
			}
		}
	}

	private void setEntityState(EntityState newState) {
		if (entityState != null) {
			entityState.exit(this);
		}
		entityState = newState;
		if (entityState != null) {
			entityState.enter(this);
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	public boolean isInCombat() {
		return isInCombat;
	}

	public void setInCombat(boolean inCombat) {
		this.isInCombat = inCombat;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean moving) {
		this.isMoving = moving;
	}

	public boolean canAttack() {
		return !isMoving && currentState != State.IMMOBILE;
	}

	public void attack() {
		if (!canAttack()) {
			return;
		}

		// 触发攻击动画
		if (graphicsComponent != null) {
			graphicsComponent.triggerAnimation("attack");
		}

		// 计算伤害
		float finalDamage = calculateDamage(attackDamage);

		// 获取攻击范围内的目标
		Entity target = findAttackTarget();
		if (target != null) {
			target.damage(finalDamage);
		}

		// 更新状态
		setInCombat(true);
		eventManager.dispatchEvent("attack", this, finalDamage);
	}

	public EntityEventManager getEventManager() {
		return eventManager;
	}

	public void damage(float amount) {
		health -= amount;
		if (health < 0) {
			health = 0;
		}
		eventManager.dispatchEvent("healthChanged", this, health);
	}

	public void addVisualEffect(String effectName) {
		visualEffects.put(effectName, true);
		eventManager.dispatchEvent("visualEffectAdded", this, effectName);
	}

	public void removeVisualEffect(String effectName) {
		visualEffects.remove(effectName);
		eventManager.dispatchEvent("visualEffectRemoved", this, effectName);
	}

	/**
	 * 计算最终伤害值
	 * @param baseDamage 基础伤害值
	 * @return 计算后的最终伤害值
	 */
	private float calculateDamage(float baseDamage) {
		float damageMultiplier = 1.0f;
		
		// 检查是否有增伤效果
		for (EntityEffect effect : effectManager.getActiveEffects()) {
			if (effect instanceof DamageModifierEffect) {
				damageMultiplier *= ((DamageModifierEffect) effect).getDamageMultiplier();
			}
		}
		
		return baseDamage * damageMultiplier;
	}

	/**
	 * 检查目标是否在攻击角度内
	 */
	private boolean isInAttackAngle(Entity target) {
		if (target == null) return false;

		// 获取当前朝向
		Vector2 facing = getFacingDirection();
		if (facing == null) return false;

		// 计算到目标的向量
		Vector2 toTarget = new Vector2(
			target.getCurrentPosition().x - getCurrentPosition().x,
			target.getCurrentPosition().y - getCurrentPosition().y
		);

		// 计算角度
		float angle = Math.abs(facing.angleDeg(toTarget));
		return angle <= attackAngle / 2;
	}

	/**
	 * 获取当前朝向的向量
	 */
	private Vector2 getFacingDirection() {
		return switch (entityConfig.getDirection()) {
			case UP -> new Vector2(0, 1);
			case DOWN -> new Vector2(0, -1);
			case LEFT -> new Vector2(-1, 0);
			case RIGHT -> new Vector2(1, 0);
		};
	}

	/**
	 * 寻找攻击范围内的目标
	 */
	private Entity findAttackTarget() {
		Vector2 currentPos = getCurrentPosition();
		
		// 获取当前选中的实体作为目标
		Entity target = null;
		
		// 如果自己是玩家，目标就是当前选中的敌人
		if (this.getEntityConfig().getEntityID().equals("PLAYER")) {
			target = MapManager.getInstance().getCurrentSelectedEntity();
		}
		// 如果自己是敌人，目标就是玩家
		else if (this.getEntityConfig().getEntityStatus().equals("FOE")) {
			target = MapManager.getInstance().getPlayer();
		}
		
		// 检查目标是否有效
		if (target != null) {
			float distance = currentPos.dst(target.getCurrentPosition());
			// 检查距离、角度和敌对关系
			if (distance <= attackRange && isInAttackAngle(target) && isEnemy(target)) {
				// 检查是否有障碍物阻挡
				if (!hasObstacleBetween(target)) {
					return target;
				}
			}
		}
		
		return null;
	}

	private boolean hasObstacleBetween(Entity target) {
		if (target == null) return true;

		Vector2 start = getCurrentPosition();
		Vector2 end = target.getCurrentPosition();
		
		// 获取地图管理器
		MapManager mapMgr = MapManager.getInstance();
		if (mapMgr == null) return true;

		// 获取碰撞层
		MapLayer collisionLayer = mapMgr.getCollisionLayer();
		if (collisionLayer == null) return false;

		// 使用射线检测是否有障碍物
		RaycastResult result = new RaycastResult();
		return raycast(start, end, result);
	}

	public boolean raycast(Vector2 start, Vector2 end, RaycastResult result) {
		if (currentMap == null) {
			return false;
		}

		// 获取碰撞层
		MapLayer collisionLayer = currentMap.getCollisionLayer();
		if (collisionLayer == null) {
			return false;
		}
		
		// 获取射线方向
		Vector2 direction = new Vector2(end.x - start.x, end.y - start.y);
		float distance = direction.len();
		direction.nor();
		
		// 步进检测
		float step = 1f; // 1个单位的步长
		Vector2 currentPoint = new Vector2(start);
		
		for (float currentDistance = 0; currentDistance <= distance; currentDistance += step) {
			// 检查当前点是否有碰撞
			if (checkCollisionAt(currentPoint)) {
				if (result != null) {
					result.point.set(currentPoint);
					result.normal.set(-direction.x, -direction.y);
					result.distance = currentDistance;
				}
				return true;
			}
			
			// 移动到下一个点
			currentPoint.add(direction.x * step, direction.y * step);
		}
		
		return false;
	}

	private boolean checkCollisionAt(Vector2 point) {
		if (currentMap == null) {
			return false;
		}
		
		MapLayer collisionLayer = currentMap.getCollisionLayer();
		if (collisionLayer == null) {
			return false;
		}
		
		// 转换为瓦片坐标
		int tileX = (int) (point.x / currentMap.getTileWidth());
		int tileY = (int) (point.y / currentMap.getTileHeight());
		
		// 获取瓦片
		TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) collisionLayer).getCell(tileX, tileY);
		
		// 检查是否有碰撞
		return cell != null && cell.getTile() != null;
	}

	public static class RaycastResult {
		public final Vector2 point = new Vector2();
		public final Vector2 normal = new Vector2();
		public float distance;
	}

	public GameMap getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(GameMap map) {
		this.currentMap = map;
	}

	/**
	 * 判断是否是敌对关系
	 */
	private boolean isEnemy(Entity other) {
		// 如果目标是玩家，而自己是敌人，则为敌对
		if (other.getEntityConfig().getEntityID().equals("PLAYER") && 
			this.getEntityConfig().getEntityStatus().equals("FOE")) {
			return true;
		}
		
		// 如果自己是玩家，而目标是敌人，则为敌对
		if (this.getEntityConfig().getEntityID().equals("PLAYER") && 
			other.getEntityConfig().getEntityStatus().equals("FOE")) {
			return true;
		}
		
		return false;
	}
}
