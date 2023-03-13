package Objects;

import model.Part;
import model.Solid;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Arrow extends Solid {
    public Arrow(){
        Col col = new Col(0xff0000);
        vertexBuffer.add(new Vertex(0,0,0.5, col));
        vertexBuffer.add(new Vertex(0.75,0,0.5, col));
        vertexBuffer.add(new Vertex(1,0,0.5, col));
        vertexBuffer.add(new Vertex(0.75,0.2,0.5, col));
        vertexBuffer.add(new Vertex(0.75,-0.2,0.5, col));


        indexBuffer.add(0);
        indexBuffer.add(1);

        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);


        partBuffer.add(new Part(TopologyType.LINE, 0, 1));
        partBuffer.add(new Part(TopologyType.TRIANGLE, 2, 1));

    }
}
