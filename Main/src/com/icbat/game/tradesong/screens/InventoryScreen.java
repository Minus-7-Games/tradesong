package com.icbat.game.tradesong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.screens.components.ItemBox;

/***/
public class InventoryScreen extends BaseInGameScreen {

    protected final Table layout;

    public InventoryScreen() {
        Stage inventoryStage = new Stage();
        layout = new Table(Tradesong.uiStyles);

        layout.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        layout.add(ItemBox.makeInventoryBox()).row();

        inventoryStage.addActor(layout);
        setupStages(inventoryStage);
    }

    @Override
    protected void doRenderWork() {
        drawBgColor(0, 0.3f, 0, 1);
    }

    @Override
    public String getScreenName() {
        return "inventoryScreen";
    }
}
