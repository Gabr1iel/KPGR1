package cz.algone.ui.window;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

/** Vlastní titulková lišta okna bez systémového rámu — tažení, tlačítka a stav maximalizace. */
public class TitleBarController {
    private static final String MAXIMIZE_ICON = "M6 6 H18 V18 H6 Z";
    private static final String RESTORE_ICON = "M8.5 8.5 V6 H18 V15.5 H15.5 M6 10 H15.5 V19.5 H6 Z";

    @FXML private SVGPath maximizeIcon;

    private Stage stage;
    private double dragOffsetX;
    private double dragOffsetY;

    public void initStage(Stage stage) {
        this.stage = stage;
        stage.maximizedProperty().addListener((obs, old, maximized) -> showMaximizeIcon(maximized));
        showMaximizeIcon(stage.isMaximized());
    }

    private void showMaximizeIcon(boolean maximized) {
        maximizeIcon.setContent(maximized ? RESTORE_ICON : MAXIMIZE_ICON);
    }

    @FXML
    private void onPressed(MouseEvent e) {
        dragOffsetX = e.getSceneX();
        dragOffsetY = e.getSceneY();
    }

    /** Tažením se okno posouvá; z maximalizovaného stavu se nejdřív vrátí pod kurzor. */
    @FXML
    private void onDragged(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;

        if (stage.isMaximized()) {
            double ratio = dragOffsetX / stage.getWidth();
            stage.setMaximized(false);
            dragOffsetX = ratio * stage.getWidth();
        }
        stage.setX(e.getScreenX() - dragOffsetX);
        stage.setY(e.getScreenY() - dragOffsetY);
    }

    @FXML
    private void onClicked(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) toggleMaximize();
    }

    @FXML
    private void minimize() {
        stage.setIconified(true);
    }

    @FXML
    private void toggleMaximize() {
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    private void close() {
        stage.close();
    }
}
