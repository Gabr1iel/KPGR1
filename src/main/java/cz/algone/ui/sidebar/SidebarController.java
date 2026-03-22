package cz.algone.ui.sidebar;

import cz.algone.common.enumAlias.*;
import cz.algone.ui.MainUIController;
import cz.algone.ui.sidebar.algorithmSection.Algorithm3DSectionControllerMain;
import cz.algone.ui.sidebar.settingsSection.Settings3DSectionControllerMain;
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

public class SidebarController extends MainUIController {
    @FXML private ToggleButton btnScenes;
    @FXML private ToggleButton btnAlgorithms;
    @FXML private ToggleButton btnSettings;
    @FXML private Algorithm3DSectionControllerMain algorithm3DSectionController;
    @FXML private Settings3DSectionControllerMain settings3DSectionController;
    @FXML private VBox sidebar;

    @FXML private VBox algorithmBox;
    @FXML private VBox sceneBox;
    @FXML private VBox settingsBox;
    @FXML private VBox algorithmBoxPlaceholder;
    @FXML private VBox settingsBoxPlaceholder;

    @FXML private ToggleGroup algorithmToggle;
    @FXML private ToggleGroup sceneToggle;
    private List<ToggleButton> dropdownToggle;

    @FXML
    private void initialize() {
        dropdownToggle = List.of(btnScenes, btnAlgorithms, btnSettings);
        bindManagedProperties();
        toggleDropdown();
    }

    @Override
    protected void onSceneContextReady() {
        addListenerToToggleGroup(sceneToggle,
                (SceneAlias a) -> sceneContext.setScene(a), SceneAlias.SCENE_2D);

        sceneContext.sceneProperty().addListener((obs, old, newVal) -> setSelectedScene(newVal));
        sceneContext.algorithmAliasProperty().addListener((obs, old, newVal) -> {setSelectedRasterizer(newVal);});
        sceneContext.controllerAliasProperty().addListener((obs, old, newVal) -> {showSidebarSections(newVal, sceneContext.getAlgorithmAlias());});
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
    private void setSelectedScene(SceneAlias alias) {
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
            if (currentAlgorithmSectionController instanceof Algorithm3DSectionControllerMain controller) {
                algorithm3DSectionController = controller;
                controller.initSceneContext(sceneContext);
            } else {
                algorithmToggle = currentAlgorithmSectionController.getToggleGroup();
                addListenerToToggleGroup(algorithmToggle,
                        (AlgorithmAlias a) -> sceneContext.setAlgorithmAlias(a), algorithmAlias);
            }

            Parent rootSettingsSection = settingsLoader.load();
            ISidebarSectionController currentSettingsSectionController = settingsLoader.getController();
            settingsBoxPlaceholder.getChildren().setAll(rootSettingsSection);

            // Předání SceneContext section controllerům které ho podporují
            if (currentSettingsSectionController instanceof MainUIController uiCtrl) {
                uiCtrl.initSceneContext(sceneContext);
                if (currentSettingsSectionController instanceof Settings3DSectionControllerMain s3d) {
                    settings3DSectionController = s3d;
                }
            }

            // CLIP settings: orientace polygonu přes ToggleGroup
            if (algorithmControllerAlias == AlgorithmControllerAlias.CLIP) {
                ToggleGroup settingsToggle = currentSettingsSectionController.getToggleGroup();
                if (settingsToggle != null) {
                    settingsToggle.selectedToggleProperty().addListener((obs, old, newToggle) -> {
                        if (newToggle instanceof ToggleButton btn && btn.getUserData() != null) {
                            try {
                                PolygonOrientation o = PolygonOrientation.valueOf(btn.getUserData().toString());
                                sceneContext.setPolygonOrientation(o);
                            } catch (IllegalArgumentException ignored) {}
                        }
                    });
                }
            }
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

    public void reset3DSettings() {
        algorithm3DSectionController.resetCubic();
        settings3DSectionController.resetSettings();
    }
}
