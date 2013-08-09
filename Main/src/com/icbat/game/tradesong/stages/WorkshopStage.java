package com.icbat.game.tradesong.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.icbat.game.tradesong.Item;
import com.icbat.game.tradesong.Recipe;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.Workshop;

import java.util.Set;

public class WorkshopStage extends Stage {

    private Workshop workshop;
    private TextButton header;
    private Group frames = new Group();
    private Group ingredients = new Group();
    private Texture frameTexture;

    private static final int SPACER = 10;
    private Tradesong gameInstance;
    private Image resultFrame;

    public WorkshopStage(Tradesong gameInstance) {
        this(gameInstance, new Workshop("Blacksmith"));
    }

    public WorkshopStage(Tradesong gameInstance, Workshop workshop) {
        super();
        this.gameInstance = gameInstance;
        frameTexture = this.gameInstance.assets.get(Tradesong.getFramePath());
        setWorkshop(workshop); // Handles the standard setup

    }

    private void addWorkshopTitle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();

        header = new TextButton(workshop.getType(), style);
        header.setVisible(true);
        header.setTouchable(Touchable.disabled);
        layOutVertically(header);

        this.addActor(header);

    }

    private void addFrames() {
        frames.clearChildren();

        for (int i = 0; i < workshop.getNumberOfSlots(); ++i) {
            Image frame = makeIndividualFrame();
            layOutVertically(frame);
            frame.setName(""+(Integer)i);
            frames.addActor(frame);
        }

        this.addActor(frames);
    }

    private Image makeIndividualFrame() {
        Image frameActor = new Image( new TextureRegion(frameTexture) );
        frameActor.setVisible(true);
        frameActor.setTouchable(Touchable.disabled);
        return frameActor;
    }

    private void addArrowAndResultFrame() {
        Texture arrowTexture = this.gameInstance.assets.get(Tradesong.getPathSpriteArrow());
        Image arrowImage = new Image( arrowTexture );
        layOutVertically(arrowImage);
        this.addActor(arrowImage);

        resultFrame = makeIndividualFrame();
        layOutVertically(resultFrame);
        this.addActor(resultFrame);
    }

    /** Called when the workshop changes, including at startup. */
    public void setWorkshop(Workshop newWorkshop) {
        workshop = newWorkshop;

        if (header != null)
            header.remove();
        addWorkshopTitle();
        addFrames();

        addArrowAndResultFrame();
        this.addActor(ingredients);
    }

    /** Sets the bounds of the param to the next spot in a vertically descending pattern
     * @param   actor   actor on which to calculate bounds */
    private void layOutVertically(Actor actor) {
        actor.setBounds(this.getWidth() - actor.getWidth() - SPACER, findLowestY() - SPACER, actor.getWidth(), actor.getHeight());
    }

    private int findLowestY() {
        float lowestFound = this.getHeight() - 20;
        float check;
        for (Actor actor : this.getActors()) {
            if (!actor.getClass().equals(Group.class)) {  //TODO find a less-hacky way to do this.
                check = actor.getY() - actor.getHeight();
                if (check < lowestFound)
                    lowestFound = check;
            }

        }

        for (Actor actor : frames.getChildren()) {
            check = actor.getY() - actor.getHeight();
            if (check < lowestFound)
                lowestFound = check;
        }

        return (int) lowestFound;

    }

    private void addOutput(Item output, InventoryStage destination) {
        output.setBounds(resultFrame.getX(), resultFrame.getY(), output.getWidth(), output.getHeight());
        output.setTouchable(Touchable.enabled);
        output.addListener(new BackToInventoryClickListener(output, destination, false));
        this.addActor(output);


    }


    public boolean addIngredient(Item item, InventoryStage parent) {

        // Check to see if there's space to add more
        Integer size = ingredients.getChildren().size;
        if (size >= workshop.getNumberOfSlots()) {
            return false;
        }
        else {

            Actor frame = frames.findActor(size.toString());
            item.setBounds(frame.getX(), frame.getY(), item.getWidth(), item.getHeight());

            // Add the listener to remove it
            item.addListener(new BackToInventoryClickListener(item, parent, false));

            // Add the item
            ingredients.addActor(item);

            // Run the check to see if there's a product! If there is, add the picture
            Item output = checkIngredientsForOutput();
            if (output != null) {
                addOutput(output, parent);

            }

            return true;
        }

    }

    public void clearIngredients() {
        for (Actor ingredient : ingredients.getChildren()) {
            ingredient.remove();
        }
    }

    public Item checkIngredientsForOutput() {
        Set<Recipe> allRecipes = gameInstance.gameState.getAllKnownRecipes();
        for (Recipe recipe : allRecipes) {
            if (recipe.getWorkshop().equals(this.workshop.getType())) {

                if (recipe.check(ingredients.getChildren()))
                    return recipe.getOutput();

            }
        }
        return null;

    }




    class BackToInventoryClickListener extends ClickListener {

        private Item owner;
        private InventoryStage inventoryStage;
        private boolean isResult;

        BackToInventoryClickListener(Item owner, InventoryStage inventoryStage, boolean isResult) {

            this.owner = owner;
            this.inventoryStage = inventoryStage;
            this.isResult = isResult;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            super.touchDown(event, x, y, pointer, button);



            if (gameInstance.gameState.getInventory().add(new Item(owner))) {
                owner.remove();
                if (isResult)
                    clearIngredients();
                inventoryStage.update();

                return true;
            }
            else {
                return false;
            }
        }
    }

}
