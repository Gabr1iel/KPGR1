package cz.algone.ui.panel;

import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.common.enumAlias.RightTabAlias;
import cz.algone.common.enumAlias.SceneAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Pravý panel — podle záložky ukazuje nastavení scény, nebo nastavení aktivní věci
 *  (ve 2D zvoleného algoritmu, ve 3D vybraného tělesa). */
public class RightPanelController extends PanelHostController {
    private static final String SCENE_BASE = "/cz/algone/views/panels/right/scene/";
    private static final String ALGORITHM_BASE = "/cz/algone/views/panels/right/algorithm/";
    private static final String EMPTY = "/cz/algone/views/panels/right/EMPTY.fxml";

    @FXML private VBox root;
    @FXML private Label panelTitle;
    @FXML private VBox panelContent;

    @Override
    protected void onSceneContextReady() {
        // Bez zvolené záložky je panel zavřený a místo připadne plátnu
        root.managedProperty().bind(root.visibleProperty());
        root.visibleProperty().bind(sceneContext.rightTabProperty().isNotNull());

        sceneContext.rightTabProperty().addListener((obs, old, tab) -> showContent());
        sceneContext.sceneProperty().addListener((obs, old, scene) -> showContent());
        sceneContext.controllerAliasProperty().addListener((obs, old, alias) -> showContent());
        showContent();
    }

    @FXML
    private void closePanel() {
        sceneContext.setRightTab(null);
    }

    private void showContent() {
        RightTabAlias tab = sceneContext.getRightTab();
        if (tab == null) return;

        if (tab == RightTabAlias.ALGORITHM) showAlgorithmSettings();
        else showSceneSettings();
    }

    private void showSceneSettings() {
        SceneAlias scene = sceneContext.getScene();
        panelTitle.setText("Nastavení scény");
        showPanel(panelContent, SCENE_BASE + scene.name() + ".fxml", EMPTY);
    }

    /** Titulek se řídí aktivním controllerem, ne prostorem — v Obecném zůstává scéna beze změny. */
    private void showAlgorithmSettings() {
        AlgorithmControllerAlias alias = sceneContext.getControllerAlias();
        boolean is3D = alias == AlgorithmControllerAlias.CONTROLLER_3D;

        panelTitle.setText(is3D ? "Nastavení tělesa" : "Nastavení algoritmu");
        showPanel(panelContent, ALGORITHM_BASE + (alias == null ? "EMPTY" : alias.name()) + ".fxml", EMPTY);
    }
}
