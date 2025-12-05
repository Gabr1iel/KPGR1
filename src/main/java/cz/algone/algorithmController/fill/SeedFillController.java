package cz.algone.algorithmController.fill;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.FillMode;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;

public class SeedFillController implements ShapeController {
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
    public void setup(RasterCanvas raster, IAlgorithm algorithm) {
        this.raster = raster;
        this.seedFill = (IFill) algorithm;
    }

    @Override
    public void drawScene() {

    }

    @Override
    public void clearRaster() {

    }

    @Override
    public void setColors(ColorPair colors) {
        this.color = colors;
    }
}
