package com.avoid.system;

import com.avoid.util.ViewportUtils;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GridRenderSystem extends EntitySystem {

    private static final Logger log = new Logger(GridRenderSystem.class.getSimpleName(), Logger.DEBUG);

    private final Viewport viewport;
    private final ShapeRenderer renderer;

    public GridRenderSystem(Viewport viewport, ShapeRenderer renderer) {
        this.viewport = viewport;
        this.renderer = renderer;
    }

    @Override
    public void update(float deltaTime) {
        viewport.apply();
        ViewportUtils.drawGrid(viewport, renderer);
    }
}
