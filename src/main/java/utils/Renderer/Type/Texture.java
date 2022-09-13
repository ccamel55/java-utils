package utils.Renderer.Type;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {
    private final int iTextureId;

    public Texture(BufferedImage bufferedImage, boolean alphaOnly) {
        iTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, iTextureId);

        //settings for texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        //sent to openGL
        final var imgByteBuffer = loadTextureAlpha(bufferedImage, alphaOnly);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imgByteBuffer);

        // cleanup stuff
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy() {
        glDeleteTextures(iTextureId);
    }

    public void bind(int slot) {
        glActiveTexture(slot);
        glBindTexture(GL_TEXTURE_2D, iTextureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId () {
        return iTextureId;
    }

    // some dude on stack overflow, shout out the homie
    private ByteBuffer loadTextureAlpha(BufferedImage image, boolean alphaOnly){

        int[] pixels = new int[image.getHeight() * image.getWidth()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        final var buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y=0; y < image.getHeight(); y++) {
            for (int x=0; x < image.getWidth(); x++) {
                final var pixel = pixels[y * image.getWidth() + x];
                final var alphaComponent = (byte)((pixel >> 24) & 0xFF);

                if (alphaOnly) {
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                    buffer.put(alphaComponent);
                }
                else {
                    buffer.put((byte)((pixel >> 16) & 0xFF));
                    buffer.put((byte)((pixel >> 8) & 0xFF));
                    buffer.put((byte)(pixel & 0xFF));
                    buffer.put(alphaComponent);
                }
            }
        }

        buffer.flip();
        return buffer;
    }
}
