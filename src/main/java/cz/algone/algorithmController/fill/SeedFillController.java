package cz.algone.algorithmController.fill;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.IFill;
import cz.algone.algorithmController.IAlgorithmController;
import cz.algone.algorithmController.scene.SceneContext;
import cz.algone.model.*;
import cz.algone.model.models2D.Model;
import cz.algone.model.models2D.Point;
import cz.algone.raster.ImageBuffer;
import cz.algone.util.color.ColorPair;
import cz.algone.util.color.ColorUtils;
import javafx.scene.paint.Color;

public class SeedFillController implements IAlgorithmController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.SEED_FILL_BACKGROUND;
    private ImageBuffer imageBuffer;
    private SceneModel sceneModel;
    private IFill fillAlgorithm;
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
        this.sceneModel = sceneContext.getSceneModel();
        this.fillAlgorithm = (IFill) algorithm;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.color = colors;
    }

    /** v {@link SceneModel} najde Polygon a získá jeho barvu */
    private int getBorderColor() {
        if (sceneModel.getModels().isEmpty())
            return ColorUtils.interpolateColor(ColorUtils.DEFAULT_COLORPICKER_COLOR.primary(), null,0);
        Model model =  sceneModel.getModels().values().iterator().next();
        Color primary = model.getColors().primary();
        return ColorUtils.interpolateColor(primary, null, 0);
    }
}
