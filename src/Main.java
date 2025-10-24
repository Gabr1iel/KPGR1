import controller.LineRasterizeController;
import controller.Raster2DController;
import raster.RasterBufferedImage;
import rasterize.LineRasterizerDDA;
import view.Panel;
import view.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 600);
        Panel panel = window.getPanel();
        RasterBufferedImage raster = panel.getRaster();

        new Raster2DController(new LineRasterizeController(panel, new LineRasterizerDDA(raster)));
    }
}