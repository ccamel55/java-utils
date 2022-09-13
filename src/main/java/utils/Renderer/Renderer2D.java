package utils.Renderer;

import glm.Glm;
import org.lwjgl.BufferUtils;
import utils.Renderer.Core.*;
import utils.Renderer.Type.*;
import utils.Renderer.Utils.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

public class Renderer2D {
    public static int VERTEX_BUFFER_SIZE = 1024 * 4 * 3;

    public static Shader rColorShader;
    public static Shader rTextureShader;

    public static BitmapFont rDefaultFont;
    public static Texture rDefaultFontTexture;

    public static ArcGenerator rArcGenerator;
    public static VertexManager rVertexManager;

    // final as if we fuck with these we will ruin everything!!!!!!!!
    private static final ArrayList<RenderBatch> renderLots = new ArrayList<>();
    private static final FloatBuffer vertexFloatBuffer = BufferUtils.createFloatBuffer(VERTEX_BUFFER_SIZE * VertexGenerator.SIZE_OF_VERTEX);

    public static void init() {

        // shitty old shaders that I wrote ages ago, need to rewrite them ngl
        final String colVertexShader = """
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

        final String colFragmentShader = """
                #version 330 core

                out vec4 out_col;
                in vec4 vert_col;

                void main()
                {
                    out_col = vert_col;
                };""";

        final String texVertexShader = """
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

        final String texFragmentShader = """
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

        final var layouts = new VertexLayoutGenerator();
        layouts.addParam(2); // xy
        layouts.addParam(4); // rgba
        layouts.addParam(2); //tex pos

        rColorShader = new Shader(colVertexShader, colFragmentShader);
        rTextureShader = new Shader(texVertexShader, texFragmentShader);

        rDefaultFont = new BitmapFont("Calibri", 12);
        rDefaultFontTexture = new Texture(rDefaultFont.pTextureImg, true);

        rArcGenerator = new ArcGenerator();
        rVertexManager = new VertexManager(VERTEX_BUFFER_SIZE, VertexGenerator.SIZE_OF_VERTEX, layouts);

        // bind stuff to the shaders
        final var proj = Glm.ortho_(0.f, 1280.f, 720.f, 0.f, 0.f, 1.f);
        rColorShader.bindUniformMat4("u_MVP", false,  proj.toFa_());

        rTextureShader.bindUniformMat4("u_MVP", false,  proj.toFa_());
        rTextureShader.bindUniform("u_Texture", 0);
    }

    public static void destroy() {
        rColorShader.destroy();
        rTextureShader.destroy();
        rDefaultFontTexture.destroy();
        rVertexManager.destroy();
    }

    public static void beginDraw() {
        // set any draw flags here
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // make a lot we can draw into
        renderLots.add(new RenderBatch(VERTEX_BUFFER_SIZE, VertexGenerator.SIZE_OF_VERTEX));
    }

    public static void endDraw() {
        rVertexManager.bind();
        rDefaultFontTexture.bind(GL_TEXTURE0);

        for (final var lot : renderLots) {

            if (lot.iVerticesCount <= 0)
                continue;

            vertexFloatBuffer.clear();
            vertexFloatBuffer.put(lot.lVertices);
            vertexFloatBuffer.flip();

            // write so we can draw from it later
            rVertexManager.writeFloatBuffer(vertexFloatBuffer);

            int offset = 0;
            for (final var batch : lot.aBatch) {

                if (batch.iTextureId == -1) {
                    rColorShader.bind();
                } else {
                    rTextureShader.bind();
                }

                glDrawArrays(batch.iPrimitiveType, offset, batch.iCount);
                offset += batch.iCount;
            }
        }

        rDefaultFontTexture.unbind();
        rVertexManager.unbind();

        renderLots.clear();
    }

    public static void addToRenderList(VertexGenerator vertices, int primitive) {
        addToRenderList(vertices, primitive, -1);
    }

    public static void addToRenderList(VertexGenerator vertices, int primitive, int texId) {
        var currentLot = renderLots.get(renderLots.size() - 1);

        // update if we too many items for vertex list
        if (currentLot.iVerticesCount + vertices.iVertexCount > VERTEX_BUFFER_SIZE) {
            renderLots.add(new RenderBatch(VERTEX_BUFFER_SIZE, VertexGenerator.SIZE_OF_VERTEX));
            currentLot = renderLots.get(renderLots.size() - 1);
        }

        // create new batch coz empty!!!!!
        if (currentLot.aBatch.isEmpty()) {
            currentLot.aBatch.add(new Batch(primitive, texId));
        }

        var currentDrawBatch = currentLot.aBatch.get(currentLot.aBatch.size() - 1);

        // make a new batch coz different primitive
        if (currentDrawBatch.iPrimitiveType != primitive || currentDrawBatch.iTextureId != texId) {
            currentLot.aBatch.add(new Batch(primitive, texId));
            currentDrawBatch = currentLot.aBatch.get(currentLot.aBatch.size() - 1);
        }

        // copy the vertices we have to our main list
        System.arraycopy(vertices.lVertexData, 0, currentLot.lVertices, currentLot.iVertexOffset, vertices.iVertexElementCount);
        currentLot.iVertexOffset += vertices.iVertexElementCount;

        currentDrawBatch.iCount += vertices.iVertexCount;
        currentLot.iVerticesCount += vertices.iVertexCount;
    }

    public static void testRect(float x, float y, float w, float h) {
        final var vertexDataArr = new VertexGenerator(6);
        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255, 255));
        vertexDataArr.addData(x + w, y, new CustomColor(255, 0, 0, 255));
        vertexDataArr.addData(x + w, y + h, new CustomColor(0, 255, 0, 255));

        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255, 255));
        vertexDataArr.addData(x + w, y + h, new CustomColor(0, 255, 0, 255));
        vertexDataArr.addData(x, y + h, new CustomColor(0, 0, 255, 255));

        addToRenderList(vertexDataArr, GL_TRIANGLES);
    }

    public static void testLines(float x, float y, float w, float h) {
        final var vertexDataArr = new VertexGenerator(5);
        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255, 255));
        vertexDataArr.addData(x + w, y, new CustomColor(255, 0, 0, 255));
        vertexDataArr.addData(x + w, y + h, new CustomColor(0, 255, 0, 255));
        vertexDataArr.addData(x, y + h, new CustomColor(0, 0, 255, 255));

        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255, 255));

        addToRenderList(vertexDataArr, GL_LINE_STRIP);
    }

    public static void testString(float x, float y, String text) {

        float curX = x;

        for (int i=0; i < text.length(); i++) {
            final var c = text.charAt(i);
            final var charInfo = rDefaultFont.mCharacterMap.get((int)c);

            final var x1 = curX + charInfo.iWidth;
            final var y1 = y + charInfo.iHeight;

            final var ux0 = charInfo.lTextureCoOrdinates[0].x;
            final var uy0 = charInfo.lTextureCoOrdinates[0].y;

            final var ux1 = charInfo.lTextureCoOrdinates[1].x;
            final var uy1 = charInfo.lTextureCoOrdinates[1].y;

            final var vertexDataArr = new VertexGenerator(6);
            vertexDataArr.addData(curX, y, new CustomColor(255, 255, 255), ux0, uy0);
            vertexDataArr.addData(x1, y1, new CustomColor(255, 255, 255), ux1, uy1);
            vertexDataArr.addData(x1, y, new CustomColor(255, 255, 255), ux1, uy0);

            vertexDataArr.addData(curX, y, new CustomColor(255, 255, 255), ux0, uy0);
            vertexDataArr.addData(x1, y1, new CustomColor(255, 255, 255), ux1, uy1);
            vertexDataArr.addData(curX, y1, new CustomColor(255, 255, 255), ux0, uy1);

            addToRenderList(vertexDataArr, GL_TRIANGLES, rDefaultFontTexture.getId());
            curX += (charInfo.iWidth + 1);
        }
    }

    public static void drawFontBitmap(float x, float y) {

        final var x1 = x + rDefaultFont.pTextureImg.getWidth();
        final var y1 = y + rDefaultFont.pTextureImg.getHeight();

        final var vertexDataArr = new VertexGenerator(6);
        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255), 0.0f, 0.0f);
        vertexDataArr.addData(x1, y1, new CustomColor(255, 255, 255), 1.0f, 1.0f);
        vertexDataArr.addData(x1, y, new CustomColor(255, 255, 255), 1.0f, 0.0f);

        vertexDataArr.addData(x, y, new CustomColor(255, 255, 255), 0.0f, 0.0f);
        vertexDataArr.addData(x1, y1, new CustomColor(255, 255, 255), 1.f, 1.f);
        vertexDataArr.addData(x, y1, new CustomColor(255, 255, 255), 0.f, 1.f);

        addToRenderList(vertexDataArr, GL_TRIANGLES, rDefaultFontTexture.getId());
    }
}
