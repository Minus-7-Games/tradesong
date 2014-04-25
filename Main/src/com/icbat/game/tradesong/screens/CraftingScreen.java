package com.icbat.game.tradesong.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.gameObjects.Workshop;
import com.icbat.game.tradesong.gameObjects.craftingStations.BaseCraftingStation;
import com.icbat.game.tradesong.screens.components.ColorOnHoverListener;

import java.util.List;

public class CraftingScreen extends BaseInGameScreen {

    private final Stage craftingStage;
    private final DragAndDrop dragAndDrop = new DragAndDrop();

    public CraftingScreen() {
        craftingStage = new Stage();
        setupStages(craftingStage);
        forceReload();
    }

    public void forceReload() {
        craftingStage.clear();
        craftingStage.addActor(new HolderTable());
    }

    @Override
    protected void doRenderWork() {
        drawBgColor(0.2f, 0.2f, 0.8f, 1);
    }

    @Override
    public String getScreenName() {
        return "craftingScreen";
    }

    private class HolderTable extends Table {
        private HolderTable() {
            super(Tradesong.uiStyles);
            this.setFillParent(true);
            this.add("Worskhop List").colspan(2).row();
            StationListing currentWorkshop = new StationListing("Current Setup", new Workshop().getOrderedNodes(), true);
            StationListing possibleStations = new StationListing("Available Stations", Tradesong.craftingStations.getNodesCopy(), false);
            currentWorkshop.linkTo(possibleStations, true);
            possibleStations.linkTo(currentWorkshop, false);

            this.add(currentWorkshop).align(Align.left + Align.top).pad(5);
            this.add(possibleStations).align(Align.left + Align.top).pad(5);
            this.pad(20);
        }
    }

    private class StationListing extends Table {
        private StationListing(String name, List<BaseCraftingStation> stations, boolean isRearrangeable) {
            super(Tradesong.uiStyles);
            this.add(name).colspan(3).row();
            for (BaseCraftingStation station : stations) {
                Actor actor = station.getActor();
                if (isRearrangeable) {
                    Label up = new Label("Up", Tradesong.uiStyles);
                    Label down = new Label("Down", Tradesong.uiStyles);

                    up.addListener(new ColorOnHoverListener(up, Color.PINK));
                    down.addListener(new ColorOnHoverListener(down, Color.PINK));

                    this.add(up).space(3);
                    this.add(down).space(3);
                }
                this.add(actor).align(Align.left).space(10).prefHeight(64).prefWidth(200).row();
            }
        }

        public void linkTo(StationListing otherStations, boolean shouldRemoveWhenAdding) {
//            for(Actor actor : this.getChildren()) {
//                actor.addListener(new ColorOnHoverListener(actor, Color.PINK));
//            }
        }
    }
}
