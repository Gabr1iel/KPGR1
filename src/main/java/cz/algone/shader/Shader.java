package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;

public interface Shader {
    Col getColor(Vertex pixel);
}
