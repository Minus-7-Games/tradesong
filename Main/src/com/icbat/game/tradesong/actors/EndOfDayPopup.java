package com.icbat.game.tradesong.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.assetReferences.TextureAssets;
import com.icbat.game.tradesong.utils.SpacedTable;

/***/
public class EndOfDayPopup extends Table {

    public EndOfDayPopup() {
        this.add(makeContentTable());
        this.setFillParent(true);
    }

    private Table makeContentTable() {
        SpacedTable contentTable = new SpacedTable();

        contentTable.add(new Label("End of day", Tradesong.uiStyles.getLabelStyle()));
        contentTable.spacedRows(2);
        contentTable.add(new Label("New contracts generated!", Tradesong.uiStyles.getLabelStyle()));
        contentTable.spacedRow();
        contentTable.add(new Label("Rent collected:  200", Tradesong.uiStyles.getLabelStyle())); // TODO extract rent to a variable somewhere.


        contentTable.setBackground(new TextureRegionDrawable(new TextureRegion(Tradesong.getTexture(TextureAssets.POPUP_BG))));
        contentTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                removeThis();
            }
        });
        contentTable.setTouchable(Touchable.enabled);

        contentTable.setSize(512, 256); // BG dimensions
        return contentTable;
    }

    protected void removeThis() {
        this.remove();
    }
}
