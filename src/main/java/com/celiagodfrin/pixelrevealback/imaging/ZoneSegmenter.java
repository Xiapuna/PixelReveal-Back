package com.celiagodfrin.pixelrevealback.imaging;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class ZoneSegmenter {

    public int[][] segment(PixelImage image) {
        int[][] zoneIds = new int[image.getWidth()][image.getHeight()];
        for (int[] column : zoneIds) {
            Arrays.fill(column, -1);
        }

        int nextZoneId = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (zoneIds[x][y] == -1) {
                    floodFill(image, x, y, zoneIds, nextZoneId);
                    nextZoneId++;
                }
            }
        }

        return zoneIds;
    }

    private void floodFill(PixelImage image, int startX, int startY, int[][] zoneIds, int zoneId) {
        Color targetColor = image.getPixelColor(startX, startY);

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{startX, startY});

        while (!stack.isEmpty()) {
            int[] position = stack.pop();
            int x = position[0];
            int y = position[1];

            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
                continue;
            }
            if (zoneIds[x][y] != -1) {
                continue;
            }
            if (!image.getPixelColor(x, y).equals(targetColor)) {
                continue;
            }

            zoneIds[x][y] = zoneId;

            stack.push(new int[]{x + 1, y});
            stack.push(new int[]{x - 1, y});
            stack.push(new int[]{x, y + 1});
            stack.push(new int[]{x, y - 1});
        }
    }
}
