package model;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex>{
    private Point3D position;
    private Col color;
    // TODO: normála, souřadnice do textury

    public Vertex(double x, double y, double z, Col color) {
//        this.color = new Col(0xff00ff);
        this.color = color;
        this.position = new Point3D(x,y,z);
    }

    public Vertex(Point3D mul) {
        this.position = mul;
    }

    public Vertex(Point3D mul, Col color) {
        this.position = mul;
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    @Override
    public Vertex mul(double k) {
        //Done: k přinásobit ke všem atributům
        return new Vertex(position.getX() * k , position.getY() * k, position.getZ() * k, this.getColor().mul(k));
    }

    @Override
    public Vertex add(Vertex vertex) {
        //Done: přičte jednotlivé atributy
        return new Vertex(position.getX() + vertex.getPosition().getX(), position.getY() + vertex.getPosition().getY(), position.getZ() + vertex.getPosition().getZ(), this.getColor().add(vertex.getColor()));
    }

    // TODO: společný IF?

    // Done: metoda pro transformaci - vstup matice
    public Vertex transform(final Mat4 mat) {
        return new Vertex(position.mul(mat), this.getColor());
    }

    public Vertex transformToWindow(int width, int height){
        Vec3D p = position.ignoreW()
                .mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width-1)/2., (height-1)/2., 1));
        return new Vertex(p.getX(), p.getY(), p.getZ(), this.getColor());
    }

    // Done: dehomogenizace
    public Vertex dehomog(Vertex v) {
        double w = v.getW();
        return new Vertex(position.getX()/w, position.getY()/w, position.getZ()/w, v.getColor());
    }

    public double getX(){
        return position.getX();
    }
    public double getY(){
        return position.getY();
    }
    public double getZ(){
        return position.getZ();
    }
    public double getW(){
        return position.getW();
    }

}
