package cz.algone.ui.panel;

import cz.algone.common.enumAlias.CategoryAlias;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Levý panel — obsah odpovídá kategorii zvolené v railu. */
public class LeftPanelController extends PanelHostController {
    private static final String BASE = "/cz/algone/views/panels/left/";
    private static final String EMPTY = BASE + "EMPTY.fxml";

    @FXML private Label panelTitle;
    @FXML private VBox panelContent;

    @Override
    protected void onSceneContextReady() {
        sceneContext.categoryProperty().addListener((obs, old, category) -> showCategory(category));
        showCategory(sceneContext.getCategory());
    }

    private void showCategory(CategoryAlias category) {
        if (category == null) {
            panelTitle.setText("");
            clearPanel(panelContent);
            return;
        }
        panelTitle.setText(category.getLabel());
        showPanel(panelContent, BASE + category.name() + ".fxml", EMPTY);
    }
}
