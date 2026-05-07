package cz.algone.model.models2D;

import cz.algone.util.color.ColorPair;

public record Point(int x, int y) implements Model {
    @Override
    public ColorPair getColors() {
        return null;
    }
}
