package cz.algone;

import cz.algone.controller.LineRasterizeController;
import cz.algone.controller.Raster2DController;
import cz.algone.raster.RasterBufferedImage;
import cz.algone.rasterize.LineRasterizerBresenham;
import cz.algone.view.Panel;
import cz.algone.view.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 600);
        Panel panel = window.getPanel();
        RasterBufferedImage raster = panel.getRaster();

        new Raster2DController(new LineRasterizeController(panel, new LineRasterizerBresenham(raster)));
    }
}