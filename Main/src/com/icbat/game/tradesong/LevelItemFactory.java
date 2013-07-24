package com.icbat.game.tradesong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icbat.game.tradesong.screens.LevelScreen;

import java.util.Random;

public class LevelItemFactory {
    TiledMap parent;
    Random rand = new Random();

    private Texture itemsTexture;
    private int itemSize = 34;
    private String[] spawnableItems = null;


    /** Handles the common functionality of the other constructors */
    public LevelItemFactory(TiledMap parent, Tradesong gameInstance) {
        // Independent vars
        this.parent = parent;
        String itemSpriteFilename = "sprites/items.png";
        gameInstance.assets.load(itemSpriteFilename, Texture.class); // TODO Does this want timing? Or just overload the manager class...?
        gameInstance.assets.finishLoading();
        this.itemsTexture = gameInstance.assets.get(itemSpriteFilename);

        // Dependent vars
        /* Keywords/static parameters */
        String itemKey = "spawnable_items";
        String items = (String)parent.getProperties().get(itemKey);
        this.spawnableItems = items.split(",");

    }

    /** Makes the a random item that is possible on this map     * */
    private Item createRandomItem() {
        int i = rand.nextInt(spawnableItems.length);
        String name, description;
        int x, y;
        name = spawnableItems[i];

        // TODO  hard coding this for now. Extract out later. This is good enough for alpha
        switch(i) {
            case 2:
                description = "A clump of Blackberries. But where's the bush?";
                x = 0;
                y = 0;
                break;
            case 0:
                description = "Some rock with little glinting bits.";
                x=0;
                y=17;
                break;
            case 1:
                description = "It's a log!";
                x=6;
                y=18;
                break;
            default:
                description = "What a strange object!";
                x=0;
                y=0;
        }

        TextureRegion region = new TextureRegion(itemsTexture, x * itemSize, y * itemSize, itemSize, itemSize);

        return new Item(name, description, region);
    }


    /** Adds a random item and updates the count to the parent screen */
    public Item makeItem() {
        return makeItem(createRandomItem());
    }


    /** Puts a specific item on the map and updates the count */
    public Item makeItem(Item item) {

        // TODO is there an easy way to do this in a transaction? Does it need to be?
        Random r = new Random();

        // Scale up by tile size (32x32) to the width and height for coordinates
        int x = 32 * r.nextInt((Integer)parent.getProperties().get("width"));
        int y = 32 * r.nextInt((Integer) parent.getProperties().get("height"));

        item.setTouchable(Touchable.enabled);
        item.addListener(new ItemClickListener(item));
        item.setVisible(true);

        item.setBounds(x, y, itemSize, itemSize);

        return item;


    }



    /** Class to handle touching/clicking of items on levels.  */
    class ItemClickListener extends ClickListener {
        Item owner;

        ItemClickListener(Item owner) {
            this.owner = owner;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            // TODO send to inventory
            owner.remove();
            //game.log.debug("Picked up " + owner.getItemName());
        }
    }
}
