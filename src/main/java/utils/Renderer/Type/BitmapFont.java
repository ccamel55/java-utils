package utils.Renderer.Type;

import java.awt.*;
import java.awt.image.*;

import java.util.*;

public class BitmapFont {
    public BufferedImage pTextureImg;
    public Map<Integer, FontInfo> mCharacterMap;

    public BitmapFont(String fontFamily, int fontSize) {
        mCharacterMap = new HashMap<>();

        // create a fake buffered image to get some metrics for our font
        final var fontFamilyFont = new Font(fontFamily, Font.PLAIN, fontSize);
        final var tempBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        final var graphics2D = tempBufferedImage.createGraphics();
        graphics2D.setFont(fontFamilyFont);

        final var fontMetrics = graphics2D.getFontMetrics();
        final var estimatedWidth = (int)Math.sqrt(fontFamilyFont.getNumGlyphs()) * fontFamilyFont.getSize() + 1;

        int width = 0;
        int height = fontMetrics.getHeight();

        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);

        for (int i=0; i < fontFamilyFont.getNumGlyphs(); i++) {
            if (fontFamilyFont.canDisplay(i)) {
                final var fontInfo = new FontInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                mCharacterMap.put(i, fontInfo);

                width = Math.max(x + fontMetrics.charWidth(i), width);
                x += fontInfo.iWidth;

                if (x > estimatedWidth) {
                    x = 0;
                    y += fontMetrics.getHeight() * 1.4f;
                    height += fontMetrics.getHeight() * 1.4f;
                }
            }
        }

        height += fontMetrics.getHeight() * 1.4f;
        graphics2D.dispose();

        // now since we have some metrics we want to build a real buffered image that contains our bitmap for the font
        pTextureImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final var graphics2DFont = pTextureImg.createGraphics();

        graphics2DFont.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2DFont.setFont(fontFamilyFont);
        graphics2DFont.setColor(Color.WHITE);

        for (int i = 0; i < fontFamilyFont.getNumGlyphs(); i++) {
            if (fontFamilyFont.canDisplay(i)) {
                final var info = mCharacterMap.get(i);
                info.calcTextureCoOrdinates(width, height);

                graphics2DFont.drawString("" + (char)i, info.iSourceX, info.iSourceY);
            }
        }

        graphics2DFont.dispose();
    }
}
