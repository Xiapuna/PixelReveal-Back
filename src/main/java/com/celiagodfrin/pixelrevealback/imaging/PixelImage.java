package com.celiagodfrin.pixelrevealback.imaging;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PixelImage {

    private BufferedImage image;

    public void load(File file) throws IOException {
        image = ImageIO.read(file);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Color getPixelColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    public void setPixelColor(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public void save(File file) throws IOException {
        ImageIO.write(image, "jpg", file);
    }

    public void create(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage getBufferedImage() {
        return image;
    }
}
