package com.avoid.common;

import com.avoid.component.BoundsComponent;
import com.avoid.config.GameConfig;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public class EntityFactory {

    private final PooledEngine engine;

    public EntityFactory(PooledEngine engine) {
        this.engine = engine;
    }

    public void addPlayer(){
        float x = GameConfig.WORLD_WIDTH / 2f;
        float y = GameConfig.PLAYER_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds .bounds.set(x, y, GameConfig.PLAYER_BOUNDS_RADIUS);

        Entity entity = engine.createEntity();
        engine.addEntity(entity);
    }
}
