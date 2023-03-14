package shaders;

import model.Vertex;
import transforms.Col;

public interface Shader {
    Col shade(Vertex v);

}
