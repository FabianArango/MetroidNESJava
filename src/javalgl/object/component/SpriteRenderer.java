package javalgl.object.component;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javalgl.object.GameObject;
import javalgl.controll.Base;

public class SpriteRenderer {
    private final GameObject gameObject;

    private BufferedImage sprite = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
    
    private float[] offSet = {0, 0};
    private boolean[] flip = {false, false};
    private float[] size = {1, 1};
    
    public static final Object DM_PLANE = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    public static final Object DM_BILINEAR = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    public static final Object DM_BICUBIC = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    private Object drawMode = DM_PLANE;

    public static final Object ANTIALIAS_OFF = RenderingHints.VALUE_ANTIALIAS_OFF;
    public static final Object ANTIALIAS_ON = RenderingHints.VALUE_ANTIALIAS_ON;
    public static final Object ANTIALIAS_DEFAULT = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
    private Object antialiasing = ANTIALIAS_OFF;

    private boolean enabled = true;

    public SpriteRenderer(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public SpriteRenderer(GameObject gameObject, BufferedImage newSprite) {
        this.gameObject = gameObject;
        setSprite(newSprite);
    }

    @Deprecated
    public static BufferedImage load(String path) { 
        try {
            //System.out.println(path);
            //System.out.println(LoadTrick.class.getResource(path));
            return ImageIO.read(new File(path));
            //System.out.println(LoadTrick.class.getResource(path));
            //return ImageIO.read(LoadTrick.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage load(URL path) { 
        try {
            //System.out.println(path);
            //System.out.println(LoadTrick.class.getResource(path));
            //return ImageIO.read(new File(path1));
            //System.out.println(LoadTrick.class.getResource(path));
            return ImageIO.read(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
        setSize(sprite.getWidth(), sprite.getHeight());
    }

    public void setSprite(String path) {
        setSprite(load(path)); 
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setDrawMode(Object drawMode) {
        this.drawMode = drawMode;
    }

    public Object getDrawMode() {
        return drawMode;
    }

    public void setAntialiasing(Object antialiasing) {
        this.antialiasing = antialiasing;
    }

    public Object getAntialiasing() {
        return antialiasing;
    }

    public void setFlip(boolean x, boolean y) {
        this.flip = new boolean[] {x, y};
    }

    public boolean[] getFlip() {
        return flip;
    }

    private float[][] applyFlip() {
        float[][] applyFlip = new float[2][2];

        if (flip[0]) {
            applyFlip[0] = new float[] {getSize()[0], -1};

        } else {
            applyFlip[0] = new float[] {0, 1};
        }

        if (flip[1]) {
            applyFlip[1] = new float[] {getSize()[1], -1};
        } else {
            applyFlip[1] = new float[] {0, 1};
        }

        return applyFlip;
    }
  
    public void setSize(float width, float height) {
        this.size[0] = width;
        this.size[1] = height;
    }

    public float[] getSize() {
        return new float[] {size[0]*gameObject.transform.getScale()[0], size[1]*gameObject.transform.getScale()[1]};
    }

    public static void swapColors(BufferedImage sprite, Color[] oldColors, Color[] newColors) {
        int color;

        for (int x = 0; x < sprite.getWidth(); x++) {
            for (int y = 0; y < sprite.getHeight(); y++) {
                color = sprite.getRGB(x, y);          
                for (int i = 0; i < oldColors.length; i++) {
                    if (color == oldColors[i].getRGB() ) {
                        sprite.setRGB(x, y, newColors[i].getRGB() );
                        break;
                    } else {
                        sprite.setRGB(x, y, color );
                    }
                }
            }
        }
    }

    public void swapColors(Color[] oldColors, Color[] newColors) {
        swapColors(getSprite(), oldColors, newColors);
    }

    public static void swapColors(BufferedImage sprite, String[] oldHexColors, String[] newHexColors) {
        final int BASE = 16;
        Color[] oldColors = new Color[oldHexColors.length]; 
        Color[] newColors = new Color[newHexColors.length];

        for (int i = 0; i < oldColors.length; i++) {
            String[] splitStrOld = oldHexColors[i].replace("/", "").replace(" ", "").split("(?<=\\G.{2})");
            String[] splitStrNew = newHexColors[i].replace("/", "").replace(" ", "").split("(?<=\\G.{2})");

            oldColors[i] = new Color(Integer.parseInt(splitStrOld[0], BASE), 
                                     Integer.parseInt(splitStrOld[1], BASE), 
                                     Integer.parseInt(splitStrOld[2], BASE), 
                                     Integer.parseInt(splitStrOld[3], BASE));

            newColors[i] = new Color(Integer.parseInt(splitStrNew[0], BASE), 
                                     Integer.parseInt(splitStrNew[1], BASE), 
                                     Integer.parseInt(splitStrNew[2], BASE), 
                                     Integer.parseInt(splitStrNew[3], BASE));
        }
        swapColors(sprite, oldColors, newColors);
    }

    public void swapColors(String[] oldHexColors, String[] newHexColors) {
        swapColors(getSprite(), oldHexColors, newHexColors);
    }


    public static BufferedImage copySprite(BufferedImage sprite) {
        BufferedImage newSprite = new BufferedImage(sprite.getWidth(), sprite.getHeight(), sprite.getType());

        for (int x = 0; x < sprite.getWidth(); x++) {
            for (int y = 0; y < sprite.getHeight(); y++) {
                newSprite.setRGB(x, y, sprite.getRGB(x, y));
            }
        }

        return newSprite;
    }

    @Deprecated
    public static BufferedImage resize(BufferedImage sprite, int newWidth, int newHeight) {
        BufferedImage newSprite = new BufferedImage(newWidth, newHeight, sprite.getType());

        Graphics2D graphics2d = newSprite.createGraphics();
        graphics2d.drawImage(sprite, 0, 0, newWidth, newHeight, null);

        return newSprite;
    }

    public void setOffSet(float x, float y) {
        this.offSet = new float[] {x, y};
    }

    public float[] getOffSet() {
        return new float[] {offSet[0]*gameObject.transform.getScale()[0], offSet[1]*gameObject.transform.getScale()[1]};
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void render(Graphics2D graphics2d){
        if (abbleToRender() & enabled) {
            float x = ((gameObject.transform.getScreenPosition()[0]+(getOffSet()[0])+applyFlip()[0][0]))+(float)Base.drawZone[0];
            float y = ((gameObject.transform.getScreenPosition()[1]+(getOffSet()[1])+applyFlip()[1][0]))+(float)Base.drawZone[1];

            float width  = (getSize()[0])*applyFlip()[0][1];
            float height = (getSize()[1])*applyFlip()[1][1];

            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, getDrawMode());
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , getAntialiasing());

            AffineTransform backup = graphics2d.getTransform();

            graphics2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(gameObject.transform.getRotation()), (int)x+(width/2), (int)y+(height/2)));
            graphics2d.drawImage(sprite, Math.round(x), Math.round(y), Math.round(width), Math.round(height), null);
            graphics2d.setTransform(backup);
        }
        // ( (x+w)*escala   ) escalar el pixer
        // (  x+(w*escala)  ) escalar la imagen  // Escala local
        // int( ( (x+(w*escalaProipa))*escalaPixelar ) )  // Escala global
    }

    private boolean abbleToRender() {
        return gameObject.transform.getScreenPosition()[0] <= Base.renderLimit[2] && gameObject.transform.getScreenPosition()[0]+getSize()[0] >= Base.renderLimit[0] &&
               gameObject.transform.getScreenPosition()[1] <= Base.renderLimit[3] && gameObject.transform.getScreenPosition()[1]+getSize()[1] >= Base.renderLimit[1];
            
    }
}