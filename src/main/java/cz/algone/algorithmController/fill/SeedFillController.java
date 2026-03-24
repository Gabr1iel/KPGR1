package cz.algone.algorithmController.fill;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.SceneContext;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.transforms.Col;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class SeedFillController implements IAlgorithmController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.SEED_FILL_BACKGROUND;
    private ImageBuffer imageBuffer;
    private SceneContext sceneContext;
    private IFill<Model> fillAlgorithm;
    private ColorPair color;

    @Override
    public void initListeners() {
        imageBuffer.getCanvas().setOnMousePressed(e -> {
           if (color == null) return;
           Point point = new Point((int) e.getX(), (int) e.getY());
           fillAlgorithm.fill(point, color, getBorderColor());
        });
    }

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.imageBuffer = sceneContext.getImageBuffer();
        this.sceneContext = sceneContext;
        this.fillAlgorithm = (IFill<Model>) algorithm;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.color = colors;
    }

    /** Ve scéně najde Polygon a získá jeho barvu */
    private int getBorderColor() {
        if (sceneContext.getModels().isEmpty())
            return ColorUtils.interpolateColor(ColorUtils.DEFAULT_COLORPICKER_COLOR.primary(), null,0);
        Model model = sceneContext.getModels().values().iterator().next();
        Col primary = model.getColors().primary();
        return primary.getARGB();
    }
}
