package cz.algone.ui.sidebar.algorithmSection;

import cz.algone.common.enumAlias.CubicAlias;
import cz.algone.ui.sidebar.ISidebarSectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public class Algorithm3DSectionController implements ISidebarSectionController {
    @FXML private ToggleGroup algorithmToggle;
    @FXML private ToggleButton bezierBtn;

    private Consumer<CubicAlias> onCubicChanged;

    @FXML
    public void initialize() {
        algorithmToggle.selectedToggleProperty().addListener((observable, oldValue, newToggle) -> {
           if (newToggle == null) return;
           if (newToggle instanceof ToggleButton btn) {
               Object data = btn.getUserData();
               if (data != null) {
                   try {
                       CubicAlias alias = CubicAlias.valueOf(data.toString());
                       onCubicChanged.accept(alias);
                   } catch (Exception e) {

                   }
               }
           }
        });
    }

    public void resetCubic() {
        bezierBtn.setSelected(true);
    }

    public void setOnCubicChanged(Consumer<CubicAlias> listener) {this.onCubicChanged = listener;}

    @Override
    public ToggleGroup getToggleGroup() {return null;}
}
