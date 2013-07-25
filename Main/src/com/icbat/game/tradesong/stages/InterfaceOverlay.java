package com.icbat.game.tradesong.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.icbat.game.tradesong.Tradesong;

public class InterfaceOverlay extends Stage {

    public InterfaceOverlay(Tradesong game) {

        String itemSpriteFilename = "sprites/items.png";
        addInventoryButton();

    }

    private void addInventoryButton() {
        // Add the default actors for the UI
        int x = 7;
        int y = 29;
        int dimension = 34;
        Texture texture = game.assets.get(itemSpriteFilename);


        Image inventoryButton = new Image(new TextureRegion(texture, x*dimension, y*dimension, dimension, dimension));

        this.addActor(inventoryButton);


        this.setTouchable(Touchable.enabled);
        this.setVisible(true);
    }
}