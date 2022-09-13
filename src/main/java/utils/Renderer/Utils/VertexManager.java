package utils.Renderer.Utils;

import utils.Renderer.Core.VertexLayoutGenerator;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class VertexManager {
    private final int iVertexArrayId;
    private final int iVertexBufferId;

    public VertexManager(int vertexCount, int vertexSize, VertexLayoutGenerator paramLayout) {
        // create vertex array
        iVertexArrayId = glGenVertexArrays();

        // bind things to the vertex array
        glBindVertexArray(iVertexArrayId);

        // create a vertex buffer
        iVertexBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, iVertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, vertexCount * (vertexSize * 4L), GL_DYNAMIC_DRAW);

        for (int i = 0; i < paramLayout.aLayout.size(); i++) {
            final var param = paramLayout.aLayout.get(i);

            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, param.iCount, GL_FLOAT, false, vertexSize * 4, param.iOffset);
        }

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glDeleteVertexArrays(iVertexArrayId);
        glDeleteBuffers(iVertexBufferId);
    }

    public void bind() {
        glBindVertexArray(iVertexArrayId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void writeFloatBuffer(FloatBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, iVertexBufferId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
