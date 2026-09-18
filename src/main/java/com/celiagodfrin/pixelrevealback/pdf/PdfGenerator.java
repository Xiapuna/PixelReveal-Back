package com.celiagodfrin.pixelrevealback.pdf;

import com.celiagodfrin.pixelrevealback.imaging.PixelImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class PdfGenerator {

    private static final float MARGIN = 40f;
    private static final float LEGEND_HEIGHT = 150f;

    public void generate(PixelImage outlineImage, File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, outlineImage.getBufferedImage());

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            float availableWidth = pageWidth - 2 * MARGIN;
            float availableHeight = pageHeight - 2 * MARGIN - LEGEND_HEIGHT;

            float scale = Math.min(availableWidth / outlineImage.getWidth(), availableHeight / outlineImage.getHeight());
            float drawnWidth = outlineImage.getWidth() * scale;
            float drawnHeight = outlineImage.getHeight() * scale;

            float x = MARGIN + (availableWidth - drawnWidth) / 2;
            float y = pageHeight - MARGIN - drawnHeight;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, x, y, drawnWidth, drawnHeight);
            }

            document.save(outputFile);
        }
    }
}