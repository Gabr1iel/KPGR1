package cz.algone.ui.sidebar;

import cz.algone.algorithm.fill.pattern.IPattern;
import cz.algone.algorithm.fill.pattern.PatternAlias;
import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.algorithm.AlgorithmAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

import java.util.function.Consumer;

public class SidebarController {
    @FXML private ToggleButton btnAlgorithms;
    @FXML private ToggleButton btnTogglePattern;
    @FXML private VBox algorithmBox;
    @FXML private Polygon arrowIcon;

    @FXML private VBox lineAlgorithms;
    @FXML private VBox seedFillAlgorithms;
    @FXML private VBox patterns;

    @FXML private ToggleGroup algorithmToggle;

    private Consumer<AlgorithmAlias> onRasterizerChanged;
    private Consumer<PatternAlias> onPatternChanged;

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

    @FXML
    public void togglePattern() {
        boolean selected = btnTogglePattern.isSelected();
        onPatternChanged.accept(selected ? PatternAlias.CHECKER : null);
    }

    public void showOptionsFor(AlgorithmControllerAlias alias) {
        // přepínáme, která sekce je vidět
        lineAlgorithms.setVisible(alias == AlgorithmControllerAlias.LINE);
        lineAlgorithms.managedProperty().bind(lineAlgorithms.visibleProperty());

        seedFillAlgorithms.setVisible(alias == AlgorithmControllerAlias.SEED_FILL);
        seedFillAlgorithms.managedProperty().bind(seedFillAlgorithms.visibleProperty());

        patterns.setVisible(alias == AlgorithmControllerAlias.SEED_FILL || alias == AlgorithmControllerAlias.SCANLINE_FILL);
        patterns.managedProperty().bind(patterns.visibleProperty());
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
    public void setOnPatternChanged(Consumer<PatternAlias> listener) {this.onPatternChanged = listener;}
}
