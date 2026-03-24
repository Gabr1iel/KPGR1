package cz.algone.algorithmController.fill;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.model.SceneContext;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Polygon;
import cz.algone.transforms.Col;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;

public class ScanlineFillController implements IAlgorithmController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.SCANLINE_FILL;
    private ImageBuffer imageBuffer;
    private SceneContext sceneContext;
    private IFill<Model> fillAlgorithm;
    private ColorPair color;

    @Override
    public void initListeners() {
        imageBuffer.getCanvas().setOnMousePressed(e -> {
            if (color == null) return;
            Polygon polygon = (Polygon) sceneContext.getModels().get(ModelType.POLYGON);
            if (polygon == null) return;
            fillAlgorithm.fill(polygon, color, getBorderColor());
        });
    }

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.imageBuffer = sceneContext.getImageBuffer();
        this.sceneContext = sceneContext;
        this.fillAlgorithm = (IFill<Model>) algorithm;
    }
    /** Ve scéně najde Polygon a získá jeho barvu */
    private int getBorderColor() {
        if (sceneContext.getModels().isEmpty())
            return ColorUtils.interpolateColor(ColorUtils.DEFAULT_COLORPICKER_COLOR.primary(), null,0);
        Model model = sceneContext.getModels().values().iterator().next();
        Col primary = model.getColors().primary();
        return primary.getARGB();
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

