package main.java.script.interactive;

import java.awt.Graphics2D;

import javalgl.controll.Input;
import javalgl.object.GameObject;
import javalgl.object.component.BoxCollider2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.controll.StateOneSceneController;
import main.java.script.misc.FrameTimer;
import main.java.script.general.SpriteSheet;


public class Tile extends GameObject {
    protected final SpriteRenderer spriteRenderer = new SpriteRenderer(this);
    protected final BoxCollider2D boxCollider2D;
    public static final byte renderSize = 16;

    private final boolean xPassableOnSlopeCollide;
    private final boolean yPassableOnSlopeCollide;

    public Tile(float x, float y, int xTexture, int yTexture, boolean xPassableOnSlopeCollide, boolean yPassableOnSlopeCollide) {
        transform.position.setPoints(x, y, x, y);
        spriteRenderer.setSprite(SpriteSheet.TILE_SHEET_SubImage(xTexture, yTexture, renderSize, renderSize));
        boxCollider2D = new BoxCollider2D(this, spriteRenderer);
        this.xPassableOnSlopeCollide = xPassableOnSlopeCollide;
        this.yPassableOnSlopeCollide = yPassableOnSlopeCollide;
    }

    public boolean getXPassableOnSlopeCollide() {
        return xPassableOnSlopeCollide;
    }

    public boolean getYPassableOnSlopeCollide() {
        return yPassableOnSlopeCollide;
    }

    public void antiTraspassing() {
        
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
        if (Input.getKeyReleased('1')) {
            Tile tile = new Tile(x, y, 0, 0, true, true);
            StateOneSceneController.TILE_GROUP.add(tile);
        }

        if (Input.getKeyReleased('M')) {
            Tile tile = new Tile(x, y, 0, 0, false, false);
            StateOneSceneController.TILE_GROUP.add(tile);
        }

        if (Input.getKeyReleased('2')) {
            PassableTile tile = new PassableTile(x, y, 16, 0);
            StateOneSceneController.TILE_GROUP.add(tile);
        }

        if (Input.getKeyReleased('3')) {
            DisintegrableTile tile = new DisintegrableTile(x, y, 32, 0, true, true);
            StateOneSceneController.TILE_GROUP.add(tile);
        }
        
        if (Input.getKeyReleased('4')) {
            ItemDropTile tile = new ItemDropTile(x, y, 0, 16, PowerUpPlayerChangeItem.CT_LONG_BEAM);
            StateOneSceneController.TILE_GROUP.add(tile);
        }  

        if (Input.getKeyReleased('5')) {
            OneHitDoor tile = new OneHitDoor(x, y, false, 0);
            StateOneSceneController.TILE_GROUP.add(tile);
        }
        if (Input.getKeyReleased('6')) {
            FiveHitDoor tile = new FiveHitDoor(x, y, true);
            StateOneSceneController.TILE_GROUP.add(tile);
        }
    }
}


class DisintegrableTile extends Tile {
    protected final FrameTimer timer = new FrameTimer(325, 325); // la animación de destruccion va a dos frames
    protected final short[][] spriteStates = {{0, 0, renderSize, renderSize}, {48, 0, renderSize, renderSize},  {48, 0, renderSize, renderSize}, {64, 0, renderSize, renderSize}, {64, 0, renderSize, renderSize}};
    
    public DisintegrableTile(float x, float y, int xTexture, int yTexture, boolean xPassableOnSlopeCollide, boolean yPassableOnSlopeCollide) {
        super(x, y, xTexture, yTexture, xPassableOnSlopeCollide, yPassableOnSlopeCollide);
        timer.setOneReturn(true);
        spriteStates[0] = new short[] {(short)xTexture, (short)yTexture, renderSize, renderSize};
    }

    @Override
    public void runTick() {
        super.runTick();
        setAnimation();
        if (timer.run()) {
            doWhenEnabled();
        }

        if (!timer.getCanRun()) {
            timer.setCanRun(true);
        }
    }

