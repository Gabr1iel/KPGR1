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

        setSelectedScene(sceneContext.getScene());
    }
    /** Přepíná viditelnost dropdown sekcí sidebaru podle stavu jejich tlačítek. */
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

    /** Označí ToggleButton odpovídající danému {@link AlgorithmAlias}u. */
    public void setSelectedRasterizer(AlgorithmAlias alias) {
        if (algorithmToggle == null) return;
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
    /** Označí ToggleButton odpovídající danému {@link SceneAlias}u. */
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
    /** Vrací ikonku šipky uvnitř daného tlačítka, nebo null pokud neexistuje. */
    private Polygon findArrow(ToggleButton btn) {
        Node g = btn.getGraphic();
        if (!(g instanceof Parent p)) return null;

        return (Polygon) p.lookup(".arrow-icon");
    }
    /** Sváže managed property sekcí sidebaru s jejich visible property. */
    private void bindManagedProperties() {
        algorithmBox.managedProperty().bind(algorithmBox.visibleProperty());
        sceneBox.managedProperty().bind(sceneBox.visibleProperty());
        settingsBox.managedProperty().bind(settingsBox.visibleProperty());
    }

    /** Načte FXML sekce sidebaru pro daný controller a algoritmus a inicializuje jejich controllery. */
    public void showSidebarSections(AlgorithmControllerAlias algorithmControllerAlias, AlgorithmAlias algorithmAlias) {
        try {
            FXMLLoader algorithmLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/algorithmsSection/" + algorithmControllerAlias.name() + ".fxml"));
            if (algorithmLoader.getLocation() == null)
                algorithmLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/algorithmsSection/EMPTY.fxml"));
            FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/settingsSection/" + algorithmControllerAlias.name() + ".fxml"));
            if (settingsLoader.getLocation() == null)
                settingsLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/sidebar/settingsSection/EMPTY.fxml"));

            Parent algorithmRoot = algorithmLoader.load();
            Parent settingsRoot = settingsLoader.load();
            algorithmBoxPlaceholder.getChildren().setAll(algorithmRoot);
            settingsBoxPlaceholder.getChildren().setAll(settingsRoot);

            ISidebarSectionController algorithmSectionController = algorithmLoader.getController();
            ISidebarSectionController settingsSectionController = settingsLoader.getController();

            algorithmToggle = algorithmSectionController.getToggleGroup();
            if (algorithmSectionController instanceof Algorithm3DSectionControllerMain c) algorithm3DSectionController = c;
            if (settingsSectionController instanceof Settings3DSectionControllerMain c) settings3DSectionController = c;

            if (algorithmSectionController instanceof MainUIController uiCtrl) uiCtrl.initSceneContext(sceneContext);
            if (settingsSectionController instanceof MainUIController uiCtrl) uiCtrl.initSceneContext(sceneContext);

            if (algorithmAlias != null) setSelectedRasterizer(algorithmAlias);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /** Registruje listener, který při změně vybraného toggle předá Consumeru odpovídající {@link IAlias}. */
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
