package raster;

public interface Raster<E> {

    void clear();

    void setClearElement(E element);

    int getWidth();

    int getHeight();

    E getValue(int x, int y);

    void setValue(int x, int y, E element);

    default boolean isInside(int x, int y){
        return (x >= 0 && x < getWidth() && y >= 0 && y < getHeight());
    };
}
