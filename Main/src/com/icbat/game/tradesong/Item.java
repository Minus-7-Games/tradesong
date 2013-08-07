package com.icbat.game.tradesong;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Basic class of all items/objects
 *
 * Abstract representation of the item. The actual "tangible" class is StackedItem.
 * @see StackedItem
 * */
public class Item extends Image {

    private String itemName;
    private String description;
    private int maxStack;
    private int rarity;
    private Texture baseTexture;
    private int spriteX;
    private int spriteY;

    public static final int ICON_SIZE = 34;

    // name, description, texture, maxStack, rarity, x, y
    public Item(String itemName, String description, Texture texture,  int maxStack, int rarity, int spriteX, int spriteY) {
        super( new TextureRegion(texture, spriteX * ICON_SIZE, spriteY * ICON_SIZE, ICON_SIZE, ICON_SIZE) );
        this.itemName = itemName;
        this.description = description;
        this.baseTexture = texture;
        this.maxStack = maxStack;
        this.rarity = rarity;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
    }

    /** Copy constructor */
    public Item(Item old) {
        this(old.getItemName(), old.getDescription(), old.getBaseTexture(), old.getMaxStack(), old.getRarity(), old.getSpriteX(), old.getSpriteY());

    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getRarity() {
        return rarity;
    }

    public Texture getBaseTexture() {
        return baseTexture;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (maxStack != item.maxStack) return false;
        if (rarity != item.rarity) return false;
        if (spriteX != item.spriteX) return false;
        if (spriteY != item.spriteY) return false;
        if (!baseTexture.equals(item.baseTexture)) return false;
        if (!description.equals(item.description)) return false;
        if (!itemName.equals(item.itemName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + maxStack;
        result = 31 * result + rarity;
        result = 31 * result + baseTexture.hashCode();
        result = 31 * result + spriteX;
        result = 31 * result + spriteY;
        return result;
    }
}
