package com.celiagodfrin.pixelrevealback.imaging;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansColorReducer {

    private final Random random;

    public KMeansColorReducer() {
        this(new Random());
    }

    public KMeansColorReducer(Random random) {
        this.random = random;
    }

    private double distanceSquared(Color a, Color b) {
        int dr = a.getRed() - b.getRed();
        int dg = a.getGreen() - b.getGreen();
        int db = a.getBlue() - b.getBlue();
        return dr * dr + dg * dg + db * db;
    }

    private List<Color> initializeCentroids(PixelImage image, int k) {
        List<Color> centroids = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int x = random.nextInt(image.getWidth());
            int y = random.nextInt(image.getHeight());
            centroids.add(image.getPixelColor(x, y));
        }

        return centroids;
    }

    private int[][] assignPixelsToClusters(PixelImage image, List<Color> centroids) {
        int[][] clusterIndices = new int[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = image.getPixelColor(x, y);

                int closestIndex = 0;
                double closestDistance = distanceSquared(pixelColor, centroids.get(0));

                for (int i = 1; i < centroids.size(); i++) {
                    double distance = distanceSquared(pixelColor, centroids.get(i));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestIndex = i;
                    }
                }

                clusterIndices[x][y] = closestIndex;
            }
        }

        return clusterIndices;
    }

    private List<Color> updateCentroids(PixelImage image, int[][] clusterIndices, List<Color> previousCentroids) {
        int k = previousCentroids.size();
        long[] sumRed = new long[k];
        long[] sumGreen = new long[k];
        long[] sumBlue = new long[k];
        int[] count = new int[k];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int clusterIndex = clusterIndices[x][y];
                Color pixelColor = image.getPixelColor(x, y);

                sumRed[clusterIndex] += pixelColor.getRed();
                sumGreen[clusterIndex] += pixelColor.getGreen();
                sumBlue[clusterIndex] += pixelColor.getBlue();
                count[clusterIndex]++;
            }
        }

        List<Color> newCentroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (count[i] == 0) {
                newCentroids.add(previousCentroids.get(i));
            } else {
                int averageRed = (int) (sumRed[i] / count[i]);
                int averageGreen = (int) (sumGreen[i] / count[i]);
                int averageBlue = (int) (sumBlue[i] / count[i]);
                newCentroids.add(new Color(averageRed, averageGreen, averageBlue));
            }
        }

        return newCentroids;
    }

    public List<Color> reduce(PixelImage image, int k, int iterations) {
        List<Color> centroids = initializeCentroids(image, k);

        for (int i = 0; i < iterations; i++) {
            int[][] clusterIndices = assignPixelsToClusters(image, centroids);
            centroids = updateCentroids(image, clusterIndices, centroids);
        }

        int[][] finalClusterIndices = assignPixelsToClusters(image, centroids);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setPixelColor(x, y, centroids.get(finalClusterIndices[x][y]));
            }
        }

        return centroids;
    }
}
