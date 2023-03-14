package Objects;

import model.Solid;
import model.Vertex;
import transforms.Cubic;
import transforms.Point3D;

public class CurveWire extends Solid {
    public CurveWire(){
        // points
        Point3D[] points = new Point3D[4];
        points[0] = new Point3D(-1,-1,-1);
        points[1] = new Point3D(1,1,-1);
        points[2] = new Point3D(1,-1,1);
        points[3] = new Point3D(-1,1,1);
        Cubic cubic = new Cubic(Cubic.BEZIER, points);

        for (int j = 0; j < 100; j++) {
            double u = j / 100.;

            Vertex v = new Vertex(cubic.compute(u));
            getVertexBuffer().add(v);
        }
    }
}
