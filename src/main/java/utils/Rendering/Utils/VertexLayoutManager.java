package utils.Rendering.Utils;

import java.util.ArrayList;

public class VertexLayoutManager {

    public static class VertexLayout {

        public int count;
        public int offset;

        public VertexLayout(int c, int o) {
            count = c;
            offset = o;
        }
    }

    private int offset;
    public ArrayList<VertexLayout> parameters;

    public VertexLayoutManager() {
        parameters = new ArrayList<>();
        offset = 0;
    }

    public void addParam(int paramFloatCount) {
        parameters.add(new VertexLayout(paramFloatCount, offset));
        offset += (paramFloatCount * 4);
    }
}
