package main.java.script.interactive;

import java.awt.Graphics2D;

import javalgl.controll.Input;
import javalgl.object.GameObject;
import javalgl.object.component.BoxCollider2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.controll.StateOneSceneController;
import main.java.script.general.SpriteSheet;

public class Slope extends GameObject {
    protected final SpriteRenderer spriteRenderer = new SpriteRenderer(this, SpriteSheet.TILE_SHEET_SubImage(16, 16, 16, 16));
    protected final BoxCollider2D boxCollider2D = new BoxCollider2D(this, 0, 0, 16, 16);

    private final boolean horizontal;
    private final boolean vertical;

    public static final boolean LEFT = false;
    public static final boolean RIGHT = true;

    public static final boolean TOP = true;
    public static final boolean BOTTOM = false;

    public Slope(float x, float y, boolean horizontal, boolean vertical) {
        transform.position.setHead(x, y);
        transform.position.setTail(x, y);
        this.horizontal = horizontal;
        this.vertical = vertical;
        spriteRenderer.setFlip(horizontal == !vertical, vertical);
    }

    public boolean getHorizontal() {
        return horizontal;
    }

    public boolean getVertical() {
        return vertical;
    }

    @Override
    public void runTick() {
        super.runTick();
    }

    @Override
    public void render(Graphics2D graphics2d) {
        super.render(graphics2d);
        spriteRenderer.render(graphics2d);
        if (StateOneSceneController.debugMode) boxCollider2D.renderLine(graphics2d);
    }

    public static void generateTileEXP(float x, float y) {
        if (Input.getKeyReleased('U')) {
            Slope slope = new Slope(x, y, false, false);
            StateOneSceneController.SLOPE_GROUP.add(slope);
        }
        if (Input.getKeyReleased('I')) {
            Slope slope = new Slope(x, y, true, false);
            StateOneSceneController.SLOPE_GROUP.add(slope);
        }
        if (Input.getKeyReleased('O')) {
            Slope slope = new Slope(x, y, false, true);
            StateOneSceneController.SLOPE_GROUP.add(slope);
        }
        if (Input.getKeyReleased('P')) {
            Slope slope = new Slope(x, y, true, true);
            StateOneSceneController.SLOPE_GROUP.add(slope);
        }
    }
}
