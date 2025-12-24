package cz.algone.algorithm.clip;

import cz.algone.model.ModelType;
import cz.algone.model.Point;
import cz.algone.model.Polygon;
import cz.algone.model.SceneModel;
import cz.algone.util.color.ColorPair;

import java.util.List;

public class ClipService {
    private final SutherlandHodgmanClipper clipper = new SutherlandHodgmanClipper();

    public Polygon clip(SceneModel sceneModel, ColorPair resultColor) {
        Polygon subject = (Polygon) sceneModel.getModels().get(ModelType.POLYGON);
        Polygon clipPoly = (Polygon) sceneModel.getModels().get(ModelType.CLIP_POLYGON);

        if (subject == null || clipPoly == null) return null;
        if (subject.getPoints().size() < 3) return null;
        if (clipPoly.getPoints().size() < 5) return null;

        List<Point> outPts = clipper.clip(subject.getPoints(), clipPoly.getPoints());
        if (outPts.size() < 3) {
            sceneModel.getModels().remove(ModelType.CLIPPED_POLYGON);
            return null;
        }

        Polygon result = new Polygon(resultColor);
        for (Point p : outPts) result.addPoint(p);

        sceneModel.getModels().put(ModelType.CLIPPED_POLYGON, result);
        return result;
    }
}
