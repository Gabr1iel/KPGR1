package cz.algone.algorithm.algorithm3D;

import cz.algone.algorithm.IAlgorithm;
import cz.algone.algorithm.clip3D.Clipper3D;
import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenhamZ;
import cz.algone.algorithm.rasterizer.triangle.TriangleRasterizer;
import cz.algone.common.enumAlias.ClipMode;
import cz.algone.common.enumAlias.RenderMode;
import cz.algone.model.models3D.Solid;
import cz.algone.objectData.Part;
import cz.algone.objectData.TopologyType;
import cz.algone.objectData.Vertex;
import cz.algone.raster.ImageBuffer;
import cz.algone.raster.ZBuffer;
import cz.algone.shader.Shader;
import cz.algone.shader.ShaderLighting;
import cz.algone.transforms.Col;
import cz.algone.transforms.Mat4;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import cz.algone.util.color.ColorPair;

import java.util.List;
import java.util.Optional;

public class Renderer implements IAlgorithm {
    private final LineRasterizerBresenhamZ lineRasterizer;
    private final TriangleRasterizer triangleRasterizer;

    private int width, height;
    private ClipMode clipMode = ClipMode.FAST;
    private RenderMode renderMode = RenderMode.WIREFRAME;

    private Mat4 view, proj;

    // ─── Osvětlení ───────────────────────────────────────────────────────────────
    private boolean lightEnabled = false;
    private Vec3D lightPosition = new Vec3D(2, 2, 2);
    private Col lightColor = new Col(1.0, 1.0, 1.0);
    private double ambientCoeff = 0.2;
    private Vec3D cameraEye = new Vec3D(0, 0, 0);
    private double specularCoeff = 0.5;
    private double specularShininess = 32.0;

    public Renderer(LineRasterizerBresenhamZ lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
    }

    /** Nastaví ZBuffer do obou rasterizérů */
    public void setZBuffer(ZBuffer zBuffer) {
        lineRasterizer.setup(zBuffer);
        triangleRasterizer.setup(zBuffer);
    }

    /** Nastaví parametry světla pro následující vykreslení */
    public void setLight(Vec3D position, Col color) {
        this.lightEnabled = true;
        this.lightPosition = position;
        this.lightColor = color;
    }

    /** Vypne osvětlení */
    public void clearLight() {
        this.lightEnabled = false;
    }

    public boolean isLightEnabled() { return lightEnabled; }

    /** Vykreslí těleso dle aktuálního {@link RenderMode} */
    public void render(Solid solid) {
        renderInternal(solid, renderMode, true);
    }

    /** Vykreslí těleso bez osvětlení (pro LightSolid) */
    public void renderUnlit(Solid solid) {
        renderInternal(solid, renderMode, false);
    }

    /** Vykreslí těleso vždy se VŠEMI částmi bez ohledu na RenderMode (pro osy) */
    public void renderAlways(Solid solid) {
        renderInternal(solid, null, false);
    }

    private void renderInternal(Solid solid, RenderMode mode, boolean applyLighting) {
        ColorPair wireColor = solid.isSelected() ? solid.getHighlightColor() : solid.getColor();
        Mat4 model = solid.getModel();

        if (clipMode == ClipMode.ANALYTICAL) {
            renderAnalytical(solid, model, mode, wireColor, applyLighting);
        } else {
            renderFastOrNone(solid, model, mode, wireColor, applyLighting);
        }
    }

    // ─── NONE / FAST ────────────────────────────────────────────────────────────

    private void renderFastOrNone(Solid solid, Mat4 model, RenderMode mode,
                                  ColorPair wireColor, boolean applyLighting) {
        Vertex[] transformed = transformVertices(solid, model, wireColor);
        if (transformed == null) return;

        for (Part part : solid.getPb()) {
            if (mode == RenderMode.WIREFRAME && part.getTopologyType() == TopologyType.TRIANGLES) continue;
            if (mode == RenderMode.FILLED   && part.getTopologyType() == TopologyType.LINES) continue;

            switch (part.getTopologyType()) {
                case LINES     -> drawLines(solid, transformed, part);
                case TRIANGLES -> drawTriangles(solid, transformed, part, model, applyLighting);
            }
        }
    }

