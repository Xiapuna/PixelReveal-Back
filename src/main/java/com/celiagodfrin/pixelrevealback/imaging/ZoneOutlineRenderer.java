package com.celiagodfrin.pixelrevealback.imaging;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ZoneOutlineRenderer {

    public Map<Integer, Point> computeZoneCentroids(int[][] zoneIds, int width, int height) {
        Map<Integer, long[]> sums = new HashMap<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int zoneId = zoneIds[x][y];
                long[] sum = sums.computeIfAbsent(zoneId, k -> new long[3]);
                sum[0] += x;
                sum[1] += y;
                sum[2] += 1;
            }
        }

        Map<Integer, Point> centroids = new HashMap<>();
        for (Map.Entry<Integer, long[]> entry : sums.entrySet()) {
            long[] sum = entry.getValue();
            int averageX = (int) (sum[0] / sum[2]);
            int averageY = (int) (sum[1] / sum[2]);
            centroids.put(entry.getKey(), new Point(averageX, averageY));
        }

        return centroids;
    }

    public PixelImage renderOutline(int[][] zoneIds, int width, int height) {
        PixelImage outlineImage = new PixelImage();
        outlineImage.create(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = isBoundaryPixel(zoneIds, x, y, width, height) ? Color.BLACK : Color.WHITE;
                outlineImage.setPixelColor(x, y, color);
            }
        }

        return outlineImage;
    }

    private boolean isBoundaryPixel(int[][] zoneIds, int x, int y, int width, int height) {
        int zoneId = zoneIds[x][y];

        if (x > 0 && zoneIds[x - 1][y] != zoneId) return true;
        if (x + 1 < width && zoneIds[x + 1][y] != zoneId) return true;
        if (y > 0 && zoneIds[x][y - 1] != zoneId) return true;
        if (y + 1 < height && zoneIds[x][y + 1] != zoneId) return true;

        return false;
    }
}