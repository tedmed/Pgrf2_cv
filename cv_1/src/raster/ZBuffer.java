package raster;

import transforms.Col;

import java.awt.*;

public class ZBuffer {
    private ImageBuffer imageBuffer;
    private DepthBuffer depthBuffer;

    public ZBuffer(ImageBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
        depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
        depthBuffer.clear();
    }

    public void drawWithZTest(int x, int y, double z, Col color){
        // načíst depthbuffer hodnotu na pozici x,y
        // porovnám ji s z (old), které vstupuje do této metody (new)
        // if new < old, tak old = new, obarvím pixel
        System.out.println(x + "|" + y);
        if(depthBuffer.getValue(x, y) > z){
            imageBuffer.setValue(x, y, color);
            depthBuffer.setValue(x, y, z);
        }
    }

    public Graphics getGraphics(){
        return imageBuffer.getGraphics();
    }

    public int getWidth(){
        return imageBuffer.getWidth();
    }

    public int getHeight(){
        return imageBuffer.getHeight();
    }
}
