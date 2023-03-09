package control;

import Objects.Arrow;
import model.Vertex;
import raster.ImageBuffer;
import raster.Raster;
import raster.ZBuffer;
import render.LineRasterizer;
import render.Renderer;
import render.TriangleRasterizer;
import transforms.Col;
import transforms.Point3D;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final TriangleRasterizer triangleRasterizer;
    private final LineRasterizer lineRasterizer;
    private final Renderer renderer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.lineRasterizer = new LineRasterizer(zBuffer);
        this.renderer = new Renderer(triangleRasterizer, lineRasterizer);
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(ImageBuffer raster) {
        raster.setClearElement(new Col(0x101010));
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void redraw() {
        panel.clear();

//        triangleRasterizer.rasterize(new Vertex(1,1,0), new Vertex(-1,0,0), new Vertex(0,-1,0), 0xff00ff);
        Arrow arrow = new Arrow();
        renderer.render(arrow);
//        triangleRasterizer.rasterize(new Point3D(-1,1,0), new Point3D(1,0,0), new Point3D(0,-1,0), 0x00ff00);
//        zBuffer.drawWithZTest(10,10,0.5,new Col(0x00ff00));
//        zBuffer.drawWithZTest(10,10,0.2,new Col(0xff0000));
//        zBuffer.drawWithZTest(10,10,0.1,new Col(0xffffff));

        panel.repaint();
    }
}
