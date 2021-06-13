package javalgl.object.component;

import java.awt.Graphics2D;
import javalgl.object.GameObject;
import javalgl.controll.Base;
import java.awt.Color;

public class BoxCollider2D {
    public final GameObject gameObject;
    private float[] offSet = {0, 0};
    private float[] size = {1, 1};

    private boolean enableCollide = true;
    private boolean enableRender = true;

    private Color renderColor = new Color(255, 255, 255, 255);

    public BoxCollider2D(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public BoxCollider2D(GameObject gameObject, float x, float y, float width, float height) {
        this.gameObject = gameObject;
        setOffSet(x, y);
        setSize(width, height);
    }

    public BoxCollider2D(GameObject gameObject, SpriteRenderer spriteRenderer) {
        this.gameObject = gameObject;
        setOffSet(spriteRenderer);
        setSize(spriteRenderer);
    }

    public void setOffSet(float x, float y) {
        this.offSet = new float[] {x, y};
    }

    public void setOffSet(SpriteRenderer spriteRenderer) {
        setOffSet(spriteRenderer.getOffSet()[0], spriteRenderer.getOffSet()[1]);
    }

    public float[] getOffSet() {
        return new float[] {offSet[0]*gameObject.transform.getScale()[0], offSet[1]*gameObject.transform.getScale()[1]};
    }

    public void setSize(float width, float height) {
        this.size = new float[] {width, height};
    }

    public void setSize(SpriteRenderer spriteRenderer) {
        setSize(spriteRenderer.getSprite().getWidth(), spriteRenderer.getSprite().getHeight());
    }

    public float[] getSize() {
        return new float[] {size[0]*gameObject.transform.getScale()[0], size[1]*gameObject.transform.getScale()[1]};
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

    public void setRenderColor(Color renderColor) {
        this.renderColor = renderColor;
    }

    public Color getRenderColor() {
        return renderColor;
    }

    public float[][] getPoints() {
        float x1 = gameObject.transform.position.getX()+(getOffSet()[0]);
        float y1 = gameObject.transform.position.getY()+(getOffSet()[1]);

        float w1 = getSize()[0];
        float h1 = getSize()[1];
        return new float[][] {{x1, y1}, {x1+w1, y1}, {x1+w1, y1+h1}, {x1, y1+h1}};
    }

    public float[][] getCameraPoints() {
        float x1 = gameObject.transform.getScreenPosition()[0]+(getOffSet()[0]);
        float y1 = gameObject.transform.getScreenPosition()[1]+(getOffSet()[1]);

        float w1 = getSize()[0];
        float h1 = getSize()[1];
        return new float[][] {{x1, y1}, {x1+w1, y1}, {x1+w1, y1+h1}, {x1, y1+h1}};
    }

    public boolean compareArea(BoxCollider2D boxCollider2) {
        return compareArea(this, boxCollider2);
    }

    public boolean superCompareArea(BoxCollider2D boxCollider2) {
        return superCompareArea(this, boxCollider2);
    }

    public static boolean compareArea(BoxCollider2D boxCollider1, BoxCollider2D boxCollider2) {
        if (boxCollider1.getEnableCollide() && boxCollider2.getEnableCollide()) {
            float[][] pointsBoxC1 = boxCollider1.getPoints();
            float[][] pointsBoxC2 = boxCollider2.getPoints();

            return (Math.min(pointsBoxC1[0][0], pointsBoxC1[1][0]) < Math.max(pointsBoxC2[1][0], pointsBoxC2[0][0])) && (Math.max(pointsBoxC1[0][0], pointsBoxC1[1][0]) > Math.min(pointsBoxC2[1][0], pointsBoxC2[0][0])) && 
                   (Math.min(pointsBoxC1[0][1], pointsBoxC1[3][1]) < Math.max(pointsBoxC2[3][1], pointsBoxC2[0][1])) && (Math.max(pointsBoxC1[0][1], pointsBoxC1[3][1]) > Math.min(pointsBoxC2[3][1], pointsBoxC2[0][1]));
        } else {
            return false;
        }        
    }

    public static boolean superCompareArea(BoxCollider2D boxCollider1, BoxCollider2D boxCollider2) {
        if (boxCollider1.getEnableCollide() && boxCollider2.getEnableCollide()) {
            float[][] pointsBoxC1 = boxCollider1.getPoints();
            float[][] pointsBoxC2 = boxCollider2.getPoints();

            return (Math.min(pointsBoxC1[0][0], pointsBoxC1[1][0]) <= Math.max(pointsBoxC2[1][0], pointsBoxC2[0][0])) && (Math.max(pointsBoxC1[0][0], pointsBoxC1[1][0]) >= Math.min(pointsBoxC2[1][0], pointsBoxC2[0][0])) && 
                   (Math.min(pointsBoxC1[0][1], pointsBoxC1[3][1]) <= Math.max(pointsBoxC2[3][1], pointsBoxC2[0][1])) && (Math.max(pointsBoxC1[0][1], pointsBoxC1[3][1]) >= Math.min(pointsBoxC2[3][1], pointsBoxC2[0][1]));
        } else {
            return false;
        }  
    }

    public void renderLine(Graphics2D graphics2d) {
        if (getEnableRender()) {
            graphics2d.setColor(getRenderColor());
            int[] xPoints = {(int)(getCameraPoints()[0][0])+Base.drawZone[0], 
                            (int)((getCameraPoints()[1][0]-1))+Base.drawZone[0], 
                            (int)((getCameraPoints()[2][0]-1))+Base.drawZone[0],
                            (int)(getCameraPoints()[3][0])+Base.drawZone[0],
                            (int)(getCameraPoints()[0][0])+Base.drawZone[0]};
                            
            int[] yPoints = {(int)(getCameraPoints()[0][1])+Base.drawZone[1], 
                            (int)(getCameraPoints()[1][1])+Base.drawZone[1], 
                            (int)((getCameraPoints()[2][1]-1))+Base.drawZone[1],
                            (int)((getCameraPoints()[3][1]-1))+Base.drawZone[1],
                            (int)(getCameraPoints()[0][1])+Base.drawZone[1]};

            graphics2d.drawPolygon(xPoints, yPoints, xPoints.length);
        }        
    }
}