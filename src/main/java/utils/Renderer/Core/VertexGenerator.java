package utils.Renderer.Core;

import utils.Renderer.Type.CustomColor;

public class VertexGenerator {
    public static int SIZE_OF_VERTEX = 8;

    public int iVertexCount;
    public int iCurrentVertex;
    public int iVertexElementCount;
    public float[] lVertexData;

    public VertexGenerator(int vertexCount) {
        iVertexCount = vertexCount;
        iCurrentVertex = 0;
        iVertexElementCount = vertexCount * SIZE_OF_VERTEX;
        lVertexData = new float[iVertexElementCount];
    }

    public void addData(float x, float y, CustomColor color, float tx, float ty) {
        final var vertex = iCurrentVertex * SIZE_OF_VERTEX;
        iCurrentVertex++;

        lVertexData[vertex] = x;
        lVertexData[vertex + 1] = y;

        lVertexData[vertex + 2] = color.lCol[0];
        lVertexData[vertex + 3] = color.lCol[1];
        lVertexData[vertex + 4] = color.lCol[2];
        lVertexData[vertex + 5] = color.lCol[3];

        lVertexData[vertex + 6] = tx;
        lVertexData[vertex + 7] = ty;
    }

    public void addData(float x, float y, CustomColor color) {
        final var vertex = iCurrentVertex * SIZE_OF_VERTEX;
        iCurrentVertex++;

        lVertexData[vertex] = x;
        lVertexData[vertex + 1] = y;

        lVertexData[vertex + 2] = color.lCol[0];
        lVertexData[vertex + 3] = color.lCol[1];
        lVertexData[vertex + 4] = color.lCol[2];
        lVertexData[vertex + 5] = color.lCol[3];

        lVertexData[vertex + 6] = 0.f;
        lVertexData[vertex + 7] = 0.f;
    }
}