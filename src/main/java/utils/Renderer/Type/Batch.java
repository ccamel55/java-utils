package utils.Renderer.Type;

public class Batch {
    public int iCount;
    public int iPrimitiveType;
    public int iTextureId;

    public Batch(int primitiveType, int textureId) {
        iCount = 0;
        iPrimitiveType = primitiveType;
        iTextureId = textureId;
    }
}
