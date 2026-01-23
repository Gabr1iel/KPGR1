package cz.algone.algorithm.algorithm3D;

import cz.algone.algorithm.rasterizer.line.LineRasterizerBresenham;
import cz.algone.model.Line;
import cz.algone.model.models3D.Solid;
import cz.algone.transforms.Mat4;
import cz.algone.transforms.Point3D;
import cz.algone.transforms.Vec3D;
import java.util.List;

public class Renderer {
    private LineRasterizerBresenham lineRasterizer;
    private int width, height;
    private boolean enableFastClip = false;

    private Mat4 view, proj;

    public Renderer(LineRasterizerBresenham lineRasterizer, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.width = width;
        this.height = height;
    }

    public void render(Solid solid) {
        // for cyklus, který projde ib a pro každý index načte vertex
        for (int i = 0; i < solid.getIb().size(); i += 2) {
            int indexA = solid.getIb().get(i);
            int indexB = solid.getIb().get(i + 1);

            Point3D pointA = solid.getVb().get(indexA);
            Point3D pointB = solid.getVb().get(indexB);

            // Model transformace z model space do world space
            pointA = pointA.mul(solid.getModel());
            pointB = pointB.mul(solid.getModel());
            // View transformace z world space do view space
            pointA = pointA.mul(view);
            pointB = pointB.mul(view);
            // Projections transformace z view space clip space
            pointA = pointA.mul(proj);
            pointB = pointB.mul(proj);

            // dehomogenizace (Clip -> NDC)
            var ndcAOpt = pointA.dehomog();
            var ndcBOpt = pointB.dehomog();
            if (ndcAOpt.isEmpty() || ndcBOpt.isEmpty()) {
                if (enableFastClip) return;
                else break;
            }

            Vec3D ndcA = ndcAOpt.get();
            Vec3D ndcB = ndcBOpt.get();

            // "rychlé ořezání" -> zahodí celý solid
            if (enableFastClip && (!insideNdc(ndcA) || !insideNdc(ndcB))) {
                break;
            }


            // TODO: transformace do okna obrazovky
            Vec3D pointAInWindow = transformToWindow(ndcA);
            Vec3D pointBInWindow = transformToWindow(ndcB);

            // rasterizace
            Line line = new Line(
                    (int) Math.round(pointAInWindow.getX()), (int) Math.round(pointAInWindow.getY()),
                    (int) Math.round(pointBInWindow.getX()), (int) Math.round(pointBInWindow.getY()),
                    (!solid.isSelected()) ? solid.getColor() : solid.getHighlightColor()
            );

            lineRasterizer.rasterize(line);
        }
    }

    private Vec3D transformToWindow(Vec3D p) {
        return p.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2.f, (height - 1) / 2.f, 1));
    }

    public void renderSolids(List<Solid> solids) {
        for (Solid solid : solids) {
            render(solid);
        }
    }

    private boolean insideNdc(Vec3D p) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        return x >= -1.0 && x <= 1.0
                && y >= -1.0 && y <= 1.0
                && z >= 0.0  && z <= 1.0;
    }

    public boolean getEnableFastClip() {return enableFastClip;}

    public void setEnableFastClip(boolean enableFastClip) {this.enableFastClip = enableFastClip;}

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
