package cz.algone.ui.panel;

import cz.algone.ui.MainUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Předek panelů, které načítají svůj obsah z FXML podle aktuálního stavu.
 *  Jednou načtený panel si drží — jeho controller tak zůstává navázaný na
 *  {@link cz.algone.model.SceneContext} a při dalším zobrazení se nenačítá znovu. */
public abstract class PanelHostController extends MainUIController {
    private final Map<String, Parent> loadedPanels = new HashMap<>();
    private String shownResource;

    /** Zobrazí panel z FXML a jeho controlleru předá {@link cz.algone.model.SceneContext}.
     *  Pokud soubor neexistuje, použije se fallback. */
    protected void showPanel(Pane container, String resource, String fallbackResource) {
        if (resource.equals(shownResource)) return;
        shownResource = resource;

        Parent root = loadedPanels.computeIfAbsent(resource, path -> load(path, fallbackResource));
        container.getChildren().setAll(root);
    }

    private Parent load(String resource, String fallbackResource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            if (loader.getLocation() == null)
                loader = new FXMLLoader(getClass().getResource(fallbackResource));

            Parent root = loader.load();
            if (loader.getController() instanceof MainUIController controller)
                controller.initSceneContext(sceneContext);

            return root;
        } catch (IOException e) {
            throw new RuntimeException("Nepodařilo se načíst panel " + resource, e);
        }
    }

    protected void clearPanel(Pane container) {
        shownResource = null;
        container.getChildren().clear();
    }
}
