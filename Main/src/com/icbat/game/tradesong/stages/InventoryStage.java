package com.icbat.game.tradesong.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icbat.game.tradesong.Inventory;
import com.icbat.game.tradesong.Item;
import com.icbat.game.tradesong.StackedItem;
import com.icbat.game.tradesong.Tradesong;

import static com.badlogic.gdx.math.MathUtils.floor;

public class InventoryStage extends Stage {

    public static final int ICON_SIZE = 34;
    public static final int SLOT_SIZE = 32;
    public static final int SLOT_SPACING = 8;
    public static final int COLUMNS_PER_ROW = 5;

    private BitmapFont font = new BitmapFont();
    private final Texture frameTexture;
    private Group frames = new Group();
    private Group items = new Group();
    private Group itemCounts = new Group();
    private Inventory inventory;
    private WorkshopStage linkedWorkshop = null;

    public InventoryStage(Tradesong gameInstance) {
        inventory = gameInstance.gameState.getInventory();

        frameTexture = gameInstance.assets.get(Tradesong.getFramePath());

        this.addActor(frames);
        this.addActor(items);
        this.addActor(itemCounts);

        update();
    }

    public void update() {
        // Clear out
        frames.clearChildren();
        items.clearChildren();
        itemCounts.clearChildren();

        // Add frames
        for (int i = 0; i < inventory.capacity(); ++i) {
            addSlotFrame(i);
            // add items with counts
            if (i < inventory.size()) {
                addStackedItemToStage(i);
                addItemCount(i);
            }
        }

        if (linkedWorkshop != null) {
            connectItemsToWorkshop();
        }
    }

    private void addSlotFrame(Integer i) {
        Image frameActor = makeSlotFrame();
        int[] position = positionToCoords(i);

        frameActor.setBounds(position[0],position[1], ICON_SIZE, ICON_SIZE);
        frameActor.setName(i.toString());
        frames.addActor(frameActor);

    }

    private Image makeSlotFrame() {
        Image frameActor = new Image( new TextureRegion(frameTexture));
        frameActor.setVisible(true);
        frameActor.setTouchable(Touchable.disabled);
        return frameActor;
    }

    private void addStackedItemToStage(Integer i) {

        Item item = inventory.getStack(i).getBaseItem();
        int[] position = positionToCoords(i);

        item.setBounds(position[0],position[1], ICON_SIZE, ICON_SIZE);
        item.setVisible(true);
        item.setTouchable(Touchable.enabled);
        item.setName(i.toString());

        items.addActor(item);
    }

    private void addItemCount(Integer i) {
        Integer stackSize = inventory.getStack(i).getCount();
        int[] position = positionToCoords(i);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        TextButton text = new TextButton(stackSize.toString(), buttonStyle);
        text.setTouchable(Touchable.disabled);
        text.setVisible(true);
        text.setBounds(position[0], position[1], ICON_SIZE, ICON_SIZE);
        text.setName(i.toString());

        itemCounts.addActor(text);


    }

    /** Utility to take a list slot and take it to a 2d coordinate */
    private int[] positionToCoords(int position) {
        //boundary math
        int x;
        int y;

        int totalSlotSize = SLOT_SIZE + SLOT_SPACING;

        // Figure out how many cells are in each row/col
        x = position % COLUMNS_PER_ROW; // 5 columns
        y = floor(position / COLUMNS_PER_ROW);

        // Scale the Column and Row up by the png dimensions + a spacer
        x *= (totalSlotSize);
        y *= (totalSlotSize);

        // Start at the middle of the screen
        x += floor(this.getWidth()/2);
        y += floor(this.getHeight()/2);

        x -= (totalSlotSize * COLUMNS_PER_ROW / 2);
        //TODO center on y-axis

        int[] out = new int[2];
        out[0] = x;
        out[1] = y;

        return out;
    }

    // TODO find a cleaner way to do this
    public void connectItemsToWorkshop() {
        Item item;
        for (Actor actor : items.getChildren()) {
            item = (Item)actor;

            StackedItem stack = inventory.getStack( new Integer(item.getName()) );

            item.addListener(new InventoryToWorkshopClickListener(stack, item));



        }

    }

    public void setLinkedWorkshop(WorkshopStage target) {
        linkedWorkshop = target;
    }

    private class InventoryToWorkshopClickListener extends ClickListener {
        StackedItem stack;
        private Item item;

        InventoryToWorkshopClickListener(StackedItem stack, Item item) {
            this.stack = stack;
            this.item = item;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);

            // Was there space in the workshop?
            if (linkedWorkshop.addIngredient( new Item(stack.getBaseItem()) )) {
                // Can we remove it? (I'd hope so...)
                if (stack.remove()) {
                    // Was that the last one?
                    String i = item.getName();
                    if (stack.getCount() == 0) {
                        items.removeActor(items.findActor(i));
                        itemCounts.removeActor(itemCounts.findActor(i));
                        inventory.remove(stack);

                    }
                    else {
                        // Update the count
                        ((TextButton)itemCounts.findActor(i)).setText(Integer.toString(stack.getCount()));

                    }
                }

            }


            return true;
        }
    }
}
