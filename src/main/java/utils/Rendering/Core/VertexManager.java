package utils.Rendering.Core;

import utils.Rendering.Utils.VertexLayoutManager;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class VertexManager {

    private final int vertexArrayId;
    private final int vertexBufferId;

    public VertexManager(int vc, int vs, ArrayList<VertexLayoutManager.VertexLayout> paramLayout) {

        // create vertex array
        vertexArrayId = glGenVertexArrays();

        // bind things to the vertex array
        glBindVertexArray(vertexArrayId);

        // create a vertex buffer
        vertexBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, vc * (vs * 4L), GL_DYNAMIC_DRAW);

        for (int i = 0; i < paramLayout.size(); i++) {
            final var param = paramLayout.get(i);

            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, param.count, GL_FLOAT, false, vs * 4, param.offset);
        }

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void destroy() {
        glDeleteVertexArrays(vertexArrayId);
        glDeleteBuffers(vertexBufferId);
    }

    public void bind() {
        glBindVertexArray(vertexArrayId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void writeFloatBuffer(FloatBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
