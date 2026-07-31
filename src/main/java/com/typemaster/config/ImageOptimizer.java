package com.typemaster.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * 2026-07-31 Full-Stack Image Optimization:
 * Automatically compresses and resizes large PNG/JPG assets on startup.
 * Skips compression if files are already optimized (checked via size thresholds).
 */
@Component
public class ImageOptimizer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        optimizeLogo();
        optimizeBlogImages();
    }

    private void optimizeLogo() {
        File pngFile = new File("src/main/resources/static/assets/images/logo.png");
        File jpgFile = new File("src/main/resources/static/assets/images/logo.jpg");
        File icoFile = new File("src/main/resources/static/favicon.ico");

        // If logo.png is already compressed (< 15KB), skip optimization
        if (pngFile.exists() && pngFile.length() < 15000) {
            System.out.println("[ImageOptimizer] Logo images are already optimized. Skipping.");
            return;
        }

        System.out.println("[ImageOptimizer] Starting logo optimization...");
        try {
            if (pngFile.exists()) {
                BufferedImage src = ImageIO.read(pngFile);
                if (src != null) {
                    // Resize logo.png to 180x180
                    BufferedImage pngDest = resizeImage(src, 180, 180);
                    ImageIO.write(pngDest, "png", pngFile);
                    System.out.println("[ImageOptimizer] logo.png optimized successfully! Size: " + (pngFile.length() / 1024) + " KB");

                    // Overwrite logo.jpg with a highly compressed JPEG version
                    BufferedImage jpgDest = resizeImage(src, 180, 180);
                    writeCompressedJpeg(jpgDest, jpgFile, 0.8f);
                    System.out.println("[ImageOptimizer] logo.jpg optimized successfully! Size: " + (jpgFile.length() / 1024) + " KB");

                    // Overwrite favicon.ico with a 32x32 PNG (renamed to .ico for browser compatibility)
                    BufferedImage icoDest = resizeImage(src, 32, 32);
                    ImageIO.write(icoDest, "png", icoFile);
                    System.out.println("[ImageOptimizer] favicon.ico optimized successfully! Size: " + (icoFile.length() / 1024) + " KB");
                }
            }
        } catch (IOException e) {
            System.err.println("[ImageOptimizer] Error optimizing logo images: " + e.getMessage());
        }
    }

    private void optimizeBlogImages() {
        File blogDir = new File("src/main/resources/static/assets/images/blog");
        if (!blogDir.exists() || !blogDir.isDirectory()) {
            return;
        }

        File[] files = blogDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
        if (files == null) return;

        for (File file : files) {
            // Skip if already optimized (< 50KB)
            if (file.length() < 50000) {
                continue;
            }

            System.out.println("[ImageOptimizer] Optimizing blog image: " + file.getName());
            try {
                BufferedImage src = ImageIO.read(file);
                if (src != null) {
                    // Resize to a maximum width of 600px, keeping aspect ratio
                    int width = src.getWidth();
                    int height = src.getHeight();
                    if (width > 600) {
                        double ratio = (double) height / width;
                        width = 600;
                        height = (int) (width * ratio);
                    }
                    BufferedImage dest = resizeImage(src, width, height);

                    // Write back as a compressed image
                    if (file.getName().toLowerCase().endsWith(".png")) {
                        ImageIO.write(dest, "png", file);
                    } else {
                        writeCompressedJpeg(dest, file, 0.85f);
                    }
                    System.out.println("[ImageOptimizer] " + file.getName() + " optimized! New Size: " + (file.length() / 1024) + " KB");
                }
            } catch (IOException e) {
                System.err.println("[ImageOptimizer] Error optimizing " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage src, int targetWidth, int targetHeight) {
        BufferedImage dest = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dest.createGraphics();
        g2d.drawImage(src, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return dest;
    }

    private void writeCompressedJpeg(BufferedImage img, File destFile, float quality) throws IOException {
        // Convert to RGB since JPEG doesn't support alpha channel transparency
        BufferedImage rgbImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgbImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No JPG image writer available");
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (FileImageOutputStream out = new FileImageOutputStream(destFile)) {
            writer.setOutput(out);
            writer.write(null, new IIOImage(rgbImage, null, null), param);
        } finally {
            writer.dispose();
        }
    }
}
