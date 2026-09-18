package com.celiagodfrin.pixelrevealback.imaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KMeansColorReducerTest {

    @TempDir
    Path tempDir;

    private File createTwoColorTestImage() throws IOException {
        BufferedImage testImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Color color = x < 2 ? Color.RED : Color.BLUE;
                testImage.setRGB(x, y, color.getRGB());
            }
        }

        File file = tempDir.resolve("test.png").toFile();
        ImageIO.write(testImage, "png", file);
        return file;
    }

    @Test
    void reduce_shouldReturnExactlyKColors() throws IOException {
        File file = createTwoColorTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        KMeansColorReducer reducer = new KMeansColorReducer(new Random(42));
        List<Color> palette = reducer.reduce(image, 2, 10);

        assertEquals(2, palette.size());
    }

    @Test
    void reduce_shouldOnlyUseColorsFromPalette() throws IOException {
        File file = createTwoColorTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        KMeansColorReducer reducer = new KMeansColorReducer(new Random(42));
        List<Color> palette = reducer.reduce(image, 2, 10);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                assertTrue(palette.contains(image.getPixelColor(x, y)));
            }
        }
    }

    @Test
    void reduce_shouldFindRedAndBlueClusters() throws IOException {
        File file = createTwoColorTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        KMeansColorReducer reducer = new KMeansColorReducer(new Random(42));
        List<Color> palette = reducer.reduce(image, 2, 10);

        assertTrue(palette.contains(Color.RED));
        assertTrue(palette.contains(Color.BLUE));
    }
}