package utils.Renderer.Type;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int iShaderId;

    public Shader(String vertexShader, String fragmentShader) {
        final var vs = genShader(vertexShader, GL_VERTEX_SHADER);
        final var fs = genShader(fragmentShader, GL_FRAGMENT_SHADER);

        // create shader program and assign shaders we compiled to it
        iShaderId = glCreateProgram();

        glAttachShader(iShaderId, vs);
        glAttachShader(iShaderId, fs);

        glLinkProgram(iShaderId);
        glValidateProgram(iShaderId);

        // nuke it we don't need anymore
        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    public void destroy() {
        glDeleteProgram(iShaderId);
    }

    public void bind() {
        glUseProgram(iShaderId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void bindUniformMat4(String uniformName, boolean transpose, float[] matrix) {
        bind();
        glUniformMatrix4fv(glGetUniformLocation(iShaderId, uniformName), false, matrix);
        unbind();
    }

    public void bindUniform(String uniformName, int v0) {
        bind();
        glUniform1i(glGetUniformLocation(iShaderId, uniformName), v0);
        unbind();
    }

    private int genShader(String shader, int type) {

        final var id = glCreateShader(type);
        glShaderSource(id, shader);
        glCompileShader(id);

        // print any errors in shader compilation
        if (glGetShaderi(id, GL_COMPILE_STATUS) != 1) {
            glDeleteShader(id);

            System.err.println(glGetShaderInfoLog(id));
            System.exit(1);
        }

        return id;
    }
}
