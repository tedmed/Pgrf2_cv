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
    public void rasterize(Vertex v1, Vertex v2, int col){

        //Done: transformace ve Vertexu
        Vertex a = v1.transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex b = v2.transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex pomoc;

        Graphics g = zBuffer.getGraphics();
        g.setColor(new Color(col));

        g.drawLine((int)a.getPosition().getX(), (int)a.getPosition().getY(), (int)b.getPosition().getX(), (int)b.getPosition().getY());

        //Done: seřadit vrcholy podle Y, 3 podmínky

        if(a.getPosition().getY() > b.getPosition().getY()) {
            pomoc = a;
            a= b;
            b = pomoc;
        }

        for (int y = (int)a.getPosition().getY(); y <= b.getPosition().getY(); y++) {
            // interpolační koeficient strany AB
            double t1 = (y - a.getPosition().getY()) / (b.getPosition().getY() - a.getPosition().getY());
            //Done: použít lerp
            Vertex v = lerp.lerp(a, b, t1);
            zBuffer.drawWithZTest((int)v.getPosition().getX(), y, v.getPosition().getZ(), new Col(0xffff00));
        }
//
//        for(int x = (int)a.getPosition().getX(); x <= b.getPosition().getX(); x++){
//            // TODO: interpolační koeficient
//            double tf = (x-a.getPosition().getX())/(b.getPosition().getX() - a.getPosition().getX());
//            // TODO: použít lerp
//            Vertex v = lerp.lerp(a, b, tf);
//            // TODO: z, Col, uv, normála
//            zBuffer.drawWithZTest(x, (int)v.getPosition().getY(), v.getPosition().getZ(), new Col(0xffff00));
//        }
    }

    private Vec3D transformToWindow(Point3D p){
        return new Vec3D(p)
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((zBuffer.getWidth()-1)/2., (zBuffer.getHeight()-1)/2., 1));
    }
}
