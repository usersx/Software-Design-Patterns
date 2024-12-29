package com.gdx.game.entities.factory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

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

class EntityComponentFactoryTest {

    @Test
    void testPlayerComponentFactory() {
        EntityComponentFactory factory = new PlayerComponentFactory();
        
        InputComponent input = factory.createInputComponent();
        PhysicsComponent physics = factory.createPhysicsComponent();
        GraphicsComponent graphics = factory.createGraphicsComponent();
        
        assertTrue(input instanceof PlayerInputComponent);
        assertTrue(physics instanceof PlayerPhysicsComponent);
        assertTrue(graphics instanceof PlayerGraphicsComponent);
    }

    @Test
    void testNPCComponentFactory() {
        EntityComponentFactory factory = new NPCComponentFactory();
        
        InputComponent input = factory.createInputComponent();
        PhysicsComponent physics = factory.createPhysicsComponent();
        GraphicsComponent graphics = factory.createGraphicsComponent();
        
        assertTrue(input instanceof NPCInputComponent);
        assertTrue(physics instanceof NPCPhysicsComponent);
        assertTrue(graphics instanceof NPCGraphicsComponent);
    }

    @Test
    void testEnemyComponentFactory() {
        EntityComponentFactory factory = new EnemyComponentFactory();
        
        InputComponent input = factory.createInputComponent();
        PhysicsComponent physics = factory.createPhysicsComponent();
        GraphicsComponent graphics = factory.createGraphicsComponent();
        
        assertTrue(input instanceof NPCInputComponent);
        assertTrue(physics instanceof EnemyPhysicsComponent);
        assertTrue(graphics instanceof NPCGraphicsComponent);
    }
} 