package icbat.games.tradesong.engine.screens.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import icbat.games.tradesong.TradesongGame;
import icbat.games.tradesong.game.workshops.Workshop;

/***/
public class RemoveWorkshopListener extends ClickListener {
    private final Workshop workshop;

    public RemoveWorkshopListener(Workshop workshop) {
        this.workshop = workshop;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Gdx.app.log("add button", "clicked!");
        TradesongGame.holdings.removeWorkshop(workshop);
        return super.touchDown(event, x, y, pointer, button);
    }
}