    private void setAnimation() {
        if (!timer.timeUp()) {
            int frame = timer.getFrame();
            byte index = 0;
            if (frame == 1 || frame == timer.getLimit()-1) {
                index = 1;
            }

            if (frame == 2 || frame == timer.getLimit()-2) {
                index = 2;
            }

            if (frame == 3 || frame == timer.getLimit()-3) {
                index = 3;
            }

            if (frame == 4 || frame == timer.getLimit()-4) {
                index = 4;
            }

            if (index != 0) {
                spriteRenderer.setEnabled(true);
                spriteRenderer.setSprite(SpriteSheet.TILE_SHEET_SubImage(spriteStates[index][0], spriteStates[index][1], spriteStates[index][2],  spriteStates[index][3]));
            } else {
                spriteRenderer.setEnabled(false);
            }
        }
    }

    public void disintegrate() {
        boxCollider2D.setEnableCollide(false);
        timer.reset();
    }

    @Override
    public void antiTraspassing() {
        super.antiTraspassing();
        timer.setCanRun(false);
    }

    protected void doWhenEnabled() {
        boxCollider2D.setEnableCollide(true);
        spriteRenderer.setEnabled(true);
        spriteRenderer.setSprite(SpriteSheet.TILE_SHEET_SubImage(spriteStates[0][0], spriteStates[0][1], spriteStates[0][2], spriteStates[0][3]));
    }

}


class ItemDropTile extends DisintegrableTile {
    private PowerUpPlayerChangeItem item;

    public ItemDropTile(float x, float y, int xTexture, int yTexture, byte changeType) {
        super(x, y, xTexture, yTexture, false, false);
        item = new PowerUpPlayerChangeItem(x, y, changeType);
    }

    @Override
    public void disintegrate() {
        if (item.getConsider()) {
            StateOneSceneController.ITEM_GROUP.add(item);
        }
        super.disintegrate();
    }

    @Override
    protected void doWhenEnabled() {
        super.doWhenEnabled();
        item.destroy();
    }
}


class PassableTile extends Tile {
    public PassableTile(float x, float y, int xTexture, int yTexture) {
        super(x, y, xTexture, yTexture, true, true);
        spriteRenderer.setSprite(SpriteSheet.TILE_SHEET_SubImage(xTexture, yTexture, renderSize, renderSize));
        boxCollider2D.setEnableCollide(false);
    }
}


class OneHitDoor extends DisintegrableTile {
    public OneHitDoor(float x, float y, boolean xFilp, int minusFrame) {
        super(x, y, 80, 0, false, false);
        for (short[] state : spriteStates) {
            state[2] = renderSize/2;
            state[3] = renderSize*3;
        }

        spriteStates[1][0] = 80;
        spriteStates[1][1] = 0;

        spriteStates[2][0] = 80;
        spriteStates[2][1] = 0;

        spriteStates[3][0] = 88;
        spriteStates[3][1] = 0;

        spriteStates[4][0] = 88;
        spriteStates[4][1] = 0;

        //timer.setLimit(235);
        timer.setFrame(timer.getLimit()-minusFrame);
        spriteRenderer.setFlip(xFilp, false);
        spriteRenderer.setOffSet(xFilp? 8: 0, 0);
        boxCollider2D.setOffSet(xFilp? 8: 0, 0);

        boxCollider2D.setSize(8, 48);
        runTick();
    }
}


class FiveHitDoor extends OneHitDoor {
    private byte count = 0;
    private boolean missile = true;
    public FiveHitDoor(float x, float y, boolean xFilp) {
        super(x, y, xFilp, 0);
    }

    public boolean getMissile() {
        return missile;
    }

    @Override
    public void disintegrate() {
        count ++;
        if (count >= 5) {
            count = 5;
            missile = false;
            super.disintegrate();
        }
    }
}


 