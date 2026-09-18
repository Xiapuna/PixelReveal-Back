package com.celiagodfrin.pixelrevealback.pdf;

import com.celiagodfrin.pixelrevealback.imaging.PixelImage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void generate_shouldProduceValidSinglePagePdf() throws IOException {
        PixelImage outlineImage = new PixelImage();
        outlineImage.create(100, 100);
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                outlineImage.setPixelColor(x, y, Color.WHITE);
            }
        }

        File outputFile = tempDir.resolve("output.pdf").toFile();
        PdfGenerator generator = new PdfGenerator();
        generator.generate(outlineImage, outputFile);

        assertTrue(outputFile.exists());

        try (PDDocument document = Loader.loadPDF(outputFile)) {
            assertEquals(1, document.getNumberOfPages());
        }
    }
}