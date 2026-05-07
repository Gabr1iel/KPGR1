package cz.algone.algorithm.clip;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.fill.scanline.ScanlineFill;
import cz.algone.algorithm.rasterizer.polygon.PolygonRasterizer;
import cz.algone.common.enumAlias.ModelType;
import cz.algone.model.models2D.Point;
import cz.algone.model.models2D.Polygon;
import cz.algone.model.models2D.Model;

import java.util.Map;
import cz.algone.raster.ImageBuffer;
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
    /** Ořeže subject polygon clip polygonem a uloží výsledek do mapy modelů. */
    public void clip(Map<ModelType, Model> models, ColorPair resultColor) {
        Polygon subject = (Polygon) models.get(ModelType.POLYGON);
        Polygon clipPoly = (Polygon) models.get(ModelType.CLIP_POLYGON);

        List<Point> outPts = clipper.clip(subject.getPoints(), clipPoly.getPoints());
        if (outPts.size() < 3) {
            models.remove(ModelType.CLIPPED_POLYGON);
            return;
        }

        Polygon result = new Polygon(resultColor);
        for (Point p : outPts) result.addPoint(p);

        models.put(ModelType.CLIPPED_POLYGON, result);
    }

    public PolygonRasterizer getPolygonRasterizer() {
        return polygonRasterizer;
    }
    public ScanlineFill getScanlineFill() {
        return scanlineFill;
    }

    @Override
    public void setup(ImageBuffer raster) {}
}
