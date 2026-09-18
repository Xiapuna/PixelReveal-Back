package com.celiagodfrin.pixelrevealback.imaging;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ZoneNumberer {

    public record ZoneNumberingResult(Map<Integer, Integer> zoneNumbers, Map<Integer, Color> legend) {}

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

    public ZoneNumberingResult numberZones(int[][] zoneIds, PixelImage image) {
        Map<Integer, Color> zoneColors = computeZoneColors(image, zoneIds, image.getWidth(), image.getHeight());

        Map<Color, Integer> colorToNumber = new HashMap<>();
        Map<Integer, Color> legend = new HashMap<>();
        Map<Integer, Integer> zoneNumbers = new HashMap<>();
        int nextNumber = 1;

        for (Map.Entry<Integer, Color> entry : zoneColors.entrySet()) {
            int zoneId = entry.getKey();
            Color color = entry.getValue();

            Integer number = colorToNumber.get(color);
            if (number == null) {
                number = nextNumber;
                colorToNumber.put(color, number);
                legend.put(number, color);
                nextNumber++;
            }

            zoneNumbers.put(zoneId, number);
        }

        return new ZoneNumberingResult(zoneNumbers, legend);
    }
}