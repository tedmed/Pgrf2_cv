package render;

import model.Lerp;
import model.Part;
import model.Solid;
import model.Vertex;

import java.util.List;

public class Renderer {
    private final Lerp<Vertex> lerp;
    private TriangleRasterizer triangleRasterizer;
    private LineRasterizer lineRasterizer;

    public Renderer(TriangleRasterizer triangleRasterizer, LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.lerp = new Lerp<>();
    }

    public void render(List<Solid> scene){
        for (Solid solid : scene) {
            render(solid);
        }
    }

    public void render(Solid solid){
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

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

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

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);

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
        double zMin = 0;
        if(a.getPosition().getZ() < zMin) return;
        else if(b.getPosition().getZ() < zMin){
            double t1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            Vertex vAB = lerp.lerp(a, b, t1);

            lineRasterizer.rasterize(a, vAB, 0x00ffff);
        }
        else lineRasterizer.rasterize(a, b, 0x00ffff);
    }
    private void renderTriangle(Vertex a, Vertex b, Vertex c){
        // TODO: ořez - fast clip - slide 99
//        if(a.getPosition().getX() > a.getPosition().getW()){
//
//        }
        // ořez podle z - slide 103
        double zMin = 0;
        if(a.getPosition().getZ() < zMin) return;
        else if(b.getPosition().getZ() < zMin){
            double t1 = (zMin - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
            double t2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex vAB = lerp.lerp(a, b, t1);
            Vertex vAC = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, vAB, vAC, 0x00ffff);
        }
        else if(c.getPosition().getZ() < zMin){
            double t1 = (zMin - b.getPosition().getZ()) / (c.getPosition().getZ() - b.getPosition().getZ());
            double t2 = (zMin - a.getPosition().getZ()) / (c.getPosition().getZ() - a.getPosition().getZ());
            Vertex vBC = lerp.lerp(b, c, t1);
            Vertex vAC = lerp.lerp(a, c, t2);

            triangleRasterizer.rasterize(a, b, vBC, 0x00ffff);
            triangleRasterizer.rasterize(a, vBC, vAC, 0x00ffff);
        }
        else triangleRasterizer.rasterize(a ,b, c, 0x00ffff);

        // seřadit vrcholy podle z, aby a.getZ() = max
        // dehomog

    }
}
