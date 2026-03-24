package cz.algone.ui.toolbar.solids;

import cz.algone.common.enumAlias.SolidAlias;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SolidsController {
    @FXML private ToggleButton tetrahedronBtn;
    @FXML private ToggleButton cuboidBtn;
    @FXML private ToggleButton cylinderBtn;
    @FXML private ToggleButton curveBtn;
    @FXML private ToggleButton planeBtn;

    private final List<ToggleButton> buttons = new ArrayList<>();
    private BiConsumer<SolidAlias, Boolean> onToggle;

    @FXML
    private void initialize() {
        buttons.addAll(List.of(tetrahedronBtn, cuboidBtn, cylinderBtn, curveBtn, planeBtn));
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

    public void setOnToggle(BiConsumer<SolidAlias, Boolean> onToggle) {this.onToggle = onToggle;}
}
