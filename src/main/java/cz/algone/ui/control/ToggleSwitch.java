package cz.algone.ui.control;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Skin;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.skin.ToggleButtonSkin;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/** Přepínač ve stylu posuvné kuličky. Dědí z {@link ToggleButton}, takže se na stav
 *  váže úplně stejně jako ostatní přepínače v aplikaci. */
public class ToggleSwitch extends ToggleButton {

    public ToggleSwitch() {
        getStyleClass().setAll("toggle-switch");
        setPickOnBounds(true);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ToggleSwitchSkin(this);
    }

    /** Vykresluje dráhu a kuličku; přepnutí stavu kuličku plynule přesune.
     *  Dědí ze {@link ToggleButtonSkin}, protože ovládání myší i klávesnicí
     *  si od JavaFX 9 zajišťuje samotný skin. */
    private static final class ToggleSwitchSkin extends ToggleButtonSkin {
        private static final Duration SLIDE = Duration.millis(140);
        private static final double KNOB_INSET = 3;
        private static final double DEFAULT_WIDTH = 42;
        private static final double DEFAULT_HEIGHT = 24;

        private final Region track = new Region();
        private final Region knob = new Region();
        private final TranslateTransition slide;

        private ToggleSwitchSkin(ToggleSwitch control) {
            super(control);

            track.getStyleClass().add("track");
            knob.getStyleClass().add("knob");
            getChildren().addAll(track, knob);

            slide = new TranslateTransition(SLIDE, knob);
            control.selectedProperty().addListener((obs, old, selected) -> slideKnob(selected));
        }

        /** Obsah tvoří jen dráha a kulička, popisek zůstává prázdný — proto bez volání super. */
        @Override
        protected void layoutChildren(double x, double y, double width, double height) {
            track.resizeRelocate(x, y, width, height);

            double size = height - 2 * KNOB_INSET;
            knob.resizeRelocate(x + KNOB_INSET, y + KNOB_INSET, size, size);

            // Během přejezdu pozici řídí animace, jinak by ji přepočet rozvržení přepsal
            if (slide.getStatus() != Animation.Status.RUNNING)
                knob.setTranslateX(getSkinnable().isSelected() ? travel() : 0);
        }

        /** Dráha, kterou kulička ujede mezi krajními polohami. */
        private double travel() {
            return getSkinnable().getWidth() - getSkinnable().getHeight();
        }

        private void slideKnob(boolean selected) {
            slide.stop();
            slide.setFromX(knob.getTranslateX());
            slide.setToX(selected ? travel() : 0);
            slide.play();
        }

        @Override
        protected double computePrefWidth(double height, double top, double right, double bottom, double left) {
            return DEFAULT_WIDTH;
        }

        @Override
        protected double computePrefHeight(double width, double top, double right, double bottom, double left) {
            return DEFAULT_HEIGHT;
        }
    }
}
