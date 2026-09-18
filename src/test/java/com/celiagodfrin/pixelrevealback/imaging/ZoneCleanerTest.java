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

class ZoneCleanerTest {

    @TempDir
    Path tempDir;

    private File createImageWithStrayPixel() throws IOException {
        BufferedImage testImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                testImage.setRGB(x, y, Color.BLUE.getRGB());
            }
        }
        testImage.setRGB(1, 1, Color.RED.getRGB());

        File file = tempDir.resolve("test.png").toFile();
        ImageIO.write(testImage, "png", file);
        return file;
    }

    @Test
    void clean_shouldMergeStrayPixelIntoNeighborZone() throws IOException {
        File file = createImageWithStrayPixel();
        PixelImage image = new PixelImage();
        image.load(file);

        ZoneSegmenter segmenter = new ZoneSegmenter();
        int[][] zoneIds = segmenter.segment(image);

        ZoneCleaner cleaner = new ZoneCleaner();
        cleaner.clean(image, zoneIds, 0.15);

        assertEquals(Color.BLUE, image.getPixelColor(1, 1));
        assertEquals(zoneIds[0][0], zoneIds[1][1]);
    }
}