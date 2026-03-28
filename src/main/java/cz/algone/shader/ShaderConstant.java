package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;

public class ShaderConstant implements Shader {
    private final Col color;

    public ShaderConstant(Col color) {
        this.color = color;
    }

    @Override
    public Col getColor(Vertex pixel) {
        return color;
    }
}
