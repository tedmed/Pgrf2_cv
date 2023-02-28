package render;

import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }
    public void rasterize(Vertex v1, Vertex v2, Vertex v3, int col){

        // TODO: transformace ve Vertexu
        Vec3D b = transformToWindow(v1.getPosition());
        Vec3D c = transformToWindow(v2.getPosition());
        Vec3D a = transformToWindow(v3.getPosition());
        Vec3D pomoc;

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(col));

        g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
        g.drawLine((int)b.x, (int)b.y, (int)c.x, (int)c.y);
        g.drawLine((int)c.x, (int)c.y, (int)a.x, (int)a.y);

        //Done: seřadit vrcholy podle Y, 3 podmínky

        if(a.y > b.y) {
            pomoc = a;
            a = new Vec3D(b.x, b.y, b.z);
            b = pomoc;
        }

        if(a.y > c.y) {
            pomoc = a;
            a = new Vec3D(c.x, c.y, c.z);
            c = pomoc;
        }

        if(b.y > c.y) {
            pomoc = b;
            b = new Vec3D(c.x, c.y, c.z);
            c = pomoc;
        }

        for (int y = (int)a.y; y <= b.y; y++){
            // interpolační koeficient strany AB
            double t1 = (y - a.y) / (b.y - a.y);
            // vypočítám x1
            int x1 = (int)((1 - t1) * a.x + t1 * b.x);
            // TODO: použít lerp

            // interpolační koeficient strany AC
            double t2 = (y - a.y) / (c.y - a.y);
            // vypočítám x2
            int x2 = (int)((1 - t2) * a.x + t2 * c.x);
            // TODO: použít lerp

            for(int x = x1; x <= x2; x++){
                // TODO: interpolační koeficient
                // TODO: z, Col, uv, normála
                // TODO: použít lerp
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
