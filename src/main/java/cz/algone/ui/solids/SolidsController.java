package cz.algone.ui.solids;

import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.model.models3D.SolidToggleEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SolidsController {
    @FXML private ToggleButton tetrahedronBtn;
    @FXML private ToggleButton cuboidBtn;
    @FXML private ToggleButton cylinderBtn;
    @FXML private ToggleButton curveBtn;
    @FXML private ToggleButton planeBtn;

    private final List<ToggleButton> buttons = new ArrayList<>();
    private Consumer<SolidToggleEvent> onToggle;

    @FXML
    private void initialize() {
        buttons.addAll(List.of(tetrahedronBtn, cuboidBtn, cylinderBtn, curveBtn, planeBtn));
        for (ToggleButton btn : buttons) {
            btn.selectedProperty().addListener((obs, oldV, selected) -> {
                SolidAlias alias = SolidAlias.valueOf(btn.getUserData().toString());
                onToggle.accept(new SolidToggleEvent(alias, selected));
            });
        }
    }

    public void unselectAllButtons() {
        for (ToggleButton btn : buttons) {
            btn.setSelected(false);
        }
    }

    public void setOnToggle(Consumer<SolidToggleEvent> onToggle) {this.onToggle = onToggle;}
}
