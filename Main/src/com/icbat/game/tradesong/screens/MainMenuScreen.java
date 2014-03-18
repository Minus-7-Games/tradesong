package com.icbat.game.tradesong.screens;

import com.icbat.game.tradesong.screens.stages.MainMenuStage;

public class MainMenuScreen extends AbstractScreen {
    public MainMenuScreen() {
        stages.add(new MainMenuStage());
    }

    @Override
    public String getScreenName() {
        return "mainMenuScreen";
    }
}