    /** Transformuje vrcholy do window space. Vrací null, pokud fast clip zahodí celé těleso. */
    private Vertex[] transformVertices(Solid solid, Mat4 model, ColorPair wireColor) {
        int size = solid.getVb().size();
        Vertex[] result = new Vertex[size];

        for (int i = 0; i < size; i++) {
            Vertex v = solid.getVb().get(i);
            Point3D clip = v.getPosition().mul(model).mul(view).mul(proj);

            Optional<Vec3D> ndcOpt = clip.dehomog();
            if (ndcOpt.isEmpty()) {
                if (clipMode == ClipMode.FAST) return null;
                continue;
            }

            Vec3D ndc = ndcOpt.get();
            if (clipMode == ClipMode.FAST && isOutsideNdc(ndc)) return null;

            Vec3D win = ndcToWindow(ndc);
            double invW = 1.0 / clip.getW();
            result[i] = new Vertex(
                    new Point3D(win.getX(), win.getY(), ndc.getZ(), 1.0),
                    wireColor.primary(),
                    0, 0, invW
            );
        }
        return result;
    }

    private void drawLines(Solid solid, Vertex[] transformed, Part part) {
        int index = part.getStartIndex();
        for (int i = 0; i < part.getCount(); i++) {
            int ia = solid.getIb().get(index++);
            int ib = solid.getIb().get(index++);
            if (transformed[ia] == null || transformed[ib] == null) continue;
            lineRasterizer.rasterize(transformed[ia], transformed[ib]);
        }
    }

    private void drawTriangles(Solid solid, Vertex[] transformed, Part part,
                               Mat4 model, boolean applyLighting) {
        boolean lit = applyLighting && lightEnabled;
        Shader shader = solid.getShader();
        Shader effectiveShader = lit ? new ShaderLighting(shader, lightColor) : shader;

        int index = part.getStartIndex();
        for (int i = 0; i < part.getCount(); i++) {
            int ia = solid.getIb().get(index++);
            int ib = solid.getIb().get(index++);
            int ic = solid.getIb().get(index++);
            if (transformed[ia] == null || transformed[ib] == null || transformed[ic] == null) continue;

            Vertex origA = solid.getVb().get(ia);
            Vertex origB = solid.getVb().get(ib);
            Vertex origC = solid.getVb().get(ic);

            Vertex a = withShaderColor(transformed[ia], origA);
            Vertex b = withShaderColor(transformed[ib], origB);
            Vertex c = withShaderColor(transformed[ic], origC);

            if (lit) {
                Vec3D faceNormal = computeFaceNormal(origA, origB, origC, model);
                a = withLighting(a, origA, model, faceNormal);
                b = withLighting(b, origB, model, faceNormal);
                c = withLighting(c, origC, model, faceNormal);
            }

            triangleRasterizer.rasterize(a, b, c, effectiveShader);
        }
    }

    //TODO: nešlo by tohle místo nechání přímo v rendereru nějak oddělit? takhle nám narůstá strašně renderer o kondicionální věci
    // ─── ANALYTICAL ─────────────────────────────────────────────────────────────

    private void renderAnalytical(Solid solid, Mat4 model, RenderMode mode,
                                  ColorPair wireColor, boolean applyLighting) {
        Vertex[] clipVertices = transformToClip(solid, model);

        for (Part part : solid.getPb()) {
            if (mode == RenderMode.WIREFRAME && part.getTopologyType() == TopologyType.TRIANGLES) continue;
            if (mode == RenderMode.FILLED   && part.getTopologyType() == TopologyType.LINES) continue;

            switch (part.getTopologyType()) {
                case LINES     -> drawLinesAnalytical(solid, clipVertices, part, wireColor.primary());
                case TRIANGLES -> drawTrianglesAnalytical(solid, clipVertices, part, model, applyLighting);
            }
        }
    }

    /** Transformuje všechny vrcholy do homogenního clip space se zachováním atributů. */
    private Vertex[] transformToClip(Solid solid, Mat4 model) {
        int size = solid.getVb().size();
        Vertex[] result = new Vertex[size];
        for (int i = 0; i < size; i++) {
            Vertex v = solid.getVb().get(i);
            Point3D clip = v.getPosition().mul(model).mul(view).mul(proj);
            result[i] = new Vertex(clip, v.getColor(), v.getU(), v.getV());
        }
        return result;
    }

    private void drawLinesAnalytical(Solid solid, Vertex[] clipVertices, Part part, Col color) {
        int index = part.getStartIndex();
        for (int i = 0; i < part.getCount(); i++) {
            int ia = solid.getIb().get(index++);
            int ib = solid.getIb().get(index++);

            Vertex[] clipped = Clipper3D.clipLine(clipVertices[ia], clipVertices[ib]);
            if (clipped == null) continue;

            Vertex va = clipToWindowVertex(clipped[0], color);
            Vertex vb = clipToWindowVertex(clipped[1], color);
            if (va == null || vb == null) continue;
            lineRasterizer.rasterize(va, vb);
        }
    }

