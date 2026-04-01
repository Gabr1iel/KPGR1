package cz.algone.util;

import cz.algone.objectData.Vectorizable;

public class Lerp<E extends Vectorizable<E>> {
    public E lerp(E a, E b, double t) {
        return a.mul(1 - t).add(b.mul(t));
    }
}
