package cz.algone.ui.sidebar;

import cz.algone.app.ShapeAlias;
import cz.algone.rasterizer.RasterizerAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import java.util.List;
import java.util.function.Consumer;

public class SidebarController {
    @FXML private ToggleButton btnAlgorithms;
    @FXML private ToggleGroup rasterizerToggle;
    @FXML private HBox algorithmBox;
    @FXML private VBox lineAlgorithms;
    @FXML private Polygon arrowIcon;

    private List<VBox> options;
    private Consumer<RasterizerAlias> onRasterizerChanged;

    @FXML
    private void initialize() {
        options = List.of(lineAlgorithms);
        algorithmBox.managedProperty().bind(algorithmBox.visibleProperty());

        rasterizerToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue == null) return;
           ToggleButton toggleButton = (ToggleButton) newValue;
            onRasterizerChanged.accept(RasterizerAlias.valueOf((String) toggleButton.getUserData()));
        });
    }

    @FXML
    private void toggleAlgorithms() {
        boolean visible = btnAlgorithms.isSelected();
        algorithmBox.setVisible(visible);
        arrowIcon.setRotate(visible ? 180 : 0);
    }

    public void showOptionsFor(ShapeAlias alias) {
        for (VBox option : options) {
            String type = option.getUserData().toString();
            option.setVisible(type.equals(alias.name()));
        }
    }

    public void setSelectedRasterizer(RasterizerAlias alias) {
        for (Toggle toggle : rasterizerToggle.getToggles()) {
            ToggleButton toggleButton = (ToggleButton) toggle;
            if (alias.name().equals(toggleButton.getUserData().toString())) {
                toggleButton.setSelected(true);
                onRasterizerChanged.accept(alias);
            }
        }
    }

    //metoda pro MainViewController, slouží k poskytnutí Consumeru
    public void setOnRasterizerChange(Consumer<RasterizerAlias> listener) {
        this.onRasterizerChanged = listener;
    }
}
