package render;

import raster.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }
    public void rasterize(Point3D p1, Point3D p2, Point3D p3, int col){

        Vec3D a = transformToWindow(p1);
        Vec3D b = transformToWindow(p2);
        Vec3D c = transformToWindow(p3);

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(col));

        g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
        g.drawLine((int)b.x, (int)b.y, (int)c.x, (int)c.y);
        g.drawLine((int)c.x, (int)c.y, (int)a.x, (int)a.y);

        // TODO: seřadit vrcholy podle Y

        for (int y = (int)a.y; y <= b.y; y++){
            // interpolační koeficient strany AB
            double t1 = (y - a.y) / (b.y - a.y);
            // vypočítám x1
            int x1 = (int)((1 - t1) * a.x + t1 * b.x);

            // interpolační koeficient strany AC
            double t2 = (y - a.y) / (c.y - a.y);
            // vypočítám x2
            int x2 = (int)((1 - t2) * a.x + t2 * c.x);

            for(int x = x1; x <= x2; x++){
                zBuffer.drawWithZTest(x, y, 0, new Col(0xffff00));
            }
        }
    }

    private Vec3D transformToWindow(Point3D p){
        return new Vec3D(p)
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((zBuffer.getWidth()-1)/2., (zBuffer.getHeight()-1)/2., 1));
    }
}
