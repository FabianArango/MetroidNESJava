package javalgl.object;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import javalgl.controll.Base;

public class Font extends GameObject {
    private String text = "";
    private String fontName = "Pixel NES";
    private float size = 16;
    private Color color = new Color(255, 255, 255, 255);
    private boolean enabled = true;

    public static final byte PLAIN = java.awt.Font.PLAIN;
    public static final byte BOLD = java.awt.Font.BOLD;
    public static final byte ITALIC = java.awt.Font.ITALIC;
    private byte style = PLAIN;

    public static final Object ANTIALIAS_OFF = RenderingHints.VALUE_ANTIALIAS_OFF;
    public static final Object ANTIALIAS_ON = RenderingHints.VALUE_ANTIALIAS_ON;
    public static final Object ANTIALIAS_DEFAULT = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
    private Object antialiasing = ANTIALIAS_OFF;

    public Font() {

    }

    public Font(String newText, float newX, float newY) {
        setText(newText);
        setPosition(newX, newY);
    }

    public Font(String newText, float newX, float newY, float newSize, Color newColor) {
        setText(newText);
        setPosition(newX, newY);
        setSize(newSize);
        setColor(newColor);
    }

    public Font(String newText, float newX, float newY, float newSize, String newHexColor) {
        setText(newText);
        setPosition(newX, newY);
        setSize(newSize);
        setColor(newHexColor);
    }

    public Font(String newText, float newX, float newY, String newFontName, byte newStyle, float newSize, Color newColor) {
        setText(newText);
        setPosition(newX, newY);
        setFontName(newFontName);
        setStyle(newStyle);
        setSize(newSize);
        setColor(newColor);
    }

    public Font(String newText, float newX, float newY, String newFontName, byte newStyle, float newSize, String newHexColor) {
        setText(newText);
        setPosition(newX, newY);
        setFontName(newFontName);
        setStyle(newStyle);
        setSize(newSize);
        setColor(newHexColor);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setStyle(byte style) {
        this.style = style;
    }

    public byte getStyle() {
        return style;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(String hexColor) {
        byte BASE = 16;
        String[] newColor = hexColor.replace("/", "").replace(" ", "").split("(?<=\\G.{2})");
        this.color =  new Color(Integer.parseInt(newColor[0], BASE), 
                                Integer.parseInt(newColor[1], BASE), 
                                Integer.parseInt(newColor[2], BASE), 
                                Integer.parseInt(newColor[3], BASE));
    }

    public Color getColor() {
        return color;
    }

    public void setAntialiasing(Object antialiasing) {
        this.antialiasing = antialiasing;
    }

    public Object getAntialiasing() {
        return antialiasing;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setPosition(float x, float y) {
        transform.position.setPoints(x, y, x, y);
    }

    public float[] getPosition() {
        return transform.position.getHead();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setScreenPosition(float x, float y) {
        transform.setScreenPosition(x, y);
    }

    public float[] getScreenPosition() {
        return transform.getScreenPosition();
    }

    @Override
    public void render(Graphics2D graphics2d) {
        if (getEnabled()) {
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, getAntialiasing());
            graphics2d.setFont(new java.awt.Font(getFontName(), getStyle(), Math.round(getSize())));
            graphics2d.setColor(getColor());
            int newLine = 1;
            for (String line: getText().split("\n")) {
                graphics2d.drawString(line, transform.getScreenPosition()[0]+Base.drawZone[0], ((transform.getScreenPosition()[1])+( getSize()*newLine)));
                newLine ++;
            }  
        }
    }
}
