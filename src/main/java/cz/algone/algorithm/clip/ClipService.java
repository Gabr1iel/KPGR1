package cz.algone.algorithm.clip;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.model.ModelType;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.model.SceneModel;
import cz.algone.raster.RasterCanvas;
import cz.algone.util.color.ColorPair;

import java.util.List;

public class ClipService implements IAlgorithm {
    private final SutherlandHodgmanClipper clipper;
    private final ScanlineFill scanlineFill;
    private final PolygonRasterizer polygonRasterizer;

    public ClipService(SutherlandHodgmanClipper clipper, ScanlineFill scanlineFill, PolygonRasterizer polygonRasterizer ) {
        this.clipper = clipper;
        this.scanlineFill = scanlineFill;
        this.polygonRasterizer = polygonRasterizer;
    }
    /** Použije metodu {@link SutherlandHodgmanClipper#clip}
     * a výsledný clipper polygon zkontroluje ->
     *pokud počet bodů menší jak 2 tak nedělá nic */
    public void clip(SceneModel sceneModel, ColorPair resultColor) {
        Polygon subject = (Polygon) sceneModel.getModels().get(ModelType.POLYGON);
        Polygon clipPoly = (Polygon) sceneModel.getModels().get(ModelType.CLIP_POLYGON);

        List<Point> outPts = clipper.clip(subject.getPoints(), clipPoly.getPoints());
        if (outPts.size() < 3) {
            sceneModel.getModels().remove(ModelType.CLIPPED_POLYGON);
            return;
        }

        Polygon result = new Polygon(resultColor);
        for (Point p : outPts) result.addPoint(p);

        sceneModel.getModels().put(ModelType.CLIPPED_POLYGON, result);
    }

    public PolygonRasterizer getPolygonRasterizer() {
        return polygonRasterizer;
    }
    public ScanlineFill getScanlineFill() {
        return scanlineFill;
    }

    //Ignorovaná metoda
    @Override
    public void setup(RasterCanvas raster) {}
}