    private void drawTrianglesAnalytical(Solid solid, Vertex[] clipVertices, Part part,
                                         Mat4 model, boolean applyLighting) {
        boolean lit = applyLighting && lightEnabled;
        Shader shader = solid.getShader();
        Shader effectiveShader = lit ? new ShaderLighting(shader, lightColor) : shader;

        int index = part.getStartIndex();
        for (int i = 0; i < part.getCount(); i++) {
            int ia = solid.getIb().get(index++);
            int ib = solid.getIb().get(index++);
            int ic = solid.getIb().get(index++);

            // Předpočítat lighting intensity pro analytickou cestu
            double intensA = 1.0, intensB = 1.0, intensC = 1.0;
            if (lit) {
                Vertex origA = solid.getVb().get(ia);
                Vertex origB = solid.getVb().get(ib);
                Vertex origC = solid.getVb().get(ic);
                Vec3D faceNormal = computeFaceNormal(origA, origB, origC, model);
                intensA = computeIntensity(origA, model, faceNormal);
                intensB = computeIntensity(origB, model, faceNormal);
                intensC = computeIntensity(origC, model, faceNormal);
            }

            List<Vertex[]> tris = Clipper3D.clipTriangle(clipVertices[ia], clipVertices[ib], clipVertices[ic]);
            for (Vertex[] tri : tris) {
                Vertex va = clipToWindowVertex(tri[0]);
                Vertex vb = clipToWindowVertex(tri[1]);
                Vertex vc = clipToWindowVertex(tri[2]);
                if (va == null || vb == null || vc == null) continue;

                if (lit) {
                    // Použijeme průměr intenzit z rohů pro clipped sub-trojúhelníky
                    va = va.withIntensity(intensA);
                    vb = vb.withIntensity(intensB);
                    vc = vc.withIntensity(intensC);
                }

                triangleRasterizer.rasterize(va, vb, vc, effectiveShader);
            }
        }
    }

    /** Dehomogenizuje clip-space vertex a převede do window space. Zachová barvu a UV s perspektivní korekcí. */
    private Vertex clipToWindowVertex(Vertex clipVertex) {
        Point3D clip = clipVertex.getPosition();
        Optional<Vec3D> ndcOpt = clip.dehomog();
        if (ndcOpt.isEmpty()) return null;
        Vec3D ndc = ndcOpt.get();
        Vec3D win = ndcToWindow(ndc);
        double invW = 1.0 / clip.getW();
        return new Vertex(
                new Point3D(win.getX(), win.getY(), ndc.getZ(), 1.0),
                clipVertex.getColor(),
                clipVertex.getU() * invW,
                clipVertex.getV() * invW,
                invW
        );
    }

    /** Dehomogenizuje clip-space vertex do window space s pevnou barvou (pro čáry). */
    private Vertex clipToWindowVertex(Vertex clipVertex, Col color) {
        Point3D clip = clipVertex.getPosition();
        Optional<Vec3D> ndcOpt = clip.dehomog();
        if (ndcOpt.isEmpty()) return null;
        Vec3D ndc = ndcOpt.get();
        Vec3D win = ndcToWindow(ndc);
        return new Vertex(new Point3D(win.getX(), win.getY(), ndc.getZ(), 1.0), color);
    }

    // ─── Osvětlení – Gouraud ─────────────────────────────────────────────────────

    /** Spočítá osvětlení pro daný vrchol a vrátí nový Vertex s nastavenou intensity.
     *  faceNormal se použije jako fallback pro vrcholy bez per-vertex normály. */
    private Vertex withLighting(Vertex windowVertex, Vertex original, Mat4 model, Vec3D faceNormal) {
        double intensity = computeIntensity(original, model, faceNormal);
        return new Vertex(
                windowVertex.getPosition(),
                windowVertex.getColor(),
                windowVertex.getU(), windowVertex.getV(), windowVertex.getW(),
                0, 0, 0, intensity
        );
    }

