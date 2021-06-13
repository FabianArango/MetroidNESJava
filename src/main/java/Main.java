package main.java;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import javax.swing.JFrame;
import main.java.script.Game;
import javalgl.controll.Loop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import javalgl.controll.Input;

public class Main {
    public static JFrame jFrame = new JFrame("METROID I");
    public static final Game game = new Game();
    public static final GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public static final GraphicsDevice device = graphics.getDefaultScreenDevice();
    public static boolean fullscreen = false;

    public static void main(String[] args) {
        generateFrame();
        Loop.mainLoop(
            Loop.LOOP_TYPE_FIXED_UPDATE, 
            60,
            new Callable<Void>(){
                public Void call() throws Exception {
                    controllFullScreen();
                    game.runTick();
                    return null;
                };
            }, 
            new Callable<Void>(){
                public Void call() throws Exception {
                    game.renderScreen();
                    return null;
                };
            }, 
            new Callable<Void>(){
                public Void call() throws Exception {
                    game.showScreen();
                    return null;
                };
            }
            );
    }

    private static void controllFullScreen() {
        if (Input.getKeyReleased(KeyEvent.VK_F12)) {
            fullscreen = !fullscreen;
            jFrame.dispose();
            generateFrame();
        }
    }

    private static void generateFrame() {
        jFrame = new JFrame("METROID I");
        if (fullscreen) {
            jFrame.setUndecorated(true);
            device.setFullScreenWindow(jFrame);
            jFrame.setVisible(true);
        } else {
            jFrame.pack();
            jFrame.setSize(650, 600);
            jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        jFrame.add(game);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void unused(String[] args) {
        System.out.println(Arrays.toString(args));

        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "echo Metroid 1").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }
}
