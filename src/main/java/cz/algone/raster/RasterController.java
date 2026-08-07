package cz.algone.raster;

import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.common.enumAlias.SceneAlias;
import cz.algone.ui.MainUIController;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class RasterController extends MainUIController {
    /** Musí odpovídat @radius-lg ve stylech, aby zaoblení karty a ořez plátna seděly. */
    private static final double CARD_RADIUS = 12;

    @FXML private Canvas canvas;
    @FXML private StackPane stackPane;
    @FXML private Label statusLabel;
    @FXML private VBox controlsPanel;

    private ImageBuffer imageBuffer;
    private ZBuffer zBuffer;
    private boolean resizePending = false;

    @FXML
    private void initialize() {
        imageBuffer = new ImageBuffer(canvas);
        zBuffer = new ZBuffer(imageBuffer);
        stackPane.setMinSize(0, 0);
        controlsPanel.managedProperty().bind(controlsPanel.visibleProperty());

        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        canvas.widthProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> resizeRaster());

        clipToCardShape();
    }

    /** Ořeže kreslicí plochu do zaoblení karty, aby vykreslené pixely nepřetékaly přes rohy. */
    private void clipToCardShape() {
        Rectangle clip = new Rectangle();
        clip.setArcWidth(CARD_RADIUS * 2);
        clip.setArcHeight(CARD_RADIUS * 2);
        clip.widthProperty().bind(stackPane.widthProperty());
        clip.heightProperty().bind(stackPane.heightProperty());
        stackPane.setClip(clip);
    }

    @Override
    protected void onSceneContextReady() {
        sceneContext.setRenderingInfra(imageBuffer, zBuffer);
        sceneContext.controllerAliasProperty().addListener((obs, old, newVal) -> showControls(newVal));
        sceneContext.sceneProperty().addListener((obs, old, newVal) -> showBackgroundFor(newVal));
        statusLabel.textProperty().bind(sceneContext.rasterStatusProperty());
        showBackgroundFor(sceneContext.getScene());
    }

    /** Canvas je průhledný, pozadí kreslicí plochy proto určuje styl podle prostoru.
     *  Obecné scénu nemění, takže si ponechá pozadí toho prostoru, ze kterého se do něj přišlo. */
    private void showBackgroundFor(SceneAlias scene) {
        if (scene == SceneAlias.GENERAL) return;
        stackPane.getStyleClass().removeAll("raster-2d", "raster-3d");
        stackPane.getStyleClass().add(scene == SceneAlias.SCENE_3D ? "raster-3d" : "raster-2d");
    }

    /** Viditelnost panelu s klávesovými zkratkami — řídí ji tlačítko Ovládání v horní liště. */
    public BooleanProperty controlsVisibleProperty() {
        return controlsPanel.visibleProperty();
    }

    public void resizeRaster() {
        if (imageBuffer == null || resizePending) return;
        resizePending = true;

        javafx.application.Platform.runLater(() -> {
            resizePending = false;

            int w = (int) Math.floor(canvas.getWidth());
            int h = (int) Math.floor(canvas.getHeight());
            if (w < 2 || h < 2) return;

            imageBuffer.resize(w, h);
            zBuffer.resize(w, h);

            if (sceneContext.getCurrentAlgorithmController() instanceof ShapeController shapeController)
                shapeController.drawScene();
            else if (sceneContext.getCurrentAlgorithmController() instanceof Controller3D controller3D)
                controller3D.create3DSpace();
        });
    }
    /** Načte a zobrazí FXML panel ovládání odpovídající danému {@link AlgorithmControllerAlias}u. */
    private void showControls(AlgorithmControllerAlias alias) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/algone/views/controls/" + alias.name() + ".fxml"));
            if (loader.getLocation() == null)
                loader = new FXMLLoader(getClass().getResource("/cz/algone/views/controls/EMPTY.fxml"));

            Parent root = loader.load();
            controlsPanel.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRasterLabel(boolean show) {
        statusLabel.setVisible(show);
        statusLabel.setManaged(show);
    }
}
