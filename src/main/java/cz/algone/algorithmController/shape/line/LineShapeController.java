package cz.algone.algorithmController.shape.line;

import cz.algone.common.enumAlias.AlgorithmAlias;
import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.SceneContext;
import cz.algone.algorithmController.shape.ShapeController;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Line;
import cz.algone.model.models2D.Model;
import cz.algone.algorithm.rasterizer.IRasterizer;
import cz.algone.util.color.ColorPair;
import javafx.scene.canvas.Canvas;

public class LineShapeController implements ShapeController {
    private final AlgorithmAlias DEFAULT_ALGORITHM = AlgorithmAlias.BRESENHAM;
    private final ModelType DEFAULT_MODELTYPE = ModelType.LINE;
    private Canvas canvas;
    private SceneContext sceneContext;
    private IRasterizer<Line> rasterizer;
    private Line line;
    private ColorPair colors;

    @Override
    public void setup(IAlgorithm algorithm, SceneContext sceneContext) {
        this.rasterizer = (IRasterizer<Line>) algorithm;
        this.canvas = sceneContext.getImageBuffer().getCanvas();
        this.sceneContext = sceneContext;
        this.line = null;
    }

    @Override
    public void initListeners() {
        canvas.setOnMousePressed(e -> line = new Line((int) e.getX(), (int) e.getY(), (int) e.getX(), (int) e.getY(), colors));
        canvas.setOnMouseDragged(e -> {
            if (line == null) {return;}
            if (e.isShiftDown())
                rasterizeLineWithShift((int) e.getX(), (int) e.getY());
            else {
                line.setX2((int) e.getX());
                line.setY2((int) e.getY());
            }
            drawScene();
        });
        canvas.setOnMouseReleased(e -> {
            if (line == null) {return;}
            if (!e.isShiftDown()) {
                line.setX2((int) e.getX());
                line.setY2((int) e.getY());
            }
            drawScene();
        });
    }
    /** Zarovná druhý bod čáry na nejbližší vodorovnou, svislou nebo 45° osu. */
    public void rasterizeLineWithShift(int x2, int y2) {
        int lengthX = Math.abs(x2 - line.getX1());
        int lengthY = Math.abs(y2 - line.getY1());
        double ratio = Math.abs(lengthX / lengthY);

        if (ratio > 2) {
            y2 = line.getY1();
        } else if (ratio < 0.5) {
            x2 = line.getX1();
        } else {
            int signX = (x2 - line.getX1()) < 0 ? -1 : 1;
            int signY = (y2 - line.getY1()) < 0 ? -1 : 1;
            int d = Math.min(lengthX, lengthY);
            x2 = line.getX1() + signX * d;
            y2 = line.getY1() + signY * d;
        }
        line.setX2(x2);
        line.setY2(y2);
    }

    @Override
    public void drawScene() {
        sceneContext.clearRasterAndScene();
        line = (Line) updateModel();
        if (line != null)
            rasterizer.rasterize(line);
    }

    @Override
    public Model updateModel() {
        sceneContext.getModels().put(DEFAULT_MODELTYPE, line);
        return line;
    }

    @Override
    public AlgorithmAlias getDefaultAlgorithm() {
        return DEFAULT_ALGORITHM;
    }

    @Override
    public void setColors(ColorPair colors) {
        this.colors = colors;
    }


}
