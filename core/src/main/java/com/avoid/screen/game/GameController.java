package com.avoid.screen.game;

import com.avoid.AvoidGame;
import com.avoid.assets.AssetDescriptors;
import com.avoid.common.GameManager;
import com.avoid.config.DifficultyLevel;
import com.avoid.config.GameConfig;
import com.avoid.entity.Background;
import com.avoid.entity.Obstacle;
import com.avoid.entity.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

@Deprecated
public class GameController {

    // -- Konstanten --
    private static final Logger log = new Logger(GameController.class.getSimpleName(), Logger.DEBUG);

    // -- Attribute --
    private Player player;
    private Array<Obstacle> obstacles = new Array<>();
    private Background background;
    private float obstacleTimer;private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private Pool<Obstacle> obstaclePool;
    private Sound hit;

    private final AvoidGame game;
    private final AssetManager assetManager;

    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE) / 2 ;
    private final float startPlayerY = 1 - GameConfig.PLAYER_SIZE / 2;

    // -- Konstruktoren --
    public GameController(AvoidGame game){
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }

    // -- init() --
    private void init(){
        // Spieler erstellen und positionieren
        player = new Player();

        player.setPosition(startPlayerX, startPlayerY);

        // Pool von Obstacles erstellen
        obstaclePool = Pools.get(Obstacle.class, 40);

        // Hintergrund erstellen
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        // Sound laden
        hit = assetManager.get(AssetDescriptors.HIT_SOUND);
    }

    // -------------------- öffentliche Methoden --------------------
    public void update(float delta){
        if (isGameOver()){
            return;
        }

        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerCollidingWithObstacle()){
            log.debug("Kollision erkannt!");
            lives--;

            if(isGameOver()){
                log.debug("GAME OVER!");
                GameManager.INSTANCE.updateHighScore(score);
            }else {
                restart();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public Background getBackground(){ return background; }

    public boolean isGameOver(){
        return lives <= 0;
    }

    // -------------------- private Methoden --------------------
    private void restart(){
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    private boolean isPlayerCollidingWithObstacle(){
        for (Obstacle obstacle : obstacles){
            if (obstacle.isNotHit() && obstacle.isPlayerColliding(player)){
                hit.play();
                return true;
            }
        }

        return false;
    }

    private void updatePlayer(){
        float xSpeed = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        }else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        player.setX(player.getX() + xSpeed);

        blockPlayerFromLeavingWorld();
    }

    private void blockPlayerFromLeavingWorld(){
        float playerX = MathUtils.clamp(player.getX(),
            0,
            GameConfig.WORLD_WIDTH - player.getWidth());

        player.setPosition(playerX, player.getY());
    }

    private void updateObstacles(float delta){
        for (Obstacle obstacle : obstacles){
            obstacle.update();
        }
        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = obstaclePool.obtain();
            DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);
            obstacles.add(obstacle);
            obstacleTimer = 0f;
        }
    }

    private void removePassedObstacles(){
        if (obstacles.size > 0){
            Obstacle first = obstacles.first();

            float minObstacleY = -GameConfig.OBSTACLE_SIZE;

            if(first.getY() < minObstacleY){
                obstacles.removeValue(first,true);
                obstaclePool.free(first);
            }
        }
    }

    private void updateScore(float delta){
        scoreTimer += delta;

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME){
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float delta){
        if(displayScore < score){
            displayScore = Math.min(
                score,
                displayScore + (int)(60 * delta));
        }
    }
}
