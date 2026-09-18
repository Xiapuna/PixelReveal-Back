package com.celiagodfrin.pixelrevealback.imaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PixelImageTest {

    @TempDir
    Path tempDir;

    private File createTestImage() throws IOException {
        BufferedImage testImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, Color.RED.getRGB());
        testImage.setRGB(1, 0, Color.GREEN.getRGB());
        testImage.setRGB(0, 1, Color.BLUE.getRGB());
        testImage.setRGB(1, 1, Color.WHITE.getRGB());

        File file = tempDir.resolve("test.png").toFile();
        ImageIO.write(testImage, "png", file);
        return file;
    }

    @Test
    void load_shouldSetCorrectDimensions() throws IOException {
        File file = createTestImage();
        PixelImage pixelImage = new PixelImage();
        pixelImage.load(file);

        assertEquals(2, pixelImage.getWidth());
        assertEquals(2, pixelImage.getHeight());
    }

    private void assertColorCloseTo(Color expected, Color actual, int tolerance) {
        assertEquals(expected.getRed(), actual.getRed(), tolerance);
        assertEquals(expected.getGreen(), actual.getGreen(), tolerance);
        assertEquals(expected.getBlue(), actual.getBlue(), tolerance);
    }

    @Test
    void getPixelColor_shouldReturnExpectedColors() throws IOException {
        File file = createTestImage();
        PixelImage pixelImage = new PixelImage();
        pixelImage.load(file);

        assertEquals(Color.RED, pixelImage.getPixelColor(0, 0));
        assertEquals(Color.GREEN, pixelImage.getPixelColor(1, 0));
        assertEquals(Color.BLUE, pixelImage.getPixelColor(0, 1));
        assertEquals(Color.WHITE, pixelImage.getPixelColor(1, 1));
    }

    @Test
    void setPixelColor_shouldUpdateColorInMemory() throws IOException {
        File file = createTestImage();
        PixelImage pixelImage = new PixelImage();
        pixelImage.load(file);

        pixelImage.setPixelColor(0, 0, Color.YELLOW);

        assertEquals(Color.YELLOW, pixelImage.getPixelColor(0, 0));
    }

    @Test
    void save_shouldProduceReadableFile() throws IOException {
        File sourceFile = createTestImage();
        PixelImage pixelImage = new PixelImage();
        pixelImage.load(sourceFile);

        File savedFile = tempDir.resolve("saved.jpg").toFile();
        pixelImage.save(savedFile);

        PixelImage reloaded = new PixelImage();
        reloaded.load(savedFile);
        assertEquals(2, reloaded.getWidth());
        assertEquals(2, reloaded.getHeight());
    }
}
