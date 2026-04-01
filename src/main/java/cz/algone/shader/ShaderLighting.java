package cz.algone.shader;

import cz.algone.objectData.Vertex;
import cz.algone.transforms.Col;

/** Dekorátor shaderu přidávající osvětlení (Gouraud).
 *  Intensity je předpočítaná per-vertex a interpolovaná rasterizérem.
 *  finalColor = baseColor * lightColor * intensity */
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
        // Všechny faktory jsou v [0,1] → výsledek taky, saturate není potřeba
        return new Col(
                baseColor.getR() * lightColor.getR() * i,
                baseColor.getG() * lightColor.getG() * i,
                baseColor.getB() * lightColor.getB() * i
        );
    }
}
