package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex>{
    private Point3D position;
    private Col color;
    // TODO: normála, souřadnice do textury


    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x,y,z);
        this.color = new Col(0xff0000);
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    @Override
    public Vertex mul(double k) {
        // TODO: k přinásobit ke všem atributům
        return null;
    }

    @Override
    public Vertex add(Vertex vertex) {
        // TODO: přičte jednotlivé atributy
        return null;
    }

    // TODO: společný IF?
    // TODO: metoda pro transformaci - vstup matice
    // TODO: dehomogenizace

    public Vertex transformToWindow(int width, int height){
        Vec3D p = position.ignoreW()
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width-1)/2., (height-1)/2., 1));
        return new Vertex(p.x, p.y, p.z);
    }

}
