package utils.Rendering.Core;

import glm.vec._2.Vec2;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class FontHelper {

    public static class TexInfo {
        public int sourceX;
        public int sourceY;
        public int width;
        public int height;

        public Vec2[] textureCoordinates = new Vec2[2];

        public TexInfo(int sourceX, int sourceY, int width, int height) {
            this.sourceX = sourceX;
            this.sourceY = sourceY;
            this.width = width;
            this.height = height;
        }

        public void calculateTextureCoordinates(int fontWidth, int fontHeight) {
            float x0 = (float)sourceX / (float)fontWidth;
            float x1 = (float)(sourceX + width) / (float)fontWidth;
            float y0 = (float)(sourceY - height) / (float)fontHeight;
            float y1 = (float)(sourceY) / (float)fontHeight;

            textureCoordinates[0] = new Vec2(x0, y1);
            textureCoordinates[1] = new Vec2(x1, y0);
        }
    }

    public BufferedImage img;
    public Map<Integer, TexInfo> characterMap ;

    public FontHelper(String fontName, int Size) {
        characterMap  = new HashMap<>();
        genBitmap(new Font(fontName, Font.BOLD, Size));
    }

    public void genBitmap(Font font) {

        // Create fake image to get font information
        img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;

        int width = 0;
        int height = fontMetrics.getHeight();

        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);

        for (int i=0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) {
                // Get the sizes for each codepoint glyph, and update the actual image width and height
                TexInfo charInfo = new TexInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                characterMap.put(i, charInfo);
                width = Math.max(x + fontMetrics.charWidth(i), width);

                x += charInfo.width;
                if (x > estimatedWidth) {
                    x = 0;
                    y += fontMetrics.getHeight() * 1.4f;
                    height += fontMetrics.getHeight() * 1.4f;
                }
            }
        }

        height += fontMetrics.getHeight() * 1.4f;
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);

        for (int i=0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) {
                TexInfo info = characterMap.get(i);
                info.calculateTextureCoordinates(width, height);
                g2d.drawString("" + (char)i, info.sourceX, info.sourceY);
            }
        }

        g2d.dispose();
    }
}
