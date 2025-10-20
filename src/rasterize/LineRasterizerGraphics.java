package rasterize;

import model.Line;
import raster.RasterBufferedImage;

import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer {

    public LineRasterizerGraphics(RasterBufferedImage raster) {
        super(raster);
    }

    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        Graphics g = raster.getImage().getGraphics();
        g.setColor(Color.RED);
        g.drawLine(x1, y1, x2, y2);
    }
}
