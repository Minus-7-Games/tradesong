package com.icbat.game.tradesong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.icbat.game.tradesong.Tradesong;
import com.icbat.game.tradesong.stages.AbstractStage;

public class SettingsScreen extends AbstractScreen {
    public SettingsScreen(Tradesong gameInstance) {
        stages.add(new SettingsStage(gameInstance));
    }


    class SettingsStage extends AbstractStage {
        public static final String KEY_SFX_VOL = "sfx_volume";
        public static final String KEY_MUSIC_VOL = "music_volume";
        Tradesong gameInstance;
        Preferences preferences = Gdx.app.getPreferences("General_Preferences");

        Table table = new Table();
        Group indicators = new Group();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();

        final Slider musicSlider;
        final Label musicIndicator;
        final Slider SFXSlider;
        final Label SFXIndicator;



        SettingsStage(Tradesong gameInstance) {

            this.gameInstance = gameInstance;

            this.table.setFillParent(true);

            this.buttonStyle.font = new BitmapFont();
            this.buttonStyle.fontColor = Color.WHITE;

            this.labelStyle.font = new BitmapFont();
            this.labelStyle.fontColor = Color.WHITE;

            this.sliderStyle.knob = new TextureRegionDrawable( new TextureRegion( Tradesong.getSliderHead() ) );
            this.sliderStyle.background = new TextureRegionDrawable( new TextureRegion( Tradesong.getSliderBG(), 100, 8 ) );

            // Set up some caching in case things are cancelled
            int cachedMusicVol = preferences.getInteger(KEY_MUSIC_VOL, 50);
            int cachedSFXVol = preferences.getInteger(KEY_SFX_VOL, 50);

            this.musicIndicator = newNumericalIndicator(KEY_MUSIC_VOL, cachedMusicVol);
            this.musicSlider = newSlider(cachedMusicVol);
            this.musicSlider.addListener(new SliderListener(musicSlider, musicIndicator));

            this.SFXIndicator = newNumericalIndicator(KEY_SFX_VOL, cachedSFXVol);
            this.SFXSlider = newSlider(cachedSFXVol);
            this.SFXSlider.addListener(new SliderListener(SFXSlider, SFXIndicator));

        }

        @Override
        public void layout() {
            this.clear();
            table.clearChildren();

            this.addActor(this.table);

            this.table.add();
            this.table.add(newSettingsHeader("Music Volume"));
            this.table.row();
            this.table.add(newStringReferencePoint("0% "));
            this.table.add(musicSlider);
            this.table.add(newStringReferencePoint(" 100%"));
            this.table.row();
            this.table.add();
            this.table.add(musicIndicator);

            this.table.row();
            this.table.row();

            this.table.add();
            this.table.add(newSettingsHeader("SFX Volume"));
            this.table.row();
            this.table.add(newStringReferencePoint("0% "));
            this.table.add(SFXSlider);
            this.table.add(newStringReferencePoint(" 100%"));
            this.table.row();
            this.table.add();
            this.table.add(SFXIndicator);

            // Save and Exit button, Discard button
            addAcceptChangesButton();
            addDiscardButton();
        }


        private void addAcceptChangesButton() {
            TextButton saveChangesButton = new TextButton("Accept changes", buttonStyle);

            saveChangesButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.goBackAScreen();
                    //TODO actually save them
                }
            });

            saveChangesButton.setPosition(this.getWidth() - saveChangesButton.getWidth(), 0);

            this.addActor(saveChangesButton);
        }

        private void addDiscardButton() {
            TextButton discardChangesButton = new TextButton("Discard changes", buttonStyle);

            discardChangesButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.goBackAScreen();
                }
            });

            this.addActor(discardChangesButton);
        }

        private Label newNumericalIndicator(String linkedKey, int indicatorStartingValue) {
            Label indicator = new Label(String.valueOf(indicatorStartingValue), labelStyle);
            indicator.setName(linkedKey);
            indicators.addActor(indicator);
            return indicator;
        }

        /***/
        private Slider newSlider(int startingVal) {
            Slider settingSlider = new Slider(0, 100, 5, false, sliderStyle);
            settingSlider.setWidth(this.getWidth()/2);
            settingSlider.setValue(startingVal);

            return settingSlider;
        }

        private Label newStringReferencePoint(String referenceValue) {
            return new Label(referenceValue, labelStyle);
        }

        private Label newSettingsHeader(String headerText) {
            return new Label(headerText, labelStyle);
        }


        class SliderListener extends ChangeListener {

            Slider owner;
            Label linkedTarget;

            SliderListener(Slider owner, Label linkedTarget) {
                this.linkedTarget = linkedTarget;
                this.owner = owner;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                linkedTarget.setText(String.valueOf((int)(owner.getValue())));
            }
        }
    }
}
