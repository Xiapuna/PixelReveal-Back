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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ZoneSegmenterTest {

    @TempDir
    Path tempDir;

    private File createTwoZoneTestImage() throws IOException {
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
    void segment_shouldAssignSameZoneToSameColorRegion() throws IOException {
        File file = createTwoZoneTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        ZoneSegmenter segmenter = new ZoneSegmenter();
        int[][] zoneIds = segmenter.segment(image);

        assertEquals(zoneIds[0][0], zoneIds[1][3]);
        assertEquals(zoneIds[2][0], zoneIds[3][3]);
    }

    @Test
    void segment_shouldAssignDifferentZonesToDifferentColorRegions() throws IOException {
        File file = createTwoZoneTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        ZoneSegmenter segmenter = new ZoneSegmenter();
        int[][] zoneIds = segmenter.segment(image);

        assertNotEquals(zoneIds[0][0], zoneIds[2][0]);
    }
}