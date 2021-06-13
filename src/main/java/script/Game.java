package main.java.script;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Canvas;
import javalgl.controll.Base;
import javalgl.controll.Input;
import main.java.script.controll.StateOneSceneController;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;

public class Game extends Canvas {
    public static final long serialVersionUID = 0;

    private BufferStrategy bufferStrategy = this.getBufferStrategy();
    public static final BufferedImage SCREEN = new BufferedImage(256, 240, BufferedImage.TYPE_INT_RGB); // 256 224
    private Color backGroundColor = new Color(0, 0, 0, 255);

    public Game() {
        Input.setListener(this);
        Base.setRenderLimit(0, 0, SCREEN.getWidth(), SCREEN.getHeight());
        setBackground(new Color(0, 0, 20, 255));
        StateOneSceneController.init();
    }

    public float getPixelsPerUnit() {
        return Math.min(((float)getHeight()/(float)SCREEN.getHeight()), ((float)getWidth()/(float)SCREEN.getWidth()));
    }

    public int[] getScreenDataSize() {
        float pixelsPerUnit = getPixelsPerUnit();
        int height = Math.round((float)SCREEN.getHeight()*pixelsPerUnit);
        int width = Math.round((float)SCREEN.getWidth()*pixelsPerUnit);
        int x = Math.max((getWidth()-width)/2, 0);
        int y = Math.max((getHeight()-height)/2, 0);

        return new int[] {x, y, width, height};
    }

    public void runTick() {
        StateOneSceneController.stateOneRunTick(this);
    }

    private void render(Graphics2D graphics2d) {
        StateOneSceneController.stateOneRender(graphics2d);
    }

    public void renderScreen() {
        bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null){
            createBufferStrategy(3);       
            return;
        }
        Graphics graphics = bufferStrategy.getDrawGraphics(); 

        //super.paint(graphics);
        //Graphics2D graphics2d = (Graphics2D)graphics;
        
        Graphics2D graphics2d = (Graphics2D)SCREEN.getGraphics();
        graphics2d.setColor(backGroundColor);
        graphics2d.fillRect(0, 0, SCREEN.getWidth(), SCREEN.getHeight());

        render(graphics2d);

        int[] screenDataSize = getScreenDataSize();
        graphics.drawImage(SCREEN, screenDataSize[0], screenDataSize[1], screenDataSize[2], screenDataSize[3], null);

        graphics.dispose();
    }

    public void showScreen() { 
        if (hasFocus()) {
            try {
                bufferStrategy.show();  
            } catch (Exception e) {
                
            }
        }
    }
}