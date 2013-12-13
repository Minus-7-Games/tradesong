package com.icbat.game.tradesong.screens.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.assetReferences.TextureAssets;
import com.icbat.game.tradesong.screens.InventoryScreen;
import com.icbat.game.tradesong.screens.listeners.GoToScreenListener;
import com.icbat.game.tradesong.utils.SpacingActor;

/**
 * Common heads-up display for play.
 * */
public class HUD extends BaseStage {

    public static final int SPRITE_DIMENSION = 32;
    private Texture items = Tradesong.getTexture(TextureAssets.ITEMS);;

    public HUD() {
        layout();
    }

    private void setupHolderAndTable(Group holder, Table tableLayout) {
        holder.setHeight(40);
        holder.setWidth(Gdx.graphics.getWidth()); // Won't do much on resize

        tableLayout.setFillParent(true);
        tableLayout.setBackground(new TextureRegionDrawable(new TextureRegion(Tradesong.getTexture(TextureAssets.HUD_BG))));
        tableLayout.align(Align.bottom);
        tableLayout.setColor(0f, 0f, 0f, 0.5f);
    }

    private Image inventoryButton() {
        TextureRegion region = new TextureRegion(items, 7 * SPRITE_DIMENSION, 29 * SPRITE_DIMENSION, SPRITE_DIMENSION, SPRITE_DIMENSION);
        Image inventoryButton = new Image(region);
        inventoryButton.setTouchable(Touchable.enabled);
        inventoryButton.addListener(new GoToScreenListener() {
            @Override
            protected void goToTargetScreen() {
                Tradesong.screenManager.goToScreen(new InventoryScreen());
            }
        });
        return inventoryButton;
    }



    private Image menuButton() {
        TextureRegion region = new TextureRegion(items, 0, 29 * SPRITE_DIMENSION, SPRITE_DIMENSION, SPRITE_DIMENSION);
        Image menuButton = new Image(region);
        menuButton.setTouchable(Touchable.enabled);
        menuButton.addListener(new GoToScreenListener() {
            @Override
            protected void goToTargetScreen() {
                Tradesong.screenManager.goToMainMenu(); // curious if this is actually what I want.
            }
        });
        return menuButton;
    }

    private Actor craftingButton() {
        Image craftingButton = new Image();
        return craftingButton;
    }

    @Override
    public void layout() {
        this.clear();
        Group holder = new Group();
        Table tableLayout = new Table();


        tableLayout.add(menuButton());
        tableLayout.add(new SpacingActor());
        tableLayout.add(inventoryButton());
        tableLayout.add(new SpacingActor());
        tableLayout.add(craftingButton());

        setupHolderAndTable(holder, tableLayout);

        holder.addActor(tableLayout);
        this.addActor(holder);
    }



    @Override
    public void hide() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
