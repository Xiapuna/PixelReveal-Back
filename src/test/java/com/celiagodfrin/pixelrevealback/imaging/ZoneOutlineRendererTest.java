package com.celiagodfrin.pixelrevealback.imaging;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZoneOutlineRendererTest {

    private int[][] createTwoZoneGrid() {
        int[][] zoneIds = new int[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                zoneIds[x][y] = x < 2 ? 0 : 1;
            }
        }
        return zoneIds;
    }

    @Test
    void computeZoneCentroids_shouldReturnAveragePosition() {
        int[][] zoneIds = createTwoZoneGrid();
        ZoneOutlineRenderer renderer = new ZoneOutlineRenderer();

        Map<Integer, Point> centroids = renderer.computeZoneCentroids(zoneIds, 4, 4);

        assertEquals(new Point(0, 1), centroids.get(0));
        assertEquals(new Point(2, 1), centroids.get(1));
    }

    @Test
    void renderOutline_shouldDrawBlackBorderBetweenZones() {
        int[][] zoneIds = createTwoZoneGrid();
        ZoneOutlineRenderer renderer = new ZoneOutlineRenderer();

        PixelImage outline = renderer.renderOutline(zoneIds, 4, 4);

        assertEquals(Color.WHITE, outline.getPixelColor(0, 0));
        assertEquals(Color.BLACK, outline.getPixelColor(1, 0));
        assertEquals(Color.BLACK, outline.getPixelColor(2, 0));
        assertEquals(Color.WHITE, outline.getPixelColor(3, 0));
    }
}