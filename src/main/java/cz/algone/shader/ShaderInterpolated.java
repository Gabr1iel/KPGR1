package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;

public class ShaderInterpolated implements Shader {
    @Override
    public Col getColor(Vertex pixel) {
        return pixel.getColor();
    }
}
