package cz.algone.algorithm.clip;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.model.Point;
import java.util.List;

public interface IClipper extends IAlgorithm {
    List<Point> clip(List<Point> subject, List<Point> clipper);
}
