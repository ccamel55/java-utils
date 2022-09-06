package utils.Rendering.Core;

import static org.lwjgl.opengl.GL20.*;

public class Shaders {

    public static final String sVertexShader = """
                #version 330 core
                
                layout(location = 0) in vec2 vposition;
                layout(location = 1) in vec4 vcolor;
              
                out vec4 vert_col;
                uniform mat4 u_MVP;
                
                void main()
                {
                   gl_Position = u_MVP * vec4(vposition, 0, 1);
                   vert_col = vcolor;
                };""";

    public static final String sFragmentShader = """
                #version 330 core

                out vec4 out_col;
                in vec4 vert_col;

                void main()
                {
                    out_col = vert_col;
                };""";

    public static final String vertexShaderTex = """
                #version 330 core

                layout(location = 0) in vec2 vposition;
                layout(location = 1) in vec4 vcolor;
                layout(location = 2) in vec2 texture_pos;
                   
                out vec4 vert_col;
                out vec2 v_text_pos;
                uniform mat4 u_MVP;

                void main()
                {
                   gl_Position = u_MVP * vec4(vposition, 0, 1);
                   vert_col = vcolor;
                   v_text_pos = texture_pos;
                };""";

    public static final String fragmentShaderTex = """
                #version 330 core

                out vec4 out_col;
                in vec4 vert_col;
                in vec2 v_text_pos;

                uniform sampler2D u_Texture;

                void main()
                {
                    float c = texture(u_Texture, v_text_pos).r;
                    out_col = vec4(1, 1, 1, c) * vert_col;
                };""";

    private final int iShaderID;

    public Shaders(String vertexShader, String fragmentShader) {
        final var vs = genShader(vertexShader, GL_VERTEX_SHADER);
        final var fs = genShader(fragmentShader, GL_FRAGMENT_SHADER);

        // create shader program and assign shaders we compiled to it
        iShaderID = glCreateProgram();

        glAttachShader(iShaderID, vs);
        glAttachShader(iShaderID, fs);

        glLinkProgram(iShaderID);
        glValidateProgram(iShaderID);

        // nuke it we dont need anymore
        glDeleteShader(vs);
        glDeleteShader(fs);
    }

    public void destroy() {
        glDeleteProgram(iShaderID);
    }

    public void bind() {
        glUseProgram(iShaderID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void bindUniformMat4(String uniformName, boolean transpose, float[] matrix) {
        bind();
        glUniformMatrix4fv(glGetUniformLocation(iShaderID, uniformName), false, matrix);
        unbind();
    }

    public void bindUniform(String uniformName, int v0) {
        bind();
        glUniform1i(glGetUniformLocation(iShaderID, uniformName), v0);
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
