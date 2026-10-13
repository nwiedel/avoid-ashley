package com.avoid.screen.game;

import com.avoid.AvoidGame;
import com.avoid.common.EntityFactory;
import com.avoid.config.GameConfig;
import com.avoid.screen.menu.MenuScreen;
import com.avoid.system.DebugCameraSystem;
import com.avoid.system.GridRenderSystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

    private final AvoidGame game;

    private AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private PooledEngine engine;
    private EntityFactory factory;

    public GameScreen(AvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        engine = new PooledEngine();
        factory = new EntityFactory(engine);

        engine.addSystem(new GridRenderSystem(viewport, renderer));
        engine.addSystem(new DebugCameraSystem(camera, GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y));

        factory.addPlayer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameConfig.CORNFLOWER_BLUE);

        engine.update(delta);

        log.debug("Eintity Size: " + engine.getEntities().size());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
