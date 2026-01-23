package cz.algone.ui.sidebar;

import cz.algone.common.enumAlias.PatternAlias;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.common.enumAlias.PolygonOrientation;
import cz.algone.common.enumAlias.IAlias;
import cz.algone.ui.sidebar.settingsSection.FillSettingsSectionController;
import cz.algone.common.enumAlias.SceneAlias;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SidebarController {
    @FXML private ToggleButton btnScenes;
    @FXML private ToggleButton btnAlgorithms;
    @FXML private ToggleButton btnSettings;
    @FXML private VBox sidebar;

    @FXML private VBox algorithmBox;
    @FXML private VBox sceneBox;
    @FXML private VBox settingsBox;
    @FXML private VBox algorithmBoxPlaceholder;
    @FXML private VBox settingsBoxPlaceholder;

    @FXML private ToggleGroup algorithmToggle;
    @FXML private ToggleGroup sceneToggle;

    private List<ToggleButton> dropdownToggle;
    private Consumer<AlgorithmAlias> onRasterizerChanged;
    private Consumer<PatternAlias> onPatternChanged;
    private Consumer<PolygonOrientation> onPolygonOrientationChanged;
    private Consumer<SceneAlias> onSceneChanged;

    @FXML
    private void initialize() {
        dropdownToggle = List.of(btnScenes, btnAlgorithms, btnSettings);
        SceneAlias alias = SceneAlias.SCENE_2D;

        Platform.runLater(() -> addListenerToToggleGroup(sceneToggle, onSceneChanged, alias));
        bindManagedProperties();
        toggleDropdown();
    }
    /** dropdown button metoda, mění visible property jednotlivých VBox */
    @FXML
    private void toggleDropdown() {
        for (Toggle toggle : dropdownToggle) {
            if (toggle instanceof ToggleButton btn) {
                boolean visible = btn.isSelected();
                Node targetBox = sidebar.lookup("#" + btn.getUserData());
                Polygon arrow = findArrow(btn);
                targetBox.setVisible(visible);
                if(arrow != null) arrow.setRotate(visible ? 180 : 0);
            }
        }
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
     * pro konkrétní scénu*/
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
    /** Binduje hlavní sekce sidebaru aby když nejsou vidět nezabírali místo v layoutu */
    private void bindManagedProperties() {
        algorithmBox.managedProperty().bind(algorithmBox.visibleProperty());
        sceneBox.managedProperty().bind(sceneBox.visibleProperty());
        settingsBox.managedProperty().bind(settingsBox.visibleProperty());
    }

    /** Pomocí {@link AlgorithmControllerAlias} nastaví URL cestu ke správnému souboru
     * pro zobrazení sekce algoritmů a nastavení */
    public void showSidebarSections(AlgorithmControllerAlias algorithmControllerAlias, AlgorithmAlias algorithmAlias) {
        try {
            FXMLLoader algorithmLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/algorithmsSection/" + algorithmControllerAlias.name() + ".fxml"));
            if (algorithmLoader.getLocation() == null)
                algorithmLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/algorithmsSection/EMPTY.fxml"));
            FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/settingsSection/" + algorithmControllerAlias.name() + ".fxml"));
            if (settingsLoader.getLocation() == null)
                settingsLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/settingsSection/EMPTY.fxml"));

            Parent rootAlgorithmSection = algorithmLoader.load();
            ISidebarSectionController currentAlgorithmSectionController = algorithmLoader.getController();
            algorithmBoxPlaceholder.getChildren().setAll(rootAlgorithmSection);
            algorithmToggle = currentAlgorithmSectionController.getToggleGroup();
            addListenerToToggleGroup(algorithmToggle, onRasterizerChanged, algorithmAlias);

            Parent rootSettingsSection = settingsLoader.load();
            ISidebarSectionController currentSettingsSectionController = settingsLoader.getController();
            settingsBoxPlaceholder.getChildren().setAll(rootSettingsSection);
            ToggleGroup settingsToggle = currentSettingsSectionController.getToggleGroup();
            addListenerToToggleGroup(settingsToggle, getCurrentSettingsConsumer(algorithmControllerAlias), algorithmAlias);

            setControllerConsumerEvent(currentSettingsSectionController);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /** Nastaví vlastnotsi {@link ToggleButton} v seznamu který je předán,
     * vzhledem k využití, následně vezme jeho userData a namapuje na konkrétní
     * {@link IAlias} a to předá do Consumeru*/
    private <T extends IAlias> void addListenerToToggleGroup(ToggleGroup toggleGroup, Consumer<T> consumer, IAlias alias) {
        if (toggleGroup == null) return;
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) return;
            if (newToggle instanceof ToggleButton btn) {
                Object data = btn.getUserData();
                if (data != null) {
                    try {
                        T currentAlias = (T) alias.getAlias(data.toString());
                        consumer.accept(currentAlias);
                    } catch (IllegalArgumentException ignored) {
                        // userData neodpovídá enumu → ignoruje
                    }
                }
            }
        });
    }
    /** Nastaví hlídání Consumeru u settings controllerů, pokud nějaký Consumer mají */
    private void setControllerConsumerEvent(ISidebarSectionController controller) {
        if (controller instanceof FillSettingsSectionController fillController)
            fillController.setOnPatternChanged((patternAlias) -> onPatternChanged.accept(patternAlias));
        else
            return;
    }
    /** Pomocí {@link AlgorithmControllerAlias} rozhodne který Consumer se bude využívat
     * v dané settings sekci */
    private Consumer getCurrentSettingsConsumer(AlgorithmControllerAlias alias) {
        switch (alias.name()) {
            case "CLIP" -> {return onPolygonOrientationChanged;}
            case "SEED_FILL" -> {return onPatternChanged;}
            case "SCALNLINE_FILL" -> {return onPatternChanged;}
        }
        return null;
    }

    public void setOnRasterizerChange(Consumer<AlgorithmAlias> listener) {this.onRasterizerChanged = listener;}
    public void setOnPatternChanged(Consumer<PatternAlias> listener) {this.onPatternChanged = listener;}
    public void setOnPolygonOrientationChanged(Consumer<PolygonOrientation> listener) {this.onPolygonOrientationChanged = listener;}
    public void setOnSceneChanged(Consumer<SceneAlias> listener) {this.onSceneChanged = listener;}
}
