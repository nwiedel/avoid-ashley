package com.avoid.screen.menu;

import com.avoid.AvoidGame;
import com.avoid.assets.AssetDescriptors;
import com.avoid.assets.RegionNames;
import com.avoid.common.GameManager;
import com.avoid.config.DifficultyLevel;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;

public class OptionsScreen extends MenuScreenBase {

    private static final Logger log = new Logger(OptionsScreen.class.getName(), Logger.DEBUG);

    private ButtonGroup<CheckBox> checkBoxGroup;
    private CheckBox easy;
    private CheckBox medium;
    private CheckBox hard;

    public OptionsScreen(AvoidGame game) {
        super(game);
    }

    @Override
    protected Actor createUI(){
        Table table = new Table();
        table.defaults().pad(15);

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        Skin uiSkin =assetManager.get(AssetDescriptors.UI_SKIN);

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        // Label
        Label label= new Label("DIFFICULTY", uiSkin);

        // Level Buttons
        easy = checkBox(DifficultyLevel.EASY.name(), uiSkin);
        // easy.setDebug(true);
        medium = checkBox(DifficultyLevel.MEDIUM.name(), uiSkin);
        hard = checkBox(DifficultyLevel.HARD.name(), uiSkin);

        checkBoxGroup = new ButtonGroup<>(easy, medium, hard);

        DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
        checkBoxGroup.setChecked(difficultyLevel.name());

        // Back Button
        TextButton backButton = new TextButton("BACK", uiSkin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                back();
            }
        });

        // Listener für Checkboxen
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                difficultyChanged();
            }
        };
        easy.addListener(listener);
        medium.addListener(listener);
        hard.addListener(listener);

        // setup Table
        Table contentTable = new Table(uiSkin);
        contentTable.defaults().pad(10);
        contentTable.setBackground(RegionNames.PANEL);

        contentTable.add(label).row();
        contentTable.add(easy).row();
        contentTable.add(medium).row();
        contentTable.add(hard).row();
        contentTable.add(backButton);

        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }

    private void back(){
        log.debug("back!");
        game.setScreen(new MenuScreen(game));
    }

    private void difficultyChanged(){
        log.debug("difficultyChanged()!");
        CheckBox checked = checkBoxGroup.getChecked();

        if (checked == easy){
            log.debug("easy");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.EASY);
        }
        else if(checked == medium){
            log.debug("medium");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.MEDIUM);
        }
        else if (checked == hard){
            log.debug("hard");
            GameManager.INSTANCE.updateDifficultyLevel(DifficultyLevel.HARD);
        }
    }

    private static CheckBox checkBox(String text, Skin skin){
        CheckBox checkBox = new CheckBox(text, skin);
        checkBox.left().pad(8);
        checkBox.getLabelCell().pad(20);
        return checkBox;
    }
}
