package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class LineRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;

    public LineRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.lerp = new Lerp<>();
    }
    public void rasterize(Vertex v1, Vertex v2){

        //Done: transformace ve Vertexu
        Vertex a = v1.dehomog(v1).transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex b = v2.dehomog(v2).transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex pomoc;

        //Done: seřadit vrcholy podle Y, 3 podmínky

        double dx = Math.abs(a.getX()-b.getX());
        double dy = Math.abs(a.getY()-b.getY());
        float k = (float) dy / (float) dx;

        if(k >= 1 || k <= -1) {

            if (a.getPosition().getY() > b.getPosition().getY()) {
                pomoc = a;
                a = b;
                b = pomoc;
            }

            for (int y = Math.max((int)a.getPosition().getY()+1,0); y <= Math.min(b.getPosition().getY(),zBuffer.getHeight()-1); y++) {
                // interpolační koeficient strany AB
                double t1 = (y - a.getPosition().getY()) / (b.getPosition().getY() - a.getPosition().getY());
                //Done: použít lerp
                Vertex vAB = lerp.lerp(a, b, t1);
                zBuffer.drawWithZTest((int) vAB.getX(), y, vAB.getPosition().getZ(), a.getColor());
            }
        }
        else {
            if (a.getPosition().getX() > b.getPosition().getX()) {
                pomoc = a;
                a = b;
                b = pomoc;
            }
            for (int x = Math.max((int)a.getPosition().getX()+1,0); x <= Math.min(b.getPosition().getX(),zBuffer.getWidth()-1); x++) {
                //Done: interpolační koeficient
                double t1 = (x - a.getPosition().getX()) / (b.getPosition().getX() - a.getPosition().getX());
                //Done: použít lerp
                Vertex vAB = lerp.lerp(a, b, t1);
                //Done: z, Col, uv, normála
                zBuffer.drawWithZTest(x, (int) vAB.getY(), vAB.getPosition().getZ(), a.getColor());
            }
        }
    }
}
