package com.icbat.game.tradesong.screens.stages;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.icbat.game.tradesong.Item;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.screens.listeners.DragMoveListener;

import java.util.*;

public class MapStage extends Stage {
    private HashSet<String> spawnableItemNames = new HashSet<String>();
    private HashSet<ValidSpawnArea> spawnAreas = new HashSet<ValidSpawnArea>();

    public MapStage(MapProperties mapProperties) {
        setupAreas(mapProperties);
        getPrototypeNames(mapProperties);
        spawnInitialItems(mapProperties);
    }

    private void setupAreas(MapProperties mapProperties) {
        String validSpawnAreasBlob = (String) mapProperties.get("validSpawnAreas");
        if (validSpawnAreasBlob != null) {
            String[] spawnAreas = validSpawnAreasBlob.split(";");

            for (String area : spawnAreas) {
                String[] coords = area.split(",");
                this.spawnAreas.add(new ValidSpawnArea(coords));
            }
        }
    }

    private void getPrototypeNames(MapProperties mapProperties) {
        String spawnableItemsBlob = (String) mapProperties.get("spawnableItems");
        if (spawnableItemsBlob != null) {
            String[] spawnableItems = spawnableItemsBlob.split(",");
            Collections.addAll(this.spawnableItemNames, spawnableItems);
            for (String name : this.spawnableItemNames) {
                Gdx.app.debug("spawnableItem", name);
            }
        }

    }

    private void spawnInitialItems (MapProperties mapProperties) {
        Integer initialNodes = Integer.parseInt((String) mapProperties.get("initialSpawnCount"));
        if (!spawnableItemNames.isEmpty() && !spawnAreas.isEmpty()) {
            Random random = new Random();
            List<String> spawnableItems = new ArrayList<String>(spawnableItemNames);
            List<ValidSpawnArea> areas = new ArrayList<ValidSpawnArea>(spawnAreas);

            for (int i=0; i<initialNodes; ++i) {
                String nameToSpawn = spawnableItems.get(random.nextInt(spawnableItemNames.size()));

                Item spawn = Tradesong.itemPrototypes.get(nameToSpawn);

                if (spawn == null) {
                    Gdx.app.error("spawning item on map", "null");
                    return;
                }

                areas.get(0).spawnItemInsideArea(spawn); // TODO multiple areas.
                this.addActor(spawn); // Looks weird, but the spawn area just sets position on the actor.
                Gdx.app.log("spawning item", spawn.getName());
                Gdx.app.debug("spawned at", spawn.getX() + ", " + spawn.getY());
            }
        }
        // TODO setup timer to spawn more

    }

    public void setDragListener (Camera camera) {
        Gdx.app.debug("", "Setting a drag listener");
        this.addListener(new DragMoveListener(camera));
    }

    /**
     * Rectangular area in which items can spawn. Provides clean way to spawn an item in inside the area.
     * */
    class ValidSpawnArea {
        Integer startX;
        Integer startY;
        Integer endX;
        Integer endY;
        private static final int ICON_SIZE = 32;

        /**
         * Expects exactly 4 elements. Will fail with errors if passed less than 4.
         * */
        public ValidSpawnArea(String[] coords) {
            this.startX = Integer.parseInt(coords[0]);
            this.startY = Integer.parseInt(coords[1]);
            this.endX = Integer.parseInt(coords[2]);
            this.endY = Integer.parseInt(coords[3]);
        }

        public void spawnItemInsideArea(Item item) {
            Random random = new Random();
            int x = random.nextInt(endX - startX) + startX;
            int y = random.nextInt(endY - startY) + startY;
            x *= ICON_SIZE;
            y *= ICON_SIZE;

            item.setPosition(x, y);
        }
    }
}
