package cz.algone.ui.rail;

import cz.algone.common.enumAlias.CategoryAlias;
import cz.algone.common.enumAlias.SceneAlias;
import cz.algone.ui.MainUIController;
import cz.algone.ui.panel.common.ToggleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Levý rail — rozcestník kategorií. Nabízené kategorie určuje aktuální pracovní prostor. */
public class RailController extends MainUIController {
    @FXML private VBox generalCategories;
    @FXML private VBox categories2D;
    @FXML private VBox categories3D;
    @FXML private ToggleGroup categoryToggle;

    @FXML
    private void initialize() {
        bindManaged(generalCategories);
        bindManaged(categories2D);
        bindManaged(categories3D);
        markPlaceholders();
    }

    @Override
    protected void onSceneContextReady() {
        ToggleBinding.bindGroup(categoryToggle, CategoryAlias.class, sceneContext.categoryProperty());
        sceneContext.sceneProperty().addListener((obs, old, scene) -> showCategoriesFor(scene));
        showCategoriesFor(sceneContext.getScene());
    }

    /** Zobrazí skupinu kategorií patřící danému prostoru. */
    private void showCategoriesFor(SceneAlias scene) {
        generalCategories.setVisible(scene == SceneAlias.GENERAL);
        categories2D.setVisible(scene == SceneAlias.SCENE_2D);
        categories3D.setVisible(scene == SceneAlias.SCENE_3D);
    }

    /** Zakáže tlačítka kategorií, které jsou zatím jen placeholder. */
    private void markPlaceholders() {
        for (Toggle toggle : categoryToggle.getToggles()) {
            if (!(toggle instanceof ToggleButton btn) || btn.getUserData() == null) continue;
            try {
                CategoryAlias category = CategoryAlias.valueOf(btn.getUserData().toString());
                if (category.isImplemented()) continue;
                btn.setDisable(true);
                btn.setTooltip(new Tooltip("Zatím neimplementováno"));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private void bindManaged(VBox box) {
        box.managedProperty().bind(box.visibleProperty());
    }
}
