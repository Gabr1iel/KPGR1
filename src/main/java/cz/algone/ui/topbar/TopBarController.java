package cz.algone.ui.topbar;

import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.common.enumAlias.RightTabAlias;
import cz.algone.common.enumAlias.SceneAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/** Horní lišta — přepínač pracovních prostorů, záložek pravého panelu a globálních akcí. */
public class TopBarController extends MainUIController {
    @FXML private ToggleGroup workspaceToggle;
    @FXML private ToggleGroup rightTabToggle;
    @FXML private ToggleButton algorithmTabBtn;
    @FXML private ToggleButton controlsBtn;

    @Override
    protected void onSceneContextReady() {
        ToggleBinding.bindGroup(workspaceToggle, SceneAlias.class, sceneContext.sceneProperty());
        ToggleBinding.bindGroup(rightTabToggle, RightTabAlias.class, sceneContext.rightTabProperty());

        sceneContext.controllerAliasProperty().addListener((obs, old, alias) -> showTabLabel(alias));
        showTabLabel(sceneContext.getControllerAlias());
    }

    /** Ve 3D nastavuje pravý panel vybrané těleso, jinde zvolený algoritmus. */
    private void showTabLabel(AlgorithmControllerAlias alias) {
        algorithmTabBtn.setText(alias == AlgorithmControllerAlias.CONTROLLER_3D ? "Těleso" : "Algoritmus");
    }

    /** Stav tlačítka Ovládání — naváže se na viditelnost panelu s klávesovými zkratkami. */
    public BooleanProperty controlsSelectedProperty() {
        return controlsBtn.selectedProperty();
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("O aplikaci");
        alert.setHeaderText("Algone");
        alert.setContentText("""
                Výuková aplikace pro demonstraci algoritmů počítačové grafiky.

                2D: rasterizace čar a tvarů, vyplňování, ořezávání polygonů.
                3D: tělesa, parametrické kubiky, osvětlení, projekce a ořezání.""");
        alert.showAndWait();
    }
}
