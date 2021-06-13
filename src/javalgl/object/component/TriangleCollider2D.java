package javalgl.object.component;

import java.awt.Graphics2D;
import javalgl.object.GameObject;
import javalgl.controll.Base;

public class TriangleCollider2D {
    private final GameObject gameObject;
    private float[] offSet = {0, 0};
    private float[][] bodyShapePoints = {{0, 0}, {1, 0}, {0, 1}};

    private boolean enableCollide = true;
    private boolean enableRender = true;

    public TriangleCollider2D(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public TriangleCollider2D(GameObject gameObject, float x, float y, float[] point1, float [] point2, float[] point3) {
        this.gameObject = gameObject;
        setOffSet(x, y);
        setBodyShapePoints(point1, point2, point3);
    }

    public TriangleCollider2D(GameObject gameObject, SpriteRenderer spriteRenderer, float[] point1, float [] point2, float[] point3) {
        this.gameObject = gameObject;
        setOffset(spriteRenderer);
        setBodyShapePoints(point1, point2, point3);
    }

    public void setOffSet(float x, float y) {
        this.offSet = new float[] {x, y};
    }

    public void setOffset(SpriteRenderer spriteRenderer) {
        setOffSet(spriteRenderer.getOffSet()[0], spriteRenderer.getOffSet()[1]);
    }

    public float[] getOffSet() {
        return new float[] {offSet[0]*gameObject.transform.getScale()[0], offSet[1]*gameObject.transform.getScale()[1]};
    }

    public void setEnableCollide(boolean enableCollide) {
        this.enableCollide = enableCollide;
    }

    public boolean getEnableCollide() {
        return enableCollide;
    }

    public void setEnableRender(boolean enableRender) {
        this.enableRender = enableRender;
    }

    public boolean getEnableRender() {
        return enableRender;
    }

    public void setBodyShapePoints(float[] point1, float [] point2, float[] point3) {
        this.bodyShapePoints = new float[][] {point1, point2, point3};
    }

    public float[][] getBodyShapePoints() {
        float[][] p = new float[bodyShapePoints.length][bodyShapePoints[0].length];

        for (int i = 0; i < bodyShapePoints.length; i++) {
            p[i][0] = bodyShapePoints[i][0]*gameObject.transform.getScale()[0];
            p[i][1] = bodyShapePoints[i][1]*gameObject.transform.getScale()[1];
        }

        return p;
    }

    public float[][] getPoints() {
        float x1 = gameObject.transform.position.getX()+(getOffSet()[0]);
        float y1 = gameObject.transform.position.getY()+(getOffSet()[1]);

        float[][] points = getBodyShapePoints();

        for (int i = 0; i < points.length; i++) {
            points[i][0] += x1;
            points[i][1] += y1;
        }

        return points;
    }

    public float[][] getCameraPoints() {
        float x1 = gameObject.transform.getScreenPosition()[0]+(getOffSet()[0]);
        float y1 = gameObject.transform.getScreenPosition()[1]+(getOffSet()[1]);

        float[][] points = getBodyShapePoints();

        for (int i = 0; i < points.length; i++) {
            points[i][0] += x1;
            points[i][1] += y1;
        }

        return points;
    }

    private static boolean isInside(TriangleCollider2D triangleCollider1, TriangleCollider2D triangleCollider2) {
        float[][] myPoints = triangleCollider1.getPoints();
        float[] a = myPoints[0];
        float[] b = myPoints[1];
        float[] c = myPoints[2];
        float[] p;
        float w1;
        float w2;
        boolean r = false;

        for (int i = 0; i < 3; i++) {
            p = triangleCollider2.getPoints()[i];
            w1 = ( a[0]*(c[1]-a[1])+(p[1]-a[1])*(c[0]-a[0])-p[0]*(c[1]-a[1]) ) / ( (b[1]-a[1])*(c[0]-a[0])-(b[0]-a[0])*(c[1]-a[1]) );
            w2 = ( p[1]-a[1]-w1*(b[1]-a[1]) ) / ( c[1]-a[1] );
            
            if ((w1 >= 0) && (w2 >= 0) && (w1+w2 <= 1)) {
                r = true;
                break;
            }
        }
        return r;
    }

    public boolean compareArea(TriangleCollider2D triangleCollider2D) {
        return compareArea(this, triangleCollider2D);
    }

    public static boolean compareArea(TriangleCollider2D triangleCollider1, TriangleCollider2D triangleCollider2) {
        return (triangleCollider1.getEnableCollide() & triangleCollider2.getEnableCollide()) & (isInside(triangleCollider1, triangleCollider2) || isInside(triangleCollider2, triangleCollider1));
    }

    public void renderLine(Graphics2D graphics2d) {
        if (enableRender) {
            float[][] points = getCameraPoints();
            int[] xPoints = {(int)points[0][0], (int)points[1][0], (int)points[2][0], (int)points[0][0]};
            int[] yPoints = {(int)points[0][1], (int)points[1][1], (int)points[2][1], (int)points[0][1]};
            for (int i = 0; i < xPoints.length; i++) {
                xPoints[i] = (int)(xPoints[i])+Base.drawZone[0];
                yPoints[i] = (int)(yPoints[i])+Base.drawZone[1];
            }
            graphics2d.drawPolygon(xPoints, yPoints, 4);
        }
    }

}
