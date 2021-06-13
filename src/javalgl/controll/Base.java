package javalgl.controll;

public class Base {
    public static final int[] drawZone = {0, 0};
    public static final float[] cameraPosition = {0, 0};
    public static final int[] renderLimit = {0, 0, 0, 0};

    public static void setDrawZone(int x, int y) {
        Base.drawZone[0] = x;
        Base.drawZone[1] = y;
    }

    public static void setRenderLimit(int x, int y, int w, int h) {
        Base.renderLimit[0] = x;
        Base.renderLimit[1] = y;
        Base.renderLimit[2] = w;
        Base.renderLimit[3] = h;
    }

    public static void setCameraPosition(float x, float y) {
        Base.cameraPosition[0] = x;
        Base.cameraPosition[1] = y;
    }

    public static void overWriteCameraPosition(float x, float y) {
        Base.cameraPosition[0] += x;
        Base.cameraPosition[1] += y;
    }
}
