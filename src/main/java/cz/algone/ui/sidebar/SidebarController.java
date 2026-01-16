package cz.algone.ui.sidebar;

import cz.algone.algorithm.fill.pattern.PatternAlias;
import cz.algone.algorithmController.AlgorithmControllerAlias;
import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithmController.clip.PolygonOrientation;
import cz.algone.util.scene.SceneAlias;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import java.util.function.Consumer;

public class SidebarController {
    @FXML private ToggleButton btnTogglePattern;
    @FXML private VBox sidebar;

    @FXML private VBox lineAlgorithms;
    @FXML private VBox seedFillAlgorithms;
    @FXML private VBox patterns;
    @FXML private VBox clipModes;
    @FXML private VBox algorithmBox;
    @FXML private VBox sceneBox;

    @FXML private ToggleGroup dropdownToggle;
    @FXML private ToggleGroup algorithmToggle;
    @FXML private ToggleGroup sceneToggle;
    @FXML private ToggleGroup orientationToggle;

    private Consumer<AlgorithmAlias> onRasterizerChanged;
    private Consumer<PatternAlias> onPatternChanged;
    private Consumer<PolygonOrientation> onPolygonOrientationChanged;
    private Consumer<SceneAlias> onSceneChanged;

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
                        // userData neodpovídá enumu → ignoruje
                    }
                }
            }
        });
        orientationToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null || onPolygonOrientationChanged == null) return;
            if (newToggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        PolygonOrientation orientation = PolygonOrientation.valueOf(data.toString());
                        onPolygonOrientationChanged.accept(orientation);
                    } catch (IllegalArgumentException ignored) {
                        // userData neodpovídá enumu → ignoruje
                    }
                }
            }

        });
        sceneToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null || onSceneChanged == null) return;
            if (newToggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        SceneAlias alias = SceneAlias.valueOf(data.toString());
                        onSceneChanged.accept(alias);
                    } catch (IllegalArgumentException ignored) {
                        // userData neodpovídá enumu → ignoruje
                    }
                }
            }
        });
        bindManagedProperties();
        toggleDropdown();
    }
    /** dropdown button metoda, mění visible property jednotlivých VBox */
    @FXML
    private void toggleDropdown() {
        for (Toggle toggle : dropdownToggle.getToggles()) {
            if (toggle instanceof ToggleButton btn) {
                boolean visible = btn.isSelected();
                Node targetBox = sidebar.lookup("#" + btn.getUserData());
                Polygon arrow = findArrow(btn);
                targetBox.setVisible(visible);
                if(arrow != null) arrow.setRotate(visible ? 180 : 0);
            }
        }
    }

    @FXML
    public void togglePattern() {
        boolean selected = btnTogglePattern.isSelected();
        if (selected)
            btnTogglePattern.setText("ON");
        else
            btnTogglePattern.setText("OFF");
        onPatternChanged.accept(selected ? PatternAlias.CHECKER : null);
    }

    /** Přepíná viditelné sekce podle {@link AlgorithmControllerAlias}*/
    public void showOptionsFor(AlgorithmControllerAlias alias) {
        lineAlgorithms.setVisible(alias == AlgorithmControllerAlias.LINE);
        seedFillAlgorithms.setVisible(alias == AlgorithmControllerAlias.SEED_FILL);
        patterns.setVisible(alias == AlgorithmControllerAlias.SEED_FILL || alias == AlgorithmControllerAlias.SCANLINE_FILL);
        clipModes.setVisible(alias == AlgorithmControllerAlias.CLIP);
    }
    /** podle {@link AlgorithmAlias} zvolí selected ToggleButton
     * pro konkrétní algoritmus*/
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
    /** podle {@link SceneAlias} zvolí selected ToggleButton
     * pro konkrétní algoritmus*/
    public void setSelectedScene(SceneAlias alias) {
        for (Toggle toggle : sceneToggle.getToggles()) {
            if (toggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null && data.toString().equals(alias.name())) {
                    sceneToggle.selectToggle(btn);
                    return;
                }
            }
        }
    }
    /** Pro každý dropdown button prohledá graphics a najde arrow icon */
    private Polygon findArrow(ToggleButton btn) {
        Node g = btn.getGraphic();
        if (!(g instanceof Parent p)) return null;

        return (Polygon) p.lookup(".arrow-icon");
    }

    private void bindManagedProperties() {
        algorithmBox.managedProperty().bind(algorithmBox.visibleProperty());
        sceneBox.managedProperty().bind(sceneBox.visibleProperty());
        lineAlgorithms.managedProperty().bind(lineAlgorithms.visibleProperty());
        patterns.managedProperty().bind(patterns.visibleProperty());
        clipModes.managedProperty().bind(clipModes.visibleProperty());
        seedFillAlgorithms.managedProperty().bind(seedFillAlgorithms.visibleProperty());
    }

    public void setOnRasterizerChange(Consumer<AlgorithmAlias> listener) {this.onRasterizerChanged = listener;}
    public void setOnPatternChanged(Consumer<PatternAlias> listener) {this.onPatternChanged = listener;}
    public void setOnPolygonOrientationChanged(Consumer<PolygonOrientation> listener) {this.onPolygonOrientationChanged = listener;}
    public void setOnSceneChanged(Consumer<SceneAlias> listener) {this.onSceneChanged = listener;}
}
