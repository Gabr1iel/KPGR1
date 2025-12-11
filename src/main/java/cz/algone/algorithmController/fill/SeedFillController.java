package cz.algone.algorithmController.fill;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.model.Model;
import cz.algone.model.SceneModel;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.scene.paint.Color;

public class SeedFillController implements IAlgorithmController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.SEED_FILL_BACKGROUND;
    private RasterCanvas raster;
    private SceneModelController sceneModelController;
    private SceneModel sceneModel;
    private IFill seedFill;
    private ColorPair color;

    @Override
    public void initListeners() {
        raster.getCanvas().setOnMousePressed(e -> {
           if (color == null) return;

           int x = (int) e.getX();
           int y = (int) e.getY();

           seedFill.fill(x, y, color, getBorderColor());
        });
    }

    @Override
    public void setup(RasterCanvas raster, IAlgorithm algorithm, SceneModelController sceneModelController) {
        this.raster = raster;
        this.sceneModelController = sceneModelController;
        this.sceneModel = sceneModelController.getSceneModel();
        this.seedFill = (IFill) algorithm;
    }

    private int getBorderColor() {
        if (sceneModel.getModels().isEmpty())
            return ColorUtils.interpolateColor(ColorUtils.DEFAULT_COLORPICKER_COLOR.primary(), null,0);
        Model model =  sceneModel.getModels().values().iterator().next();
        Color primary = model.getColors().primary();
        return ColorUtils.interpolateColor(primary, null, 0);
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.color = colors;
    }
}
