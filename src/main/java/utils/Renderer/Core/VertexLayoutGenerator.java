package utils.Renderer.Core;

import utils.Renderer.Type.VertexLayout;

import java.util.ArrayList;

public class VertexLayoutGenerator {
    public int iCurrentOffset;
    public ArrayList<VertexLayout> aLayout;

    public VertexLayoutGenerator() {
        iCurrentOffset = 0;
        aLayout = new ArrayList<>();
    }

    public void addParam(int paramFloatCount) {
        aLayout.add(new VertexLayout(paramFloatCount, iCurrentOffset));
        iCurrentOffset += (paramFloatCount * 4);
    }
}

