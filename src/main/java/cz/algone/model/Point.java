package cz.algone.model;

import cz.algone.util.color.ColorPair;

public record Point(int x, int y) implements Model {
    //ignorovaná metoda
    @Override
    public ColorPair getColors() {
        return null;
    }
}
