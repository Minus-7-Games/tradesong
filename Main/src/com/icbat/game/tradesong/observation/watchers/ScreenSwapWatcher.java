package com.icbat.game.tradesong.observation.watchers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.assetReferences.SoundAssets;
import com.icbat.game.tradesong.observation.Notification;
import com.icbat.game.tradesong.observation.Watcher;
import com.icbat.game.tradesong.observation.notifications.ScreenSwapNotification;

/***/
public class ScreenSwapWatcher implements Watcher {

    private Sound swapSound = Tradesong.getSound(SoundAssets.SCREEN_SWAP);

    @Override
    public void handleNotification(Notification notification) {
        Gdx.app.debug("Screen Swap Watcher", "Checking if I care");
        if (!verifyICare(notification)) {
            return;
        }

        Gdx.app.debug("Screen Swap Watcher", "Swapping");

        swapSound.stop();
        swapSound.play();
    }

    private boolean verifyICare(Notification notification) {
        return notification != null && notification instanceof ScreenSwapNotification;
    }
}