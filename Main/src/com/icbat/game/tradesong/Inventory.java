package com.icbat.game.tradesong;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory with persistence
 */
public class Inventory extends PersistantData {
    private int maxSize = 30;
    private List<Item> items = new ArrayList<Item>();

    public Inventory() {
        // TODO remove this, it's for testing!
        addItem(Tradesong.itemPrototypes.get("Ore"));
        addItem(Tradesong.itemPrototypes.get("Wood"));
        addItem(Tradesong.itemPrototypes.get("Blackberry"));


    }

    public boolean addItem(Item newItem) {
        if (canAdd()) {
            items.add(newItem);
            return true;
        } else {
            return false;
        }
    }

    public Item takeOutItemAt(int i) {
        Item removed = items.get(i);
        if (removed != null) {
            items.remove(i);
            return removed;
        }
         else {
            return null;
        }
    }

    public List<Item> getCopyOfInventory() {
        Gdx.app.debug("inventorySize", "" + items.size());
        return new ArrayList<Item>(items);
    }

    private boolean canAdd() {
        return items.size() + 1 < maxSize;
    }

    @Override
    public void save() {
        // TODO impl
    }

    @Override
    public void load() {
        // TODO impl
    }

    public int getMaxSize() {
        return maxSize;
    }
}
