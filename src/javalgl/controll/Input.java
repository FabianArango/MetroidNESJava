package javalgl.controll;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.MouseInfo;

public class Input {
    private static final boolean[] KEY_INPUT_LISTENER = new boolean[256];
    private static final int[] KEY_LOCATION = new int[KEY_INPUT_LISTENER.length];
    private static final boolean[] KEY_INPUT = new boolean[KEY_INPUT_LISTENER.length];
    private static final boolean[] KEY_INPUT_LAST = new boolean[KEY_INPUT_LISTENER.length];

    private static final boolean[] MOUSE_INPUT_LISTENER = new boolean[4];
    private static final boolean[] MOUSE_INPUT = new boolean[MOUSE_INPUT_LISTENER.length];
    private static final boolean[] MOUSE_INPUT_LAST = new boolean[MOUSE_INPUT_LISTENER.length];

    private static final KeyListener KEY_LISTENER = new KeyListener(){
        @Override
        public void keyPressed(KeyEvent event) {
            KEY_INPUT_LISTENER[event.getKeyCode()] = true;
            KEY_LOCATION[event.getKeyCode()] = event.getKeyLocation();
        };

        @Override
        public void keyReleased(KeyEvent event) {
            KEY_INPUT_LISTENER[event.getKeyCode()] = false;
            //KEY_LOCATION[event.getKeyCode()] = event.getKeyLocation();
        };

        @Override
        public void keyTyped(KeyEvent event) {

        };
    };

    private static final MouseListener MOUSE_LISTENER = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent event) {
            MOUSE_INPUT_LISTENER[event.getButton()] = true;
        };

        @Override
        public void mouseReleased(MouseEvent event) {
            MOUSE_INPUT_LISTENER[event.getButton()] = false;
        };

        @Override
        public void mouseClicked(MouseEvent event) {

        };

        @Override
        public void mouseEntered(MouseEvent event) {

        };

        @Override
        public void mouseExited(MouseEvent event) {

        };
    };

    /**
     * Adds the listener to the component
     * @param component
     */
    public static void setListener(Component component) {
        component.addKeyListener(KEY_LISTENER);
        component.addMouseListener(MOUSE_LISTENER);
        component.setFocusable(true);
    }

    public static boolean getKey(int key) {
        return KEY_INPUT[key];
    }

    public static boolean getKeyLocation(int key, int location) {
        return KEY_LOCATION[key] == location;
    }

    public static boolean getKeyPressed(int key) {
        return KEY_INPUT[key] && !KEY_INPUT_LAST[key];
    }

    public static boolean getKeyReleased(int key) {
        return !KEY_INPUT[key] && KEY_INPUT_LAST[key];
    }

    public static boolean getMouseButton(int mouseButton) {
        return MOUSE_INPUT[mouseButton];
    }

    public static boolean getMouseButtonPressed(int key) {
        return MOUSE_INPUT[key] && !MOUSE_INPUT_LAST[key];
    }

    public static boolean getMouseButtonReleased(int key) {
        return !MOUSE_INPUT[key] && MOUSE_INPUT_LAST[key];
    }

    public static double[] getMousePosition() {
        return new double[] {MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY()};
    }

    public static double[] getMousePosition(Component component) {
        return new double[] {getMousePosition()[0]-component.getLocationOnScreen().getX(), getMousePosition()[1]-component.getLocationOnScreen().getY()};
    }

    public static void getInput() {
        for (int i = 0; i < KEY_INPUT_LISTENER.length; i++) {
            KEY_INPUT_LAST[i] = KEY_INPUT[i];
            KEY_INPUT[i] = KEY_INPUT_LISTENER[i];
        }

        for (int i = 0; i < MOUSE_INPUT_LISTENER.length; i++) {
            MOUSE_INPUT_LAST[i] = MOUSE_INPUT[i];
            MOUSE_INPUT[i] = MOUSE_INPUT_LISTENER[i];
        }
    }
}
