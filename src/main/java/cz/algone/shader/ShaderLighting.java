package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;

/** Dekorátor shaderu, který násobí výslednou barvu barvou světla a intenzitou vrcholu. */
public class ShaderLighting implements Shader {
    private final Shader base;
    private final Col lightColor;

    public ShaderLighting(Shader base, Col lightColor) {
        this.base = base;
        this.lightColor = lightColor;
    }

    @Override
    public Col getColor(Vertex pixel) {
        Col baseColor = base.getColor(pixel);
        double i = pixel.getIntensity();
        return new Col(
                baseColor.getR() * lightColor.getR() * i,
                baseColor.getG() * lightColor.getG() * i,
                baseColor.getB() * lightColor.getB() * i
        );
    }
}
