package model;

import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Vertex> vertexBuffer = new ArrayList<>();
    protected List<Integer> indexBuffer = new ArrayList<>();
    protected List<Part> partBuffer = new ArrayList<>();
    private Mat4 model = new Mat4Identity();

    public List<Part> getPartBuffer() {
        return partBuffer;
    }
    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }
    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }
    protected void addIndices(Integer... indices){
        indexBuffer.addAll(Arrays.asList(indices));
    }
}
