package utils.Renderer.Type;

public class CustomColor {
    public float[] lCol;

    public CustomColor(int r, int g, int b) {
        lCol = new float[] {
                (r / 255.f),
                (g / 255.f),
                (b / 255.f),
                1.f
        };
    }

    public CustomColor(int r, int g, int b, int a) {
        lCol = new float[] {
                (r / 255.f),
                (g / 255.f),
                (b / 255.f),
                (a / 255.f)
        };
    }

    public CustomColor(float r, float g, float b) {
        lCol = new float[] {
                r,
                g,
                b,
                1.f
        };
    }

    public CustomColor(float r, float g, float b, float a) {
        lCol = new float[] {
                r,
                g,
                b,
                a
        };
    }

    public float red() {
        return lCol[0];
    }

    public float green() {
        return lCol[1];
    }

    public float blue() {
        return lCol[2];
    }

    public float alpha() {
        return lCol[3];
    }
}