package controller;

import model.Line;
import rasterize.Rasterizer;
import view.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LineRasterizeController implements RasterizeController {
    private final Panel panel;
    private final Rasterizer<Line> rasterizer;

    private Line line;

    public LineRasterizeController(Panel panel, Rasterizer<Line> rasterizer) {
        this.panel = panel;
        this.rasterizer = rasterizer;
    }

    @Override
    public void initListeners() {
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                line = new Line(panel.getWidth()/2, e.getX(), panel.getHeight()/2 ,e.getY());
                drawScene();
            }
        });
    }

    @Override
    public void drawScene() {
        panel.getRaster().clear();
        rasterizer.rasterize(line);
        panel.repaint();
    }
}
