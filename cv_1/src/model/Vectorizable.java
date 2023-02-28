package model;

public interface Vectorizable<V> {
    V mul(double k);
    V add(V v);
}
