package model;

public class Lerp<V extends Vectorizable<V>> {
    public V lerp(V v1, V v2, double t){
        // proběhne interpolace
        //Done: implementovat
        v1.mul(1-t).add(v2.mul(t));
        return null;
    }
}
