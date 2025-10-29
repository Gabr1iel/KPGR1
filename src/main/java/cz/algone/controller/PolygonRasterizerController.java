package cz.algone.controller;

import cz.algone.raster.RasterCanvas;
import cz.algone.rasterize.Rasterizer;

public class PolygonRasterizerController implements RasterizeController {
    @Override
    public void initListeners() {
        /*panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                polygon.addPoint(new Point(e.getX(), e.getY()));
                drawScene();
            }

        });*/
    }

    @Override
    public void drawScene() {

    }

    @Override
    public void setup(RasterCanvas raster, Rasterizer rasterizer) {

    }
}
