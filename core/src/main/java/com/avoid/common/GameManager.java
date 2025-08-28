package com.avoid.common;

import com.avoid.AvoidGame;
import com.avoid.config.DifficultyLevel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private static final String HIGH_SCORE_KEY = "highscore";
    private static final String DIFFICULTY_KEY = "difficulty";

    private Preferences PREFS;
    private int highscore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;

    private GameManager(){
        PREFS = Gdx.app.getPreferences(AvoidGame.class.getSimpleName());
        highscore = PREFS.getInteger(HIGH_SCORE_KEY, 0);
        String difficultyName = PREFS.getString(DIFFICULTY_KEY, DifficultyLevel.MEDIUM.name());
        difficultyLevel = DifficultyLevel.valueOf(difficultyName);
    }

    public void updateHighScore(int score){
        if (score < highscore){
            return;
        }
        highscore = score;
        PREFS.putInteger(HIGH_SCORE_KEY, highscore);
        PREFS.flush();
    }

    public String getHighScoreString(){
        return String.valueOf(highscore);
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void updateDifficultyLevel(DifficultyLevel newDifficultyLevel){
        if(difficultyLevel == newDifficultyLevel){
            return;
        }
        difficultyLevel = newDifficultyLevel;
        PREFS.putString(DIFFICULTY_KEY, difficultyLevel.name());
        PREFS.flush();
    }
}
