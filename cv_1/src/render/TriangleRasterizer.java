package render;

import model.Lerp;
import model.Vertex;
import raster.ZBuffer;
import shaders.Shader;
import shaders.ShaderConstCol;
import shaders.ShaderInterCol;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> lerp;
    private Shader shader;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
        this.lerp = new Lerp<>();
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void rasterize(Vertex v1, Vertex v2, Vertex v3){

        //Done: transformace ve Vertexu
        Vertex a = v1.dehomog(v1).transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex b = v2.dehomog(v2).transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
        Vertex c = v3.dehomog(v3).transformToWindow(zBuffer.getWidth(), zBuffer.getHeight());
//
//        Graphics g = zBuffer.getGraphics();
//        g.setColor(new Color(col));
//
//        g.drawLine((int)a.getPosition().getX(), (int)a.getPosition().getY(), (int)b.getPosition().getX(), (int)b.getPosition().getY());
//        g.drawLine((int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)c.getPosition().getX(), (int)c.getPosition().getY());
//        g.drawLine((int)c.getPosition().getX(), (int)c.getPosition().getY(), (int)a.getPosition().getX(), (int)a.getPosition().getY());

        //Done: seřadit vrcholy podle Y, 3 podmínky

        Vertex pomoc;
        if(a.getPosition().getY() > b.getPosition().getY()) {
            pomoc = a;
            a = b;
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

        for (int y = Math.max((int)a.getPosition().getY()+1,0); y <= Math.min(b.getPosition().getY(),zBuffer.getHeight()-1); y++){
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
            for(int x = Math.max((int)vAB.getPosition().getX(),0); x <= Math.min(vAC.getPosition().getX(),zBuffer.getWidth()-1); x++){
                //Done: interpolační koeficient
                double tf = (x-vAB.getPosition().getX())/(vAC.getPosition().getX() - vAB.getPosition().getX());
                //Done: použít lerp
                Vertex v = lerp.lerp(vAB, vAC, tf);
                // TODO: z, Col, uv, normála
                zBuffer.drawWithZTest(x, y, v.getPosition().getZ(), shader.shade(v));
            }
        }

        for (int y = Math.max((int)b.getPosition().getY()+1,0); y <= Math.min(c.getPosition().getY(),zBuffer.getHeight()-1); y++){
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
            for(int x = Math.max((int)vBC.getPosition().getX(),0); x <= Math.min(vAC.getPosition().getX(),zBuffer.getWidth()-1); x++){
                //Done: interpolační koeficient
                double tf = (x-vBC.getPosition().getX())/(vAC.getPosition().getX() - vBC.getPosition().getX());
                //Done: použít lerp
                Vertex v = lerp.lerp(vBC, vAC, tf);
                // TODO: z, Col, uv, normála
                zBuffer.drawWithZTest(x, y, v.getPosition().getZ(), shader.shade(v));
            }
        }
    }
}
