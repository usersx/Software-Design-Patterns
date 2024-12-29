package com.gdx.game.entities;

import com.gdx.game.component.GraphicsComponent;
import com.gdx.game.component.InputComponent;
import com.gdx.game.component.PhysicsComponent;
import com.gdx.game.entities.npc.NPCGraphicsComponent;
import com.gdx.game.entities.npc.NPCInputComponent;
import com.gdx.game.entities.npc.NPCPhysicsComponent;
import com.gdx.game.entities.npc.enemy.EnemyPhysicsComponent;
import com.gdx.game.entities.player.PlayerGraphicsComponent;
import com.gdx.game.entities.player.PlayerInputComponent;
import com.gdx.game.entities.player.PlayerPhysicsComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityFactoryTest {

    @Test
    void testGetInstance() {
        EntityFactory entityFactory = EntityFactory.getInstance();
        assertNotNull(entityFactory);
        assertSame(entityFactory, EntityFactory.getInstance());
    }

    @Test
    void testGetPlayerEntity() {
        Entity entity = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.WARRIOR);
        
        assertNotNull(entity);
        assertTrue(entity.getInputComponent() instanceof PlayerInputComponent);
        assertTrue(entity.getPhysicsComponent() instanceof PlayerPhysicsComponent);
        assertTrue(entity.getGraphicsComponent() instanceof PlayerGraphicsComponent);
    }

    @Test
    void testGetNPCEntity() {
        Entity entity = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.NPC);
        
        assertNotNull(entity);
        assertTrue(entity.getInputComponent() instanceof NPCInputComponent);
        assertTrue(entity.getPhysicsComponent() instanceof NPCPhysicsComponent);
        assertTrue(entity.getGraphicsComponent() instanceof NPCGraphicsComponent);
    }

    @Test
    void testGetEnemyEntity() {
        Entity entity = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.ENEMY);
        
        assertNotNull(entity);
        assertTrue(entity.getInputComponent() instanceof NPCInputComponent);
        assertTrue(entity.getPhysicsComponent() instanceof EnemyPhysicsComponent);
        assertTrue(entity.getGraphicsComponent() instanceof NPCGraphicsComponent);
    }

    @Test
    void testGetInvalidEntityType() {
        assertThrows(IllegalArgumentException.class, () -> {
            EntityFactory.getInstance().getEntity(null);
        });
    }
}
