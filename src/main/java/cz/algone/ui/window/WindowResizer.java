package cz.algone.ui.window;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/** Změna velikosti okna tažením za jeho okraje. Okno bez systémového rámu si ji musí řešit samo. */
public final class WindowResizer {
    /** Šířka citlivého pásu u okraje okna. Leží v odsazení kolem shellu, takže nekoliduje s obsahem. */
    private static final int EDGE = 6;

    private final Stage stage;
    private final Region root;

    private boolean left, right, top, bottom;
    private double startScreenX, startScreenY;
    private double startX, startY, startWidth, startHeight;

    private WindowResizer(Stage stage, Region root) {
        this.stage = stage;
        this.root = root;
    }

    public static void install(Stage stage, Region root) {
        WindowResizer resizer = new WindowResizer(stage, root);
        root.addEventFilter(MouseEvent.MOUSE_MOVED, resizer::onMoved);
        root.addEventFilter(MouseEvent.MOUSE_PRESSED, resizer::onPressed);
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, resizer::onDragged);
        root.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> resizer.clearEdges());
    }

    /** Podle blízkosti k okraji nastaví kurzor a zapamatuje si, které hrany se budou táhnout. */
    private void onMoved(MouseEvent e) {
        if (stage.isMaximized()) {
            root.setCursor(Cursor.DEFAULT);
            clearEdges();
            return;
        }

        double x = e.getSceneX();
        double y = e.getSceneY();

        left = x < EDGE;
        right = x > root.getWidth() - EDGE;
        top = y < EDGE;
        bottom = y > root.getHeight() - EDGE;

        root.setCursor(cursorForEdges());
    }

    private Cursor cursorForEdges() {
        if (top && left) return Cursor.NW_RESIZE;
        if (top && right) return Cursor.NE_RESIZE;
        if (bottom && left) return Cursor.SW_RESIZE;
        if (bottom && right) return Cursor.SE_RESIZE;
        if (left) return Cursor.W_RESIZE;
        if (right) return Cursor.E_RESIZE;
        if (top) return Cursor.N_RESIZE;
        if (bottom) return Cursor.S_RESIZE;
        return Cursor.DEFAULT;
    }

    private void onPressed(MouseEvent e) {
        if (!isResizing()) return;

        startScreenX = e.getScreenX();
        startScreenY = e.getScreenY();
        startX = stage.getX();
        startY = stage.getY();
        startWidth = stage.getWidth();
        startHeight = stage.getHeight();
        e.consume();
    }

    private void onDragged(MouseEvent e) {
        if (!isResizing()) return;

        double dx = e.getScreenX() - startScreenX;
        double dy = e.getScreenY() - startScreenY;

        if (right) stage.setWidth(Math.max(stage.getMinWidth(), startWidth + dx));
        if (bottom) stage.setHeight(Math.max(stage.getMinHeight(), startHeight + dy));

        // Tažení za levou/horní hranu musí kromě velikosti posunout i počátek okna
        if (left) {
            double width = Math.max(stage.getMinWidth(), startWidth - dx);
            stage.setX(startX + startWidth - width);
            stage.setWidth(width);
        }
        if (top) {
            double height = Math.max(stage.getMinHeight(), startHeight - dy);
            stage.setY(startY + startHeight - height);
            stage.setHeight(height);
        }
        e.consume();
    }

    private boolean isResizing() {
        return left || right || top || bottom;
    }

    private void clearEdges() {
        left = right = top = bottom = false;
    }
}
