package utils.Rendering;

import glm.Glm;

import org.lwjgl.BufferUtils;
import utils.Rendering.Core.FontHelper;
import utils.Rendering.Core.Shaders;
import utils.Rendering.Core.TextureManager;
import utils.Rendering.Core.VertexManager;
import utils.Rendering.Type.Batch;
import utils.Rendering.Type.RenderLot;
import utils.Rendering.Utils.VertexGenerator;
import utils.Rendering.Utils.VertexLayoutManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Render2D {

    public static int NUMBER_VERTICES = 1024 * 4 * 3;
    private static Shaders colShader = null;
    private static Shaders texShader = null;

    private static TextureManager textureManager = null;
    private static VertexManager vertManager = null;

    public static FontHelper testFont = null;

    private static final ArrayList<RenderLot> renderLots = new ArrayList<>();
    private static final FloatBuffer vertexFloatBuffer = BufferUtils.createFloatBuffer(NUMBER_VERTICES * VertexGenerator.SIZE_OF_VERTEX);

    static int testText = -1;

    public static void init() {

        final var layouts = new VertexLayoutManager();
        layouts.addParam(2); // xy
        layouts.addParam(4); // rgba
        layouts.addParam(2); //tex pos

        vertManager = new VertexManager(NUMBER_VERTICES, VertexGenerator.SIZE_OF_VERTEX, layouts.parameters);
        textureManager = new TextureManager();

        try {
            testFont = new FontHelper("Tahoma", 20);
            testText = textureManager.addTexture(testFont.img);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        colShader = new Shaders(Shaders.sVertexShader, Shaders.sFragmentShader);
        texShader = new Shaders(Shaders.vertexShaderTex, Shaders.fragmentShaderTex);

        final var proj = Glm.ortho_(0.f, 1280.f, 720.f, 0.f, 0.f, 1.f);
        colShader.bindUniformMat4("u_MVP", false,  proj.toFa_());
        texShader.bindUniformMat4("u_MVP", false,  proj.toFa_());
    }

    public static void destroy() {
        vertManager.destroy();
        textureManager.destroy();
        colShader.destroy();
        texShader.destroy();
    }

    public static void beginDraw() {
        // add a draw batch
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        renderLots.add(new RenderLot());
    }

    public static void endDraw() {

        vertManager.bind();

        for (final var lot : renderLots) {

            if (lot.numVerts <= 0)
                continue;

            vertexFloatBuffer.clear();
            vertexFloatBuffer.put(lot.vertices);
            vertexFloatBuffer.flip();

            // write so we can draw from it later
            vertManager.writeFloatBuffer(vertexFloatBuffer);

            int offset = 0;
            for (final var batch : lot.batches) {

                if (batch.textureID != -1) {
                    texShader.bind();
                    textureManager.bind(batch.textureID);
                }
                else
                    colShader.bind();

                glDrawArrays(batch.primitiveType, offset, batch.count);
                offset += batch.count;
            }
        }

        vertManager.unbind();
        renderLots.clear();
    }

    public static void addToRenderList(VertexGenerator verts, int primitive) {
        addToRenderList(verts, primitive, -1);
    }

    public static void addToRenderList(VertexGenerator verts, int primitive, int texId) {
        var currentLot = renderLots.get(renderLots.size() - 1);

        // update if we too many items for vertex list
        if (currentLot.numVerts + verts.iNumVerts > NUMBER_VERTICES) {
            renderLots.add(new RenderLot());
            currentLot = renderLots.get(renderLots.size() - 1);
        }

        // create new batch coz empty!!!!!
        if (currentLot.batches.isEmpty()) {
            currentLot.batches.add(new Batch(primitive, texId));
        }

        var currentDrawBatch = currentLot.batches.get(currentLot.batches.size() - 1);

        // make a new batch coz different primitive
        if (currentDrawBatch.primitiveType != primitive || currentDrawBatch.textureID != texId) {
            currentLot.batches.add(new Batch(primitive, texId));
            currentDrawBatch = currentLot.batches.get(currentLot.batches.size() - 1);
        }

        // copy the vertices we have to our main list
        System.arraycopy(verts.getData(), 0, currentLot.vertices, currentLot.vertOffset, verts.iNumArrElems);
        currentLot.vertOffset += verts.iNumArrElems;

        currentDrawBatch.count += verts.iNumVerts;
        currentLot.numVerts += verts.iNumVerts;
    }

    public static void testRect(float x, float y, float w, float h) {

        final var vertexDataArr = new VertexGenerator(6);
        vertexDataArr.addData( x, y, 1.f, 1.f, 1.f, 1.f);
        vertexDataArr.addData( x + w, y, 1.f, 0.f, 0.f, 1.f);
        vertexDataArr.addData( x + w, y + h, 0.f, 1.f, 0.f, 1.f);

        vertexDataArr.addData( x, y, 1.f, 1.f, 1.f, 1.f);
        vertexDataArr.addData( x + w, y + h, 0.f, 1.f, 0.f, 1.f);
        vertexDataArr.addData( x, y + h, 0.f, 0.f, 1.f, 1.f);

        addToRenderList(vertexDataArr, GL_TRIANGLES);
    }

    public static void testLines(float x, float y, float w, float h) {

        final var vertexDataArr = new VertexGenerator(5);
        vertexDataArr.addData( x, y, 1.f, 1.f, 1.f, 1.f);
        vertexDataArr.addData( x + w, y, 1.f, 0.f, 0.f, 1.f);
        vertexDataArr.addData( x + w, y + h, 0.f, 1.f, 0.f, 1.f);
        vertexDataArr.addData( x, y + h, 0.f, 0.f, 1.f, 1.f);

        // join
        vertexDataArr.addData( x, y, 1.f, 1.f, 1.f, 1.f);

        addToRenderList(vertexDataArr, GL_LINE_STRIP);
    }

    public static void testString(float x, float y, String text) {

        for (int i=0; i < text.length(); i++) {
            char c = text.charAt(i);
            FontHelper.TexInfo charInfo = testFont.characterMap.get((int)c);

            float xPos = x;
            float yPos = y;

            addCharacter(xPos, yPos, charInfo);
            x += (charInfo.width + 1);
        }
    }

    private static void addCharacter(float x, float y, FontHelper.TexInfo charInfo) {

        float x1 = x + charInfo.width;
        float y1 = y + charInfo.height;

        float ux0 = charInfo.textureCoordinates[0].x;
        float uy0 = charInfo.textureCoordinates[1].y;

        float ux1 = charInfo.textureCoordinates[1].x;
        float uy1 = charInfo.textureCoordinates[0].y;

        final var vertexDataArr = new VertexGenerator(6);

        vertexDataArr.addData(x, y, 1.f, 1.f, 1.f, 1.f, ux0, uy0);
        vertexDataArr.addData(x1, y1, 1.f, 1.f, 1.f, 1.f, ux1, uy1);
        vertexDataArr.addData(x1, y, 1.f, 1.f, 1.f, 1.f, ux1, uy0);

        vertexDataArr.addData(x, y, 1.f, 1.f, 1.f, 1.f, ux0, uy0);
        vertexDataArr.addData(x1, y1, 1.f, 1.f, 1.f, 1.f, ux1, uy1);
        vertexDataArr.addData(x, y1, 1.f, 1.f, 1.f, 1.f, ux0, uy1);

        addToRenderList(vertexDataArr, GL_TRIANGLES, testText);
    }
}
