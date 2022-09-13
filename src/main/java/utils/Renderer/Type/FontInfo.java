package utils.Renderer.Type;

import glm.vec._2.Vec2;

public class FontInfo {
    public int iSourceX;
    public int iSourceY;
    public int iWidth;
    public int iHeight;
    public Vec2[] lTextureCoOrdinates;

    public FontInfo(int sourceX, int sourceY, int width, int height) {
        iSourceX = sourceX;
        iSourceY = sourceY;
        iWidth = width;
        iHeight = height;
        lTextureCoOrdinates = new Vec2[2];
    }

    public void calcTextureCoOrdinates(int textureWidth, int textureHeight) {
        final var x0 = (float)iSourceX / (float)textureWidth;
        final var y0 = (float)(iSourceY - iHeight) / (float)textureHeight;

        final var x1 = (float)(iSourceX + iWidth) / (float)textureWidth;
        final var y1 = (float)iSourceY / (float)textureHeight;

        lTextureCoOrdinates[0] = new Vec2(x0, y0);
        lTextureCoOrdinates[1] = new Vec2(x1, y1);
    }
}
