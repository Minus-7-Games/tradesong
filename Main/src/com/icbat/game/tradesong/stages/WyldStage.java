package com.icbat.game.tradesong.stages;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.icbat.game.tradesong.Item;
import com.icbat.game.tradesong.Tradesong;

import java.util.ArrayList;
import java.util.Random;

/** This stage governs:
 *  - items
 *  - the draggable, clear background
 *  - it's also a nice encapsulation of adding/removing items
 *  */
public class WyldStage extends LevelStage {

    // Constants for extracting map properties
    public static final String PROPERTY_INITIAL_SPAWN_COUNT = "initialSpawnCount";
    public static final String PROPERTY_SPAWN_CAPACITY = "maxSpawnCapacity";
    public static final String PROPERTY_SPAWNABLE_ITEMS = "spawnableItems";
    public static final String PROPERTY_VALID_SPAWN_AREA = "validSpawnArea";

    private Area validSpawn;

    private final ArrayList<Item> possibleItemSpawns = new ArrayList<Item>();

    Sound gatherSound = Tradesong.getGatherSound();

    Timer gatherTimer = new Timer();

    int initialItemCount;
    int itemCount;
    int maxSpawnedPerMap;
    private final int mapHeight;

    public WyldStage(Tradesong gameInstance, MapProperties properties) {
        super(gameInstance, properties);

        // Use the Map properties to get some good stuff
        initialItemCount = Integer.parseInt((String)properties.get(PROPERTY_INITIAL_SPAWN_COUNT));
        maxSpawnedPerMap = Integer.parseInt((String) properties.get(PROPERTY_SPAWN_CAPACITY));
        String[] itemsArray = ((String)properties.get(PROPERTY_SPAWNABLE_ITEMS)).split(",");

        // Figure out what spawns here
        for (String itemName : itemsArray) {
            possibleItemSpawns.add( Tradesong.gameState.getItemByName(itemName) );
        }

        // Set up knowledge of where things can spawn
        mapHeight = (Integer)properties.get("height");

        String validSpawnsBlob = (String)properties.get(PROPERTY_VALID_SPAWN_AREA);
        String[] VSA = validSpawnsBlob.split(",");

        validSpawn = new Area( Integer.parseInt(VSA[0]),Integer.parseInt(VSA[1]),Integer.parseInt(VSA[2]),Integer.parseInt(VSA[3]));


        for (int i = 0; i < initialItemCount; ++i) {
            spawnItem();
        }

        layout();

    }

    public boolean spawnItem() {
        if (itemCount < maxSpawnedPerMap + 1) {
            finalizeItemForView(chooseItemByRarity());
            ++itemCount;
            return true;
        } else {
            return false;
        }
    }

    /***/
    private Item chooseItemByRarity() {
        int maxRarity = 0;
        int totalRarity = 0;

        for (Item item : possibleItemSpawns) {
            totalRarity += item.getRarity();
            if (item.getRarity() > maxRarity)
                maxRarity = item.getRarity();
        }

        ArrayList<Integer> scaledRarities = new ArrayList<Integer>();
        for (Item item : possibleItemSpawns) {
            int n = maxRarity + 1 - item.getRarity();


            scaledRarities.add( n );
        }

        int n = new Random().nextInt( totalRarity );

        for (int i = 0; i < possibleItemSpawns.size(); ++i) {
            if (n < scaledRarities.get(i)) {
                return new Item(possibleItemSpawns.get(i));
            }
            else {
                n -= scaledRarities.get(i);
            }

        }

        return null;
    }

    /** Performs common steps for Items being added to the stage randomly */
    private void finalizeItemForView(Item item) {

        item.addListener(new GatherClickListener(item));
        int[] coords = validSpawn.getRandomCoordsInside();
        item.setPosition(coords[0], coords[1]);
        item.setTouchable(Touchable.enabled);
        item.setVisible(true);
        this.addActor(item);
    }

    public void removeItemCount() {
        removeItemCount(1);
    }

    public void removeItemCount(int i) {
        itemCount -= i;
    }

    /** Simple class to represent a 2d space. Currently assumes map coordinate system (origin in top-left), may need to adjust */
    class Area {
        int left;
        int top;
        int right;
        int bottom;

        Area(int left, int top, int right, int bottom) {
            this.left = left;
            this.right = right;

            // Reflect around mapHeight as the map's coords are top-left and the stage's are bottom-left
            this.bottom = mapHeight - bottom;
            this.top = mapHeight - top;


        }

        int[] getRandomCoordsInside() {
            Random rand = new Random();
            int[] randCoords = new int[2];

            int n;

            do {
                n = rand.nextInt(right);
            } while (n < left);
            randCoords[0] = n * 32;

            do {
                n = rand.nextInt(top);
            } while (n < bottom);
            randCoords[1] = n * 32;

            return randCoords;
        }


    }


    /** Class to handle touching/clicking of items on levels.  */
    class GatherClickListener extends ClickListener {
        Item owner;

        GatherClickListener(Item owner) {
            this.owner = owner;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            gatherSound.stop();
            gatherSound.play();

            gatherTimer.stop();
            gatherTimer.clear();
            gatherTimer.scheduleTask(new Timer.Task() {

                @Override
                public void run() {
                    if (Tradesong.gameState.getInventory().add(new Item(owner))) {
                        gatherSound.stop();
                        removeItemCount();
                        owner.remove();
                    }
                }
            }, Tradesong.gameState.getParameterByName(Tradesong.getParamDelayGather()).getCurrentValue());
            gatherTimer.start();


            return super.touchDown(event, x, y, pointer, button);
        }
    }

}
