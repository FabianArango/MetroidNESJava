package javalgl.controll;

import java.util.concurrent.Callable;

public class Loop {
    public static final byte LOOP_TYPE_FIXED_UPDATE = 0;
    public static final byte LOOP_TYPE_VARIABLE_UPDATE = 1; 

    private static long lastUpate = System.nanoTime();
    private static double ns = 0;

    public static float getDeltaSecond() {
        return (((float)System.nanoTime()-(float)lastUpate)/1000000000);
    }

    public static float getFps() {
        return 1f/getDeltaSecond();
    }

    private static void stopMill() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private static void callMethod(Callable<Void> method) {
        try {
            method.call();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } 
    }

    public static void mainLoop(byte loopType, double ticksPerSecond, Callable<Void> runTick, Callable<Void> render, Callable<Void> show) {
        switch (loopType) {
            case LOOP_TYPE_FIXED_UPDATE:
                lastUpate = System.nanoTime();
                ns = 1000000000 / ticksPerSecond;
                double delta = 0;
                while(true) {
                    long now = System.nanoTime();
                    delta += (now - lastUpate) / ns;
                    lastUpate = now;
                    while(delta >= 1) {
                    Input.getInput();
                    callMethod(runTick);
                    callMethod(render);
                    callMethod(show); // Menor uso del CPU
                    delta--;
                    }
                    //callMethod(render); // Mayor uso del CPU
                    stopMill();
                }
        
            case LOOP_TYPE_VARIABLE_UPDATE:
                lastUpate = System.nanoTime();
                ns = 1000000000 / ticksPerSecond;
                while (true) {
                    if (System.nanoTime() - lastUpate >= ns || ticksPerSecond == -1) {
                        Input.getInput();
                        callMethod(runTick);
                        callMethod(render);
                        //callMethod(render); // Menor uso del CPU
                        lastUpate = System.nanoTime();
                    }
                    callMethod(show); // Mayor uso del CPU
                    stopMill();
                }
            
            default:
                throw new ArithmeticException("Unhandled main loop type");
        }
    }
}
