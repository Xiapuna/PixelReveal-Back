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

class ZoneNumbererTest {

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
    void numberZones_shouldAssignDifferentNumbersToDifferentColors() throws IOException {
        File file = createTwoZoneTestImage();
        PixelImage image = new PixelImage();
        image.load(file);

        ZoneSegmenter segmenter = new ZoneSegmenter();
        int[][] zoneIds = segmenter.segment(image);

        ZoneNumberer numberer = new ZoneNumberer();
        ZoneNumberer.ZoneNumberingResult result = numberer.numberZones(zoneIds, image);

        int redZoneId = zoneIds[0][0];
        int blueZoneId = zoneIds[2][0];

        int redNumber = result.zoneNumbers().get(redZoneId);
        int blueNumber = result.zoneNumbers().get(blueZoneId);

        assertEquals(2, result.legend().size());
        assertNotEquals(redNumber, blueNumber);
        assertEquals(Color.RED, result.legend().get(redNumber));
        assertEquals(Color.BLUE, result.legend().get(blueNumber));
    }
}