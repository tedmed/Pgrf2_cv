package raster;

import java.awt.image.BufferedImage;

public class DepthBuffer implements Raster<Double>{
    private double[][] buffer;
    private int height, width;
    private double clearValue = 1;

    public DepthBuffer(int width, int height) {
        this.height = height;
        this.width = width;

        this.buffer = new double[width][height];
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void clear() {
        //Done: všude nastavit clearValue
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                buffer[i][j] = clearValue;
            }
    }

    @Override
    public void setClearElement(Double value) {

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Double getValue(int x, int y) {
        //Done: implementace a kontrola prvku mimo pole
        if(isInside(x, y)) return buffer[x][y];
        else return null;
    }

    @Override
    public void setValue(int x, int y, Double value) {
        //Done: kontrola zapsání mimo pole
        if(!isInside(x, y)) return;
        buffer[x][y] = value;
    }
}
