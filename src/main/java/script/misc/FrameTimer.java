package main.java.script.misc;

public class FrameTimer {
    private int frame = 1;
    private int limit = 1;
    private boolean canReturn = true;
    private boolean oneReturn = false;
    private boolean canRun = true;

    public FrameTimer(int timeLimit) {
        setLimit(timeLimit);
    }

    public FrameTimer(int timeLimit, int newFrame) {
        setLimit(timeLimit);
        setFrame(newFrame);
    }

    public boolean run() {
        if (canRun) frame ++;
        if (frame > limit) {
            frame = limit;
            if (canReturn) {
                if (oneReturn) canReturn = false;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void reset(int timeLimit) {
        setLimit(timeLimit);
        frame = 1;
        canReturn = true;
    }

    public void reset() {
        reset(limit);
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setOneReturn(boolean oneReturn) {
        this.oneReturn = oneReturn;
    }

    public boolean getOneReturn() {
        return oneReturn;
    }

    public boolean timeUp() {
        return (frame >= limit);
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public boolean getCanRun() {
        return canRun;
    }

}
