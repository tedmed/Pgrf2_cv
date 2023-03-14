package Objects;

import model.Solid;
import model.Vertex;
import transforms.Bicubic;
import transforms.Cubic;
import transforms.Point3D;

import java.util.ArrayList;

public class PatchWire extends Solid {
    public PatchWire(){
        // TODO: řídící body
        Point3D[] points = new Point3D[4];

        Bicubic bicubic = new Bicubic(Cubic.FERGUSON, points);

        for (int i = 0; i < 100; i++) {
            double t = i / 100.;
            for (int j = 0; j < 100; j++) {
                double u = j / 100.;

                Vertex v = new Vertex(bicubic.compute(u, t));
                getVertexBuffer().add(v);
            }
        }
    }
}
