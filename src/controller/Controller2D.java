package controller;

import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.LineRasterizerTrivial;
import view.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller2D {
    private final Panel panel;

    private Line line;
    private LineRasterizer rasterizer;

    private Polygon polygon;

    public Controller2D(Panel panel) {
        this.panel = panel;
        rasterizer = new LineRasterizerTrivial(panel.getRaster());

        polygon = new Polygon();

        initListeners();
    }

    private void initListeners() {
        /*panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                polygon.addPoint(new Point(e.getX(), e.getY()));
                drawScene();
            }

        });*/
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                line = new Line(panel.getWidth()/2, e.getX(), panel.getHeight()/2 ,e.getY());
                drawScene();
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();
        rasterizer.rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
        panel.repaint();
    }
}
