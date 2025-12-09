package cz.algone.algorithmController.fill;

import cz.algone.algorithm.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneModelController;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;

public class SeedFillController implements IAlgorithmController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.FILL;
    private RasterCanvas raster;
    private IFill seedFill;
    private ColorPair color;
    private FillMode mode = FillMode.BACKGROUND;

    @Override
    public void initListeners() {
        raster.getCanvas().setOnMousePressed(e -> {
           if (color == null) return;

           int x = (int) e.getX();
           int y = (int) e.getY();

           seedFill.fill(x, y, color, mode);
        });
    }

    @Override
    public void setup(RasterCanvas raster, IAlgorithm algorithm, SceneModelController sceneModelController) {
        this.raster = raster;
        this.seedFill = (IFill) algorithm;
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
