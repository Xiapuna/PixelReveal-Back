package com.celiagodfrin.pixelrevealback.imaging;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ZoneCleaner {

    private Map<Integer, Integer> computeZoneSizes(int[][] zoneIds, int width, int height) {
        Map<Integer, Integer> zoneSizes = new HashMap<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int zoneId = zoneIds[x][y];
                zoneSizes.merge(zoneId, 1, Integer::sum);
            }
        }

        return zoneSizes;
    }

    private Map<Integer, Color> computeZoneColors(PixelImage image, int[][] zoneIds, int width, int height) {
        Map<Integer, Color> zoneColors = new HashMap<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int zoneId = zoneIds[x][y];
                zoneColors.putIfAbsent(zoneId, image.getPixelColor(x, y));
            }
        }

        return zoneColors;
    }

    private double distanceSquared(Color a, Color b) {
        int dr = a.getRed() - b.getRed();
        int dg = a.getGreen() - b.getGreen();
        int db = a.getBlue() - b.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    private void addNeighborIfDifferent(int[][] zoneIds, int x, int y, int zoneId, Set<Integer> neighborZoneIds, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        if (zoneIds[x][y] != zoneId) {
            neighborZoneIds.add(zoneIds[x][y]);
        }
    }

    private Integer findNearestNeighborZone(int[][] zoneIds, int zoneId, Map<Integer, Color> zoneColors, int width, int height) {
        Set<Integer> neighborZoneIds = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (zoneIds[x][y] != zoneId) {
                    continue;
                }
                addNeighborIfDifferent(zoneIds, x - 1, y, zoneId, neighborZoneIds, width, height);
                addNeighborIfDifferent(zoneIds, x + 1, y, zoneId, neighborZoneIds, width, height);
                addNeighborIfDifferent(zoneIds, x, y - 1, zoneId, neighborZoneIds, width, height);
                addNeighborIfDifferent(zoneIds, x, y + 1, zoneId, neighborZoneIds, width, height);
            }
        }

        Color ownColor = zoneColors.get(zoneId);
        Integer closestZoneId = null;
        double closestDistance = Double.MAX_VALUE;

        for (int neighborZoneId : neighborZoneIds) {
            double distance = distanceSquared(ownColor, zoneColors.get(neighborZoneId));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestZoneId = neighborZoneId;
            }
        }

        return closestZoneId;
    }

    private void mergeZoneInto(PixelImage image, int[][] zoneIds, int zoneId, int targetZoneId, Color targetColor, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (zoneIds[x][y] == zoneId) {
                    zoneIds[x][y] = targetZoneId;
                    image.setPixelColor(x, y, targetColor);
                }
            }
        }
    }

    public void clean(PixelImage image, int[][] zoneIds, double minZoneSizeRatio) {
        int width = image.getWidth();
        int height = image.getHeight();
        int minPixelCount = (int) (width * height * minZoneSizeRatio);

        Map<Integer, Integer> zoneSizes = computeZoneSizes(zoneIds, width, height);
        Map<Integer, Color> zoneColors = computeZoneColors(image, zoneIds, width, height);

        for (Map.Entry<Integer, Integer> entry : zoneSizes.entrySet()) {
            int zoneId = entry.getKey();
            int size = entry.getValue();

            if (size >= minPixelCount) {
                continue;
            }

            Integer nearestZoneId = findNearestNeighborZone(zoneIds, zoneId, zoneColors, width, height);
            if (nearestZoneId == null) {
                continue;
            }

            Color targetColor = zoneColors.get(nearestZoneId);
            mergeZoneInto(image, zoneIds, zoneId, nearestZoneId, targetColor, width, height);
        }
    }
}