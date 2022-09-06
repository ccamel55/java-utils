package utils.Rendering.Type;

import utils.Rendering.Render2D;
import utils.Rendering.Utils.VertexGenerator;

import java.util.ArrayList;

public class RenderLot {

    public int numVerts;
    public int vertOffset;

    public float[] vertices;
    public ArrayList<Batch> batches;

    public RenderLot() {
        vertices = new float[Render2D.NUMBER_VERTICES * VertexGenerator.SIZE_OF_VERTEX];
        batches = new ArrayList<>();
        numVerts = 0;
        vertOffset = 0;
    }
}
