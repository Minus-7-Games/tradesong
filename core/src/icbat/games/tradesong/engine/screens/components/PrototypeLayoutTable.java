package icbat.games.tradesong.engine.screens.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import icbat.games.tradesong.TradesongGame;
import icbat.games.tradesong.game.PlayerHoldings;
import icbat.games.tradesong.game.TurnTaker;
import icbat.games.tradesong.game.workshops.Workshop;

import java.util.Collection;

/***/
public class PrototypeLayoutTable extends Table {
    private final Collection<Workshop> potentialWorkshops;
    private final PlayerHoldings holdings;

    public PrototypeLayoutTable(final TurnTaker turnTaker, Collection<Workshop> potentialWorkshops, final PlayerHoldings holdings) {

        this.potentialWorkshops = potentialWorkshops;
        this.holdings = holdings;
        align(Align.top);
        setFillParent(true);

        add(new TurnCounter(turnTaker)).pad(10).align(Align.top).colspan(2);
        row();
        add(new SpareWorkerCounter(holdings)).pad(10).align(Align.top);
        add(new MoneyCounter(holdings)).pad(10).align(Align.top);

        row();

        add(potentialWorkshops()).pad(10).align(Align.top);
        add(activeWorkshops()).pad(10).align(Align.top);
        row();

        add(new ContractsDisplay()).pad(10).align(Align.top);
        add(new StorageDisplay()).pad(10).align(Align.top);
    }

    private Actor potentialWorkshops() {
        Table potentialDisplay = new Table(TradesongGame.skin);
        potentialDisplay.add("Potential Workshops").colspan(2).pad(10).row();
        for (final Workshop workshop : potentialWorkshops) {
            potentialDisplay.add(workshop.getActor()).pad(5);
            potentialDisplay.add(new BasicTextButton("Add ->", new AddWorkshopListener(workshop))).pad(5);
            potentialDisplay.row();
        }
        return potentialDisplay;
    }

    private Actor activeWorkshops() {
        Table activeDisplay = new Table(TradesongGame.skin);
        activeDisplay.add("Active Workshops").colspan(2).pad(10).row();
        for (final Workshop workshop : holdings.getWorkshops()) {
            activeDisplay.add(new BasicTextButton("<-", new RemoveWorkshopListener(workshop))).pad(5);
            activeDisplay.add(workshop.getActor()).pad(5);
            activeDisplay.add(new AddWorkersButton(workshop, holdings.getSpareWorkers())).pad(5);
            activeDisplay.add(new RemoveWorkersButton(workshop, holdings.getSpareWorkers())).pad(5);
            activeDisplay.row();
        }
        return activeDisplay;
    }


}


