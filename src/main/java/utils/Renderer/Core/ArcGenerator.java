package utils.Renderer.Core;

import utils.Renderer.Type.ArcInfo;

public class ArcGenerator {
    public static int NUMBER_OF_POINTS = 360 / 4; // 90 points
    public static double TWO_PI = Math.PI * 2;

    public ArcInfo[] lCirclePoint;

    public ArcGenerator() {
        lCirclePoint = new ArcInfo[NUMBER_OF_POINTS];

        for (int i = 0; i < NUMBER_OF_POINTS; i++) {
            // find how many radians to step on each point
            final var radianStep = (i / (double)NUMBER_OF_POINTS) * TWO_PI;
            lCirclePoint[i] = new ArcInfo(Math.sin(radianStep), Math.cos(radianStep));
        }
    }
}
