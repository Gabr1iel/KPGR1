package cz.algone.raster;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.controller3D.Controller3D;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.common.enumAlias.AlgorithmControllerAlias;
import cz.algone.model.SceneModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RasterController {
    @FXML private Canvas canvas;
    @FXML private StackPane stackPane;
    @FXML private Label statusLabel;
    @FXML private VBox controlsPanel;

    private ImageBuffer imageBuffer;
    private ZBuffer zBuffer;
    private boolean resizePending = false;
    private IAlgorithmController algorithmController;

    private final SceneModel sceneModel = new SceneModel();
    private SceneModelController sceneModelController;

    @FXML
    private void initialize() {
        imageBuffer = new ImageBuffer(canvas);
        zBuffer = new ZBuffer(imageBuffer);
        stackPane.setMinSize(0, 0);
        sceneModelController = new SceneModelController(imageBuffer, zBuffer, sceneModel);
        statusLabel.textProperty().bind(sceneModelController.getRasterStatus());

        //Velikost rasteru se určí podle velikosti StackPane
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());

        //Přepočítání rasteru při změně velikosti
        canvas.widthProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> resizeRaster());
    }
    /** Zobrazí/skryje controlos nápovědu */
    @FXML
    private void toggleControls() {
        boolean show = !controlsPanel.isVisible();
        controlsPanel.setVisible(show);
        controlsPanel.setManaged(show);
    }

    /** Nastaví {@link IAlgorithmController} a {@link IAlgorithm} rasteru,
     *  zároveň jim předává i raster a sceneModel */
    public void setAlgorithmController(AlgorithmControllerAlias alias, IAlgorithmController algorithmController, IAlgorithm algorithm) {
        imageBuffer.clearListeners();
        algorithm.setup(imageBuffer);
        algorithmController.setup(algorithm, sceneModelController);
        algorithmController.initListeners();
        showControls(alias);
        this.algorithmController = algorithmController;
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

            if (algorithmController instanceof ShapeController shapeController)
                shapeController.drawScene();
            else if (algorithmController instanceof Controller3D controller3D)
                controller3D.create3DSpace();
        });
    }
    /** Podle {@link AlgorithmControllerAlias} přepne controls sekci pro daný Controller*/
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

    public SceneModelController getSceneModelController() {
        return sceneModelController;
    }
}
