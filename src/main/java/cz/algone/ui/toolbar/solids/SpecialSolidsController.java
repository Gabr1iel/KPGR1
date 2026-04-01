package cz.algone.ui.toolbar.solids;

import cz.algone.common.enumAlias.SolidAlias;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SpecialSolidsController {
    @FXML private ToggleButton lightBtn;
    @FXML private ToggleButton curveBtn;
    @FXML private ToggleButton planeBtn;
    @FXML private ToggleButton diskBtn;
    @FXML private ToggleButton litSurfaceBtn;

    private final List<ToggleButton> buttons = new ArrayList<>();
    private BiConsumer<SolidAlias, Boolean> onToggle;

    @FXML
    private void initialize() {
        buttons.addAll(List.of(lightBtn, curveBtn, planeBtn, diskBtn, litSurfaceBtn));
        for (ToggleButton btn : buttons) {
            btn.selectedProperty().addListener((obs, oldV, selected) -> {
                SolidAlias alias = SolidAlias.valueOf(btn.getUserData().toString());
                onToggle.accept(alias, selected);
            });
        }
    }

    public void unselectAllButtons() {
        for (ToggleButton btn : buttons) {
            btn.setSelected(false);
        }
    }

    public void setOnToggle(BiConsumer<SolidAlias, Boolean> onToggle) { this.onToggle = onToggle; }
}