    /** Spočítá intensity = ambient + diffuse * max(0, dot(N, L)) pro daný vrchol. */
    private double computeIntensity(Vertex original, Mat4 model, Vec3D faceNormal) {
        // World-space pozice vrcholu
        Point3D worldPt = original.getPosition().mul(model);
        Vec3D worldPos = new Vec3D(worldPt.getX(), worldPt.getY(), worldPt.getZ());

        // World-space normála – per-vertex nebo face fallback
        Vec3D normal = getWorldNormal(original, model, faceNormal);

        // Směr ke světlu
        Vec3D toLight = lightPosition.sub(worldPos);
        Optional<Vec3D> lOpt = toLight.normalized();
        if (lOpt.isEmpty()) return ambientCoeff;
        Vec3D L = lOpt.get();

        double diff = Math.max(0, normal.dot(L));

        double spec = 0.0;
        if (diff > 0) {
            Optional<Vec3D> vOpt = cameraEye.sub(worldPos).normalized();
            if (vOpt.isPresent()) {
                Vec3D R = normal.mul(2.0 * normal.dot(L)).sub(L);
                spec = Math.pow(Math.max(0, R.dot(vOpt.get())), specularShininess) * specularCoeff;
            }
        }

        return Math.min(1.0, ambientCoeff + (1.0 - ambientCoeff) * diff + spec);
    }


    /** Vrací world-space normálu. Pokud vertex má explicitní normálu, transformuje ji modelem.
     *  Pokud ne (0,0,0), použije faceNormal. */
    private Vec3D getWorldNormal(Vertex v, Mat4 model, Vec3D faceNormal) {
        double nx = v.getNx(), ny = v.getNy(), nz = v.getNz();
        if (nx == 0 && ny == 0 && nz == 0) {
            return faceNormal;
        }
        // Transformace normály modelem (w=0 → bez translace, rotace + uniformní scale)
        Point3D transformed = new Point3D(nx, ny, nz, 0).mul(model);
        Vec3D worldNormal = new Vec3D(transformed.getX(), transformed.getY(), transformed.getZ());
        return worldNormal.normalized().orElse(faceNormal);
    }

    /** Spočítá face normálu z 3 vrcholů trojúhelníku ve world space. */
    private Vec3D computeFaceNormal(Vertex a, Vertex b, Vertex c, Mat4 model) {
        Vec3D wa = toWorldPos(a, model);
        Vec3D wb = toWorldPos(b, model);
        Vec3D wc = toWorldPos(c, model);
        Vec3D ab = wb.sub(wa);
        Vec3D ac = wc.sub(wa);
        Vec3D cross = ab.cross(ac);
        return cross.normalized().orElse(new Vec3D(0, 1, 0));
    }

    /** Transformuje pozici vrcholu do world space. */
    private Vec3D toWorldPos(Vertex v, Mat4 model) {
        Point3D wp = v.getPosition().mul(model);
        return new Vec3D(wp.getX(), wp.getY(), wp.getZ());
    }

    // ─── Sdílené utility ────────────────────────────────────────────────────────

    //TODO: v utils už máme nějakou podobnou metodu, nejde použít? a pokud ne, proč tohle není tam?
    private boolean isOutsideNdc(Vec3D ndc) {
        return ndc.getX() < -1 || ndc.getX() > 1
                || ndc.getY() < -1 || ndc.getY() > 1
                || ndc.getZ() < 0  || ndc.getZ() > 1;
    }
    //TODO: nemělo by tohle být v utils?
    private Vec3D ndcToWindow(Vec3D p) {
        return p.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2.0, (height - 1) / 2.0, 1));
    }
    //TODO: nemělo by tohle být v utils?
    private Vertex withShaderColor(Vertex transformed, Vertex original) {
        double invW = transformed.getW();
        return new Vertex(
                transformed.getPosition(),
                original.getColor(),
                original.getU() * invW,  // u/w pro perspektivně korektní interpolaci
                original.getV() * invW,  // v/w
                invW                     // 1/w
        );
    }

    // ─── Render kolekce ─────────────────────────────────────────────────────────

    public void renderAxisSolids(List<Solid> axis) {
        for (Solid solid : axis) renderAlways(solid);
    }

    // ─── Gettery / settery ──────────────────────────────────────────────────────

    public ClipMode getClipMode() { return clipMode; }
    public void setClipMode(ClipMode mode) { this.clipMode = mode; }

    /** Zpětná kompatibilita pro starší kód používající boolean. */
    public boolean getEnableFastClip() { return clipMode == ClipMode.FAST; }
    public void setEnableFastClip(boolean v) { clipMode = v ? ClipMode.FAST : ClipMode.NONE; }

    public RenderMode getRenderMode() { return renderMode; }
    public void setRenderMode(RenderMode mode) { this.renderMode = mode; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setView(Mat4 view) { this.view = view; }
    public void setProj(Mat4 proj) { this.proj = proj; }

    public void setAmbientCoeff(double ambientCoeff) { this.ambientCoeff = ambientCoeff; }
    public double getAmbientCoeff() { return ambientCoeff; }
    public void setCameraEye(Vec3D eye) { this.cameraEye = eye; }

    @Override
    public void setup(ImageBuffer raster) {}
}
