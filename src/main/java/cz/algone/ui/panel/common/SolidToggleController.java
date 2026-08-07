package cz.algone.ui.panel.common;

import cz.algone.common.enumAlias.SolidAlias;
import cz.algone.model.models3D.Solid;
import cz.algone.ui.MainUIController;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/** Přidávání a odebírání těles ze scény. Tlačítka nese kontejner {@code solidButtons},
 *  hodnotu určuje jejich userData ({@link SolidAlias}). Stav tlačítek sleduje scénu,
 *  takže se po jejím vyčištění samy odznačí. */
public class SolidToggleController extends MainUIController {
    @FXML protected Pane solidButtons;

    private final List<ToggleButton> buttons = new ArrayList<>();
    private boolean syncing = false;

    @Override
    protected void onSceneContextReady() {
        collectButtons();
        sceneContext.getSolids().addListener((MapChangeListener<SolidAlias, Solid>) change -> syncFromScene());
        syncFromScene();
        onSolidsReady();
    }

    /** Hook pro potomky, kteří potřebují navázat další prvky panelu. */
    protected void onSolidsReady() {}

    private void collectButtons() {
        if (solidButtons == null) return;
        for (Node node : solidButtons.getChildren()) {
            if (!(node instanceof ToggleButton btn) || btn.getUserData() == null) continue;

            SolidAlias alias = aliasOf(btn);
            if (alias == null) continue;

            buttons.add(btn);
            btn.selectedProperty().addListener((obs, old, selected) -> {
                if (syncing) return;
                sceneContext.toggleSolids(alias, selected);
            });
        }
    }

    /** Sjednotí stav tlačítek s tělesy skutečně přítomnými ve scéně. */
    private void syncFromScene() {
        syncing = true;
        try {
            for (ToggleButton btn : buttons) {
                SolidAlias alias = aliasOf(btn);
                btn.setSelected(alias != null && sceneContext.getSolids().containsKey(alias));
            }
        } finally {
            syncing = false;
        }
    }

    private SolidAlias aliasOf(ToggleButton btn) {
        try {
            return SolidAlias.valueOf(btn.getUserData().toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
