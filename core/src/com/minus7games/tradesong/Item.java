package com.minus7games.tradesong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;

/***/
public class Item implements Comparable<Item>, Displayable {
    private final String internalName;
    private final String displayName;
    private final String description;

    public Item(String internalName, String displayName, String description) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.description = description;
    }

    public Item copy() {
        return new Item(internalName, displayName, description);
    }

    public static Item parseItem(JsonValue itemNode) {
        String internalName = itemNode.getString("internalName", "DEFAULT_ITEM_NAME");
        Gdx.app.debug("parsing item: internal name", internalName);
        String displayName = itemNode.getString("displayName", "Default. What a lazy display name.");
        Gdx.app.debug("parsing item: display name ", displayName);
        String description = itemNode.getString("description", "DEFAULT DESCRIPTION. Yell at the author to write more, please.");
        Gdx.app.debug("parsing item: description  ", description);
        return new Item(internalName, displayName, description);
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDescription() {
        return description;
    }


    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int compareTo(Item o) {
        return this.internalName.compareTo(o.getInternalName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (!internalName.equals(item.internalName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return internalName.hashCode();
    }

    @Override
    public Actor getActor() {
        Table table = new Table(GameSkin.get());
        table.add(new Image(Tradesong.assets.<Texture>get("icons/" + internalName + ".png")));
        table.add(displayName).row();
        return table;
    }
}
