package javalgl.object.component;

import java.awt.Graphics2D;
import java.awt.Color;
import javalgl.object.GameObject;

public class RigidBox2D extends BoxCollider2D {
    private BoxCollider2D leftBoxCollider2D;
    private BoxCollider2D rightBoxCollider2D;
    private BoxCollider2D topBoxCollider2D;
    private BoxCollider2D bottomBoxCollider2D;
    private boolean[] edge = {false, false, false, false}; // left / rigth / top / bottom
    private float edgeOffSet = .1f;

    private BoxCollider2D boxCollider2DSlopeDown = new BoxCollider2D(gameObject, 0, 0, 0, 0);
    private BoxCollider2D boxCollider2DSlopeTop = new BoxCollider2D(gameObject, 0, 0, 0, 0);

    private boolean slopeCollide = false;

    public RigidBox2D(GameObject gameObject) {
        super(gameObject);
    }

    public RigidBox2D(GameObject gameObject, SpriteRenderer spriteRenderer) {
        super(gameObject, spriteRenderer);
        setEdge();
    }

    public RigidBox2D(GameObject gameObject, float x, float y, float width, float height) {
        super(gameObject, x, y, width, height);
        setEdge();
    }

    @Override
    public void setOffSet(float x, float y) {
        super.setOffSet(x, y);
        setEdge();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        setEdge();
    }

    private void setEdge() {
        float[] tempOffSet = {getOffSet()[0]/gameObject.transform.getScale()[0], getOffSet()[1]/gameObject.transform.getScale()[1]};
        float[] tempSize = {getSize()[0]/gameObject.transform.getScale()[0], getSize()[1]/gameObject.transform.getScale()[1]};

        leftBoxCollider2D = new BoxCollider2D(gameObject, tempOffSet[0]-edgeOffSet, tempOffSet[1], 0, tempSize[1]);
        rightBoxCollider2D = new BoxCollider2D(gameObject, tempOffSet[0]+tempSize[0]+edgeOffSet, tempOffSet[1], 0, tempSize[1]);
        topBoxCollider2D = new BoxCollider2D(gameObject, tempOffSet[0], tempOffSet[1]-edgeOffSet, tempSize[0], 0);
        bottomBoxCollider2D = new BoxCollider2D(gameObject, tempOffSet[0], tempOffSet[1]+tempSize[1]+edgeOffSet, tempSize[0], 0);

        boxCollider2DSlopeDown = new BoxCollider2D(gameObject, tempOffSet[0], tempSize[1]+tempOffSet[1]-1, tempSize[0], 2);
        boxCollider2DSlopeTop = new BoxCollider2D(gameObject, tempOffSet[0], tempOffSet[1]-1, tempSize[0], 2);
        boxCollider2DSlopeDown.setRenderColor(new Color(122, 122, 122, 255));
        boxCollider2DSlopeTop.setRenderColor(new Color(122, 122, 122, 255));
    }

    public boolean[] getEdge() {
        return edge;
    }

    public boolean getSlopeCollide() {
        return slopeCollide;
    }

    public void setPreData() {
        edge = new boolean[] {false, false, false, false};
        slopeCollide = false;
    }

    public boolean slopeCollideResponse(BoxCollider2D boxCollider2D, float xMag, float yMag, boolean horizontal, boolean vertical) {
        if (BoxCollider2D.compareArea(boxCollider2DSlopeDown, boxCollider2D) || BoxCollider2D.compareArea(boxCollider2DSlopeTop, boxCollider2D)) {
            float slopeXPos = boxCollider2D.getPoints()[0][0];
            float myXPos = this.getPoints()[0][0]+(this.getSize()[0]/2f);

            float height = boxCollider2D.getSize()[1];
            float width = boxCollider2D.getSize()[0];

            float H = ((myXPos-slopeXPos)+(height-width));

            float y1 = boxCollider2D.getPoints()[horizontal? 0: 2][1];

            if (H > boxCollider2D.getSize()[0]) H = boxCollider2D.getSize()[0];
            if (H < 0) H = 0;

            float o = !vertical? (this.getSize()[1]+this.getOffSet()[1]): this.getOffSet()[1];

            float A = (y1-o)+(H*(horizontal? 1: -1));

            if ((!vertical? (yMag >= 0 && gameObject.transform.position.getY()+Math.abs(Math.round(xMag*2)) >= A): (yMag <= 0 && gameObject.transform.position.getY()-Math.abs(Math.round(xMag*2)) <= A)) && H >= 0 && H <= boxCollider2D.getSize()[0]) {
                if (topBoxCollider2D.compareArea(boxCollider2D)) edge[2] = true;
                if (bottomBoxCollider2D.compareArea(boxCollider2D)) edge[3] = true;
                gameObject.transform.position.setHead(gameObject.transform.position.getX(), A-yMag);
            }
            slopeCollide = true;
            
        }

        return slopeCollide;
    }

    public boolean xTileCollideResponse(BoxCollider2D boxCollider2D, float xMag) {        
        if (BoxCollider2D.compareArea(this, boxCollider2D)) {
            if (xMag > 0) {
                gameObject.transform.position.overWrite(boxCollider2D.getPoints()[0][0]-this.getPoints()[2][0], 0);
            }

            if (xMag < 0) {
                gameObject.transform.position.overWrite(boxCollider2D.getPoints()[2][0]-this.getPoints()[0][0], 0);
            }
        }

        if (leftBoxCollider2D.compareArea(boxCollider2D)) edge[0] = true;
        if (rightBoxCollider2D.compareArea(boxCollider2D)) edge[1] = true;
        return edge[0] | edge[1];       
    }

    public boolean yTileCollideResponse(BoxCollider2D boxCollider2D, float yMag) {
        if (BoxCollider2D.compareArea(this, boxCollider2D)) {
            if (yMag > 0) {
                gameObject.transform.position.overWrite(0, boxCollider2D.getPoints()[0][1]-this.getPoints()[2][1]);
            }

            if (yMag < 0) {
                gameObject.transform.position.overWrite(0, boxCollider2D.getPoints()[2][1]-this.getPoints()[0][1]);
            }
        }

        if (!slopeCollide) {
            if (topBoxCollider2D.compareArea(boxCollider2D)) edge[2] = true;
            if (bottomBoxCollider2D.compareArea(boxCollider2D)) edge[3] = true;
        }
        return edge[2] | edge[3];
    }

    @Override
    public void renderLine(Graphics2D graphics2d) {
        boxCollider2DSlopeDown.renderLine(graphics2d);
        boxCollider2DSlopeTop.renderLine(graphics2d);
        super.renderLine(graphics2d);
        //topBoxCollider2D.renderLine(graphics2d);
        //leftBoxCollider2D.renderLine(graphics2d);
        //rightBoxCollider2D.renderLine(graphics2d);
        //bottomBoxCollider2D.renderLine(graphics2d);
    }

}