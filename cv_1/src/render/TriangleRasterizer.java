package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.lerp = new Lerp<>();
    }
    public void rasterize(Vertex v1, Vertex v2, Vertex v3, int col){

        //Done: transformace ve Vertexu
        Vertex b = v1.transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex c = v2.transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex a = v3.transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex pomoc;

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(col));

        g.drawLine((int)a.getPosition().getX(), (int)a.getPosition().getY(), (int)b.getPosition().getX(), (int)b.getPosition().getY());
        g.drawLine((int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)c.getPosition().getX(), (int)c.getPosition().getY());
        g.drawLine((int)c.getPosition().getX(), (int)c.getPosition().getY(), (int)a.getPosition().getX(), (int)a.getPosition().getY());

        //Done: seřadit vrcholy podle Y, 3 podmínky

        if(a.getPosition().getY() > b.getPosition().getY()) {
            pomoc = a;
            a= b;
            b = pomoc;
        }

        if(a.getPosition().getY() > c.getPosition().getY()) {
            pomoc = a;
            a = c;
            c = pomoc;
        }

        if(b.getPosition().getY() > c.getPosition().getY()) {
            pomoc = b;
            b = c;
            c = pomoc;
        }

        for (int y = (int)a.getPosition().getY(); y <= b.getPosition().getY(); y++){
            // interpolační koeficient strany AB
            double t1 = (y - a.getPosition().getY()) / (b.getPosition().getY() - a.getPosition().getY());
            //Done: použít lerp
            Vertex vAB = lerp.lerp(a, b,t1);

            // interpolační koeficient strany AC
            double t2 = (y - a.getPosition().getY()) / (c.getPosition().getY() - a.getPosition().getY());
            //Done: použít lerp
            Vertex vAC = lerp.lerp(a, c,t2);

            if(vAB.getPosition().getX() > vAC.getPosition().getX()){
                Vertex pom = vAB;
                vAB = vAC;
                vAC = pom;
            }
            for(int x = (int)vAB.getPosition().getX(); x <= vAC.getPosition().getX(); x++){
                // TODO: interpolační koeficient
                double tf = (x-vAB.getPosition().getX())/(vAC.getPosition().getX() - vAB.getPosition().getX());
                // TODO: použít lerp
                Vertex v = lerp.lerp(vAB, vAC, tf);
                // TODO: z, Col, uv, normála
                zBuffer.drawWithZTest(x, y, v.getPosition().getZ(), new Col(0xffff00));
            }
        }

        for (int y = (int)b.getPosition().getY(); y <= c.getPosition().getY(); y++){
            // interpolační koeficient strany AB
            double t1 = (y - b.getPosition().getY()) / (c.getPosition().getY() - b.getPosition().getY());
            //Done: použít lerp
            Vertex vBC = lerp.lerp(b, c,t1);

            // interpolační koeficient strany AC
            double t2 = (y - a.getPosition().getY()) / (c.getPosition().getY() - a.getPosition().getY());
            //Done: použít lerp
            Vertex vAC = lerp.lerp(a, c,t2);

            if(vBC.getPosition().getX() > vAC.getPosition().getX()){
                Vertex pom = vBC;
                vBC = vAC;
                vAC = pom;
            }
            for(int x = (int)vBC.getPosition().getX(); x <= vAC.getPosition().getX(); x++){
                // TODO: interpolační koeficient
                double tf = (x-vBC.getPosition().getX())/(vAC.getPosition().getX() - vBC.getPosition().getX());
                // TODO: použít lerp
                Vertex v = lerp.lerp(vBC, vAC, tf);
                // TODO: z, Col, uv, normála
                zBuffer.drawWithZTest(x, y, v.getPosition().getZ(), new Col(0xffff00));
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
