package Objects;

import model.Part;
import model.Solid;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cube extends Solid {
    public Cube(){
        Col col1 = new Col(0xffff00);
        Col col2 = new Col(0x00ff00);
        Col col3 = new Col(0xff00ff);
        Col col4 = new Col(0x00ffff);
        vertexBuffer.add(new Vertex(0,-1,1,col1));
        vertexBuffer.add(new Vertex(2,-1,1,col2));
        vertexBuffer.add(new Vertex(2,1,1,col3));
        vertexBuffer.add(new Vertex(0,1,1,col4));
        vertexBuffer.add(new Vertex(0,-1,-1,col3));
        vertexBuffer.add(new Vertex(2,-1,-1,col4));
        vertexBuffer.add(new Vertex(2,1,-1,col1));
        vertexBuffer.add(new Vertex(0,1,-1,col2));

        addIndices(0,1,2, 0,2,3, 0,1,5, 0,4,5, 0,3,4, 3,4,7, 2,3,6, 3,6,7, 1,2,5, 2,5,6, 4,5,7, 5,6,7);

        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 12));

    }
}
