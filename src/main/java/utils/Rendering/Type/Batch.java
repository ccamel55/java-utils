package utils.Rendering.Type;

public class Batch {

    public int count;
    public int primitiveType;
    public int textureID;

    public Batch(int primType, int texID) {
        count = 0;
        primitiveType = primType;
        textureID = texID;
    }
}
