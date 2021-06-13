package javalgl.object;

public class Vector2 {
    private final float[] head = {0, 0};
    private final float[] tail = {0, 0};

    public Vector2(float x1, float y1) {
        setHead(x1, y1);
        setTail(x1, y1);
    }

    public Vector2(float x1, float y1, float x2, float y2) {
        setHead(x1, y1);
        setTail(x2, y2);
    }

    public void setHead(float x1, float y1) {
        setTail(getX(), getY());
        this.head[0] = x1;
        this.head[1] = y1;
    }

    public float[] getHead() {
        return head;
    }

    public void setTail(float x2, float y2 ) {
        this.tail[0] = x2;
        this.tail[1] = y2;
    }

    public float[] getTail() {
        return tail;
    }

    public void setX(float x1) {
        setHead(x1, getY());
    }
    
    public void setY(float y1) {
        setHead(getX(), y1);
    }
    
    public float getX() {
        return getHead()[0];
    }

    public float getY() {
        return getHead()[1];
    }

    public void setX2(float x1) {
        setTail(x1, getY());
    }
    
    public void setY2(float y1) {
        setTail(getX(), y1);
    }
    
    public float getX2() {
        return getTail()[0];
    }

    public float getY2() {
        return getTail()[1];
    }

    public void setPoints(float x1, float y1, float x2, float y2) {
        setHead(x1, y1);
        setTail(x2, y2);
    }

    public float[][] getPoints() {
        return new float[][] {getHead(), getTail()};
    }

    public void overWrite(float x, float y) {
        setHead(getX()+x, getY()+y);
    }

    public void overWrite(float x1, float y1, float x2, float y2) {
        setHead(getX()+x1, getY()+y1);
        setTail(getX2()+x2, getY2()+y2);
    }

}
