package com.icbat.game.tradesong.screens.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.icbat.game.tradesong.Item;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.assetReferences.TextureAssets;
import com.icbat.game.tradesong.screens.listeners.InventoryClickListener;
import com.icbat.game.tradesong.utils.SpacingActor;

import java.util.List;

/**
 * Basic, modular piece of just the inventory. Consider description area here or on its own stage (probably here)
 */
public class InventoryStage extends BaseStage {
    private Label description = new Label("", Tradesong.uiStyles.getLabelStyle());
    private Label itemName = new Label("", Tradesong.uiStyles.getLabelStyle());

    @Override
    public void layout() {
        super.layout();

        this.clear();

        Table holdingTable = makeHoldingTable();
        holdingTable.top();
        holdingTable.pad(62);
        holdingTable = holdingTable.debugTable();

        holdingTable.add(makeInventoryTable());
        holdingTable.row();
        holdingTable.add(makeItemInfoTable());

        this.addActor(holdingTable);
    }

    protected Table makeHoldingTable() {
        Table layout = new Table();
        layout.setFillParent(true);
        return layout;
    }

    protected Table makeInventoryTable() {
        Table inventory = new Table();
        List<Item> inventoryCopy = Tradesong.inventory.getCopyOfInventory();

        for (int i = 1; i <= Tradesong.inventory.getMaxSize(); ++i) {
            Image frame = new Image(Tradesong.getTexture(TextureAssets.FRAME));
            if (i - 1 < inventoryCopy.size() && inventoryCopy.get(i - 1) != null) {
                Item item = inventoryCopy.get(i - 1);
                item.addListener(new InventoryClickListener(item) {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        setItemName(this.owner.getName());
                        setDescription(this.owner.getDescription());
                    }
                });
                inventory.stack(frame, item);
            } else {
                inventory.add(frame);
            }

            inventory.add(new SpacingActor());

            if (i % 6 == 0) {
                inventory.row();
                inventory.add(new SpacingActor());
                inventory.row();
            }

        }
        return inventory;
    }

    protected Table makeItemInfoTable() {
        Table itemInfoTable = new Table();
        itemName = makeItemName();
        description = makeDescription();

        itemInfoTable.stack(itemName);
        itemInfoTable.row();
        itemInfoTable.stack(description);

        return itemInfoTable;
    }

    private Label makeItemName() {
        return new Label("", Tradesong.uiStyles.getLabelStyle());
    }

    private Label makeDescription() {
        Label descriptionField = new Label("", Tradesong.uiStyles.getLabelStyle());
        descriptionField.setWrap(true);
        return descriptionField;
    }

    protected void setDescription(String newDesc) {
        description.setText(newDesc);
        Gdx.app.debug("description dimensions",description.getWidth() + ", " + description.getHeight());
    }

    protected void setItemName(String newName) {
        itemName.setText(newName);
        Gdx.app.debug("itemName dimensions", itemName.getWidth() + ", " + itemName.getHeight());
    }


}
