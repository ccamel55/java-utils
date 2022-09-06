package utils.Rendering.Utils;

public class VertexGenerator {

    // x, y     r, g, b, a
    public static int SIZE_OF_VERTEX = 8;
    public int iNumArrElems;
    public int iNumVerts;

    private float[] lData ;
    public int iCurrentVert;

    public VertexGenerator(int numVertices) {
        iNumVerts = numVertices;
        iNumArrElems = numVertices * SIZE_OF_VERTEX;

        lData = new float[iNumArrElems];
        iCurrentVert = 0;
    }

    public float[] getData() {
        return lData;
    }

    public void addData(float x, float y, float r, float g, float b, float a) {
        final var step = iCurrentVert * SIZE_OF_VERTEX;

        lData[step] = x;
        lData[step + 1] = y;

        lData[step + 2] = r;
        lData[step + 3] = g;
        lData[step + 4] = b;
        lData[step + 5] = a;

        lData[step + 6] = 0.f;
        lData[step + 7] = 0.f;

        iCurrentVert++;
    }

    public void addData(float x, float y, float r, float g, float b, float a, float tx, float ty) {
        final var step = iCurrentVert * SIZE_OF_VERTEX;

        lData[step] = x;
        lData[step + 1] = y;

        lData[step + 2] = r;
        lData[step + 3] = g;
        lData[step + 4] = b;
        lData[step + 5] = a;

        lData[step + 6] = tx;
        lData[step + 7] = ty;

        iCurrentVert++;
    }
}
