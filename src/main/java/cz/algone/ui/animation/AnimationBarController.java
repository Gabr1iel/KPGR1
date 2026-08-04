package cz.algone.ui.animation;

import cz.algone.ui.MainUIController;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/** Přehrávač animací pod plátnem.
 *  Zatím jde o náhled vzhledu — ovládací prvky nejsou napojené a průběh je ukázkový.
 *  Až budou animace krokovatelné, stačí {@link #progressProperty()} navázat na
 *  poměr aktuálního a celkového počtu kroků. */
public class AnimationBarController extends MainUIController {
    private static final double KNOB_SIZE = 14;

    /** Fáze algoritmu. Slouží k orientaci a skokům na začátek úseku,
     *  samotné převíjení je na nich nezávislé. */
    private static final double[] CHECKPOINTS = {0.22, 0.55, 0.80};
    private static final String[] CHECKPOINT_NAMES = {"Setřídění hran", "Průchod scanline", "Dokončení"};

    @FXML private HBox root;
    @FXML private StackPane track;
    @FXML private Region trackFill;
    @FXML private Region knob;
    @FXML private Label stepLabel;

    private final DoubleProperty progress = new SimpleDoubleProperty(0.38);

    public DoubleProperty progressProperty() {
        return progress;
    }

    @Override
    protected void onSceneContextReady() {
        root.managedProperty().bind(root.visibleProperty());
        root.visibleProperty().bind(sceneContext.animationBarVisibleProperty());

        bindTrack();
        addCheckpoints();
    }

    /** Vyplněná část a jezdec se odvozují od šířky dráhy, takže sedí i po změně velikosti okna. */
    private void bindTrack() {
        DoubleBinding filled = track.widthProperty().multiply(progress);

        StackPane.setAlignment(trackFill, Pos.CENTER_LEFT);
        trackFill.prefWidthProperty().bind(filled);
        trackFill.maxWidthProperty().bind(filled);

        StackPane.setAlignment(knob, Pos.CENTER_LEFT);
        knob.translateXProperty().bind(filled.subtract(KNOB_SIZE / 2));
    }

    /** Značky fází vloží pod jezdec, aby je nepřekrýval. */
    private void addCheckpoints() {
        int knobIndex = track.getChildren().indexOf(knob);

        for (int i = 0; i < CHECKPOINTS.length; i++) {
            Region marker = new Region();
            marker.getStyleClass().add("anim-marker");
            marker.setMouseTransparent(true);

            StackPane.setAlignment(marker, Pos.CENTER_LEFT);
            marker.translateXProperty().bind(track.widthProperty().multiply(CHECKPOINTS[i]));
            Tooltip.install(marker, new Tooltip(CHECKPOINT_NAMES[i]));

            track.getChildren().add(knobIndex + i, marker);
        }
    }
}
