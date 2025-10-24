package controller;

public class Raster2DController {
    RasterizeController rasterizeController;

    public Raster2DController(RasterizeController controller) {
        this.rasterizeController = controller;
        controller.initListeners();
    }

    public void setRasterizeController(RasterizeController controller) {
        this.rasterizeController = controller;
    }
}
