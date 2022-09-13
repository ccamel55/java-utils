package utils.Renderer.Type;

import java.util.ArrayList;

public class RenderBatch {
    public int iVerticesCount;
    public int iVertexOffset;
    public float[] lVertices;
    public ArrayList<Batch> aBatch;

    public RenderBatch(int vertexCount, int vertexSize) {
        iVerticesCount = 0;
        iVertexOffset = 0;
        lVertices = new float[vertexCount * vertexSize];
        aBatch = new ArrayList<>();
    }
}
