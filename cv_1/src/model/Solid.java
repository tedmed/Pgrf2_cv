package model;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Vertex> vertexBuffer = new ArrayList<>();
    protected List<Integer> indexBuffer = new ArrayList<>();
    protected List<Part> partBuffer = new ArrayList<>();

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

}
