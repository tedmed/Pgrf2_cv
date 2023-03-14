package render;

import model.Lerp;
import model.Part;
import model.Solid;
import model.Vertex;
import shaders.Shader;
import shaders.ShaderConstCol;
import shaders.ShaderInterCol;
import transforms.Mat4;
import transforms.Point3D;

import java.util.List;

public class Renderer {
    private final Lerp<Vertex> lerp;
    private TriangleRasterizer triangleRasterizer;
    private LineRasterizer lineRasterizer;
    private Mat4 view, proj;

    public Renderer(TriangleRasterizer triangleRasterizer, LineRasterizer lineRasterizer, Mat4 view, Mat4 proj) {
        this.view = view;
        this.proj = proj;
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.lerp = new Lerp<>();
    }

    public void render(List<Solid> scene){
        for (Solid solid : scene) {
            render(solid);
        }
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void render(Solid solid){
        Mat4 mvp = solid.getModel().mul(view).mul(proj);
        for (Part part : solid.getPartBuffer()) {
            int start;
            switch (part.getType()){
                // TODO: all of the cases
                case LINE:
                    start = part.getIndex();
                    for (int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start+1);

                        start += 2;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);

                        renderLine(a, b);
                    }
                    break;
                case POINT:
                    break;
                case TRIANGLE:
                    start = part.getIndex();
                    for (int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(start);
                        int indexB = solid.getIndexBuffer().get(start+1);
                        int indexC = solid.getIndexBuffer().get(start+2);

                        start += 3;

                        Vertex a = solid.getVertexBuffer().get(indexA).transform(mvp);
                        Vertex b = solid.getVertexBuffer().get(indexB).transform(mvp);
                        Vertex c = solid.getVertexBuffer().get(indexC).transform(mvp);

                        renderTriangle(a, b, c);
                    }
                    break;
                case LINE_STRIP:
                    break;
                case TRIANGLE_STRIP:
                    break;
            }
        }
    }

    private void renderLine(Vertex a, Vertex b){
//        shaderInter.shade(a);
//        shaderInter.shade(b);
//        shaderConst.shade(a);
//        shaderConst.shade(b);
        if((a.getX() > a.getW() && b.getX() > b.getW()) ||
                (-a.getW() > a.getX() && -b.getW() > b.getX()) ||
                (a.getY() > a.getW() && b.getY() > b.getW()) ||
                (-a.getW() > a.getY() && -b.getW() > b.getY()) ||
                (0 > a.getZ() && 0 > b.getZ()) ||
                (a.getZ() > a.getW() && b.getZ() > b.getW())) return;

        Vertex pomoc;
        if(a.getPosition().getZ() < b.getPosition().getZ()) {
            pomoc = a;
            a = b;
            b = pomoc;
        }

        double zMin = 0;
        if(a.getPosition().getZ() <= zMin) return;
        else if(b.getPosition().getZ() <= zMin){
            double t1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            Vertex vAB = lerp.lerp(a, b, t1);

            lineRasterizer.rasterize(a, vAB);
        }
        else lineRasterizer.rasterize(a, b);
    }
    private void renderTriangle(Vertex a, Vertex b, Vertex c){
        // Done: ořez - fast clip - slide 99
        if((a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) ||
                (-a.getW() > a.getX() && -b.getW() > b.getX() && -c.getW() > c.getX()) ||
                (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) ||
                (-a.getW() > a.getY() && -b.getW() > b.getY() && -c.getW() > c.getY()) ||
                (0 > a.getZ() && 0 > b.getZ() && 0 > c.getZ()) ||
                (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW())) return;

        // seřadit vrcholy podle z, aby a.getZ() = max
        Vertex pomoc;
        if(a.getPosition().getZ() < b.getPosition().getZ()) {
            pomoc = a;
            a = b;
            b = pomoc;
        }
        if(a.getPosition().getZ() < c.getPosition().getZ()) {
            pomoc = a;
            a = c;
            c = pomoc;
        }
        if(b.getPosition().getZ() < c.getPosition().getZ()) {
            pomoc = b;
            b = c;
            c = pomoc;
        }

        // ořez podle z - slide 103
        double zMin = 0;
        if(a.getPosition().getZ() <= zMin) return;
        else if(b.getPosition().getZ() <= zMin){
            double t1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            double t2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex vAB = lerp.lerp(a, b, t1);
            Vertex vAC = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, vAB, vAC);
        }
        else if(c.getPosition().getZ() <= zMin){
            double t1 = (zMin - b.getPosition().getZ()) / (c.getPosition().getZ() - b.getPosition().getZ());
            double t2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex vBC = lerp.lerp(b, c, t1);
            Vertex vAC = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, b, vBC);
            triangleRasterizer.rasterize(a, vBC, vAC);
        }
        else triangleRasterizer.rasterize(a ,b, c);

    }
}
