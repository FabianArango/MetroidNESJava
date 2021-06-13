package main.java.script.misc;

@Deprecated
public class Chronometer {
    private int rate = 0;
    private long timer = System.currentTimeMillis();
    private boolean end = false;

    public Chronometer(int rate) {
        this.rate = rate;
    }

    public Chronometer(int rate, long timer) {
        this.rate = rate;
        this.timer = timer;
    }

    public boolean timeOver() {
        if (!end) {
            if (System.currentTimeMillis()-timer >= rate) {
                end = true;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getTimer() {
        return timer;
    }

    public void reset(int newRate) {
        setRate(newRate);
        setTimer(System.currentTimeMillis());
        end = false;
    }

    public void reset() {
        reset(getRate());
    }

}