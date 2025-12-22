package cz.algone.algorithm.fill.pattern;

import cz.algone.util.color.ColorPair;

public interface IPattern {
    int colorAt(int x, int y, ColorPair color);
}
