package cz.algone.ui.sidebar;

import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.algorithm.AlgorithmAlias;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SidebarController {

    @FXML private ToggleButton btnAlgorithms;
    @FXML private VBox algorithmBox;
    @FXML private Polygon arrowIcon;

    @FXML private VBox lineAlgorithms;
    @FXML private VBox seedFillAlgorithms;

    @FXML private ToggleGroup algorithmToggle;

    private Consumer<AlgorithmAlias> onRasterizerChanged;

    @FXML
    private void initialize() {
        algorithmToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null || onRasterizerChanged == null) return;
            if (newToggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        AlgorithmAlias alias = AlgorithmAlias.valueOf(data.toString());
                        onRasterizerChanged.accept(alias);
                    } catch (IllegalArgumentException ignored) {
                        // userData neodpovídá enumu → ignorujeme
                    }
                }
            }
        });
    }

    @FXML
    private void toggleAlgorithms() {
        boolean visible = btnAlgorithms.isSelected();
        algorithmBox.setVisible(visible);
        arrowIcon.setRotate(visible ? 180 : 0);
    }

    public void showOptionsFor(AlgorithmControllerAlias alias) {
        // přepínáme, která sekce je vidět
        lineAlgorithms.setVisible(alias == AlgorithmControllerAlias.LINE);
        lineAlgorithms.managedProperty().bind(lineAlgorithms.visibleProperty());

        seedFillAlgorithms.setVisible(alias == AlgorithmControllerAlias.FILL);
        seedFillAlgorithms.managedProperty().bind(seedFillAlgorithms.visibleProperty());
    }

    public void setSelectedRasterizer(AlgorithmAlias alias) {
        for (Toggle toggle : algorithmToggle.getToggles()) {
            if (toggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null && data.toString().equals(alias.name())) {
                    algorithmToggle.selectToggle(btn);
                    return;
                }
            }
        }
    }

    public void setOnRasterizerChange(Consumer<AlgorithmAlias> listener) {
        this.onRasterizerChanged = listener;
    }
}
