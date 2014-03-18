package com.icbat.game.tradesong.observation.watchers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import com.icbat.game.tradesong.GameState;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.assetReferences.SoundAssets;
import com.icbat.game.tradesong.gameObjects.Item;
import com.icbat.game.tradesong.observation.Notification;
import com.icbat.game.tradesong.observation.Watcher;
import com.icbat.game.tradesong.observation.notifications.GatherNotification;
import com.icbat.game.tradesong.observation.notifications.StopNotification;
import com.icbat.game.tradesong.utils.Constants;

/**
 * Responds appropriately to an item node being gathered
 * */
public class GatheringWatcher implements Watcher {

    private Sound gatherSound = Tradesong.getSound(SoundAssets.GATHER_CLINK);
    private Sound completionSound = Tradesong.getSound(SoundAssets.SUCCESS);
    private Timer gatherTimer = new Timer();

    @Override
    public void handleNotification(Notification notification) {
        if (!verifyICare(notification)) {
             return;
        }

        if (notification instanceof GatherNotification) {
            startGathering((Item) notification.getContents());
        } else if (notification instanceof StopNotification) {
            stopGathering();
        }


    }

    private boolean verifyICare(Notification notification) {
        if (notification == null || notification.getAction() == null) {
            return false;
        }

        if (!(notification instanceof GatherNotification) && !(notification instanceof StopNotification)) {
            return false;
        }

        return true;
    }

    private void startGathering(final Item owner) {
        if (GameState.inventory.canAdd()) {
            gatherSound.stop();
            completionSound.stop();

            gatherSound.loop();
            gatherTimer.clear();
            gatherTimer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    gatherSound.stop();
                    completionSound.play();
                    GameState.inventory.addItem(new Item(owner));
                    owner.remove();
                }
            }, Constants.GATHER_TIME_BASE.value() * Tradesong.saveableState.getGatherTimeMultiplier());
        }
    }

    private void stopGathering() {
        gatherSound.stop();
        gatherTimer.stop();
        gatherTimer.clear();
    }


}
