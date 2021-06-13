package main.java.script.interactive;

import java.awt.Graphics2D;

import javalgl.controll.Input;
import javalgl.object.GameObject;
import javalgl.object.component.BoxCollider2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.controll.StateOneSceneController;
import main.java.script.misc.FrameTimer;
import main.java.script.general.Palette;
import main.java.script.general.SpriteSheet;

public class Item extends GameObject {
    protected final SpriteRenderer spriteRenderer = new SpriteRenderer(this);
    protected final BoxCollider2D boxCollider2D;
    public static byte renderSize = 16;

    protected short xTexture = 0;
    protected short yTexture = 0;
    protected short wTexture = 0;
    protected short hTexture = 0;
    
    public Item() {
        spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(0, 0, renderSize, renderSize));
        boxCollider2D = new BoxCollider2D(this, 6, 6, 4, 4);
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

    public static void put(float x, float y) {
        if (Input.getKeyPressed('2')) {
            StadePlayerChangeItem energy = new StadePlayerChangeItem(x, y, StadePlayerChangeItem.CT_ENERGY, StadePlayerChangeItem.R_NORMAL_ENERGY);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('3')) {
            StadePlayerChangeItem energy = new StadePlayerChangeItem(x, y, StadePlayerChangeItem.CT_MISSILE, StadePlayerChangeItem.R_MISSILE_AMMUNITION);;
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('4')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_MISSILE_PACK);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('5')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_ENERGY_TANK);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('6')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_MARU_MARI);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('7')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_BOMBS);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('8')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_VARIA_SUIT);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('9')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_HIGH_JUMP);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('Z')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_SCREW_ATTACK);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('C')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_LONG_BEAM);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('V')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_ICE_BEAM);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
        if (Input.getKeyPressed('B')) {
            PowerUpPlayerChangeItem energy = new PowerUpPlayerChangeItem(x, y, PowerUpPlayerChangeItem.CT_WAVE_BEAM);
            StateOneSceneController.ITEM_GROUP.add(energy);
        }
    }
}


class StadePlayerChangeItem extends Item {
    public static final byte CT_ENERGY = 0;
    public static final byte CT_MISSILE = 1;

    public static final byte R_NORMAL_ENERGY = 5;
    public static final byte R_SUPER_ENERGY = 20;
    public static final byte R_MISSILE_AMMUNITION = 2;

    private byte changeType;
    private byte resources;

    private String[][][] palette = {{Palette.PALETTE_05, Palette.PALETTE_12}, {Palette.PALETTE_01, Palette.PALETTE_12}};

    private FrameTimer paletteTimer = new FrameTimer(2, 2);
    private byte frame = (byte)palette.length;

    public StadePlayerChangeItem(float x, float y, byte changeType, byte resources) {
        this.changeType = changeType;
        this.resources = resources;

        if (this.changeType == CT_ENERGY) {
            xTexture = 8;
            yTexture = 24;
            wTexture = (short)(renderSize/2);
            hTexture = (short)(renderSize/2);
        }

        if (this.changeType == CT_MISSILE) {
            xTexture = 0;
            yTexture = 8;
            wTexture = renderSize;
            hTexture = (short)(renderSize/2);
            transform.setRotation(-90);
            spriteRenderer.setOffSet(-4, 0);
        }

        boxCollider2D.setOffSet(2, 2);
        transform.position.setPoints(x, y, x, y);
        setAnimation();
    }

    public byte getChangeType() {
        return changeType;
    }

    public byte getResources() {
        return resources;
    }

    private void setAnimation() {
        if (paletteTimer.run()) {
            paletteTimer.reset();
            frame ++;

            if (frame >= palette.length) {
                frame = 0;
            }

            spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(xTexture, yTexture, wTexture, hTexture));
            spriteRenderer.swapColors(Palette.PALETTE_00, palette[Player.armour? 1: 0][frame]);
        }
    }

    @Override
    public void runTick() {
        super.runTick();
        setAnimation();
    }

}


class PowerUpPlayerChangeItem extends Item {
    public static final byte CT_MARU_MARI = 0;
    public static final byte CT_BOMBS = 1;
    public static final byte CT_VARIA_SUIT = 2;
    public static final byte CT_HIGH_JUMP = 3;
    public static final byte CT_SCREW_ATTACK = 4;

    public static final byte CT_ENERGY_TANK = 5;
    public static final byte CT_MISSILE_PACK = 6;

    public static final byte CT_LONG_BEAM = 7;
    public static final byte CT_ICE_BEAM = 8;
    public static final byte CT_WAVE_BEAM = 9;
    
    public static final byte ENERGY_TANK_ADD = 1;
    public static final byte MISSILE_PACK_ADD = 5;

    private byte changeType;

    private final FrameTimer animationTimer = new FrameTimer(2, 2);
    private final String[][] palette = {Palette.PALETTE_19, Palette.PALETTE_05, Palette.PALETTE_12, Palette.PALETTE_13}; 
    private byte frame = (byte)palette.length; 

    private boolean consider = true;

    public PowerUpPlayerChangeItem(float x, float y, byte changeType) {
        this.changeType = changeType;
        switch (this.changeType) {
            case PowerUpPlayerChangeItem.CT_ENERGY_TANK:
                xTexture = 24;
                yTexture = 0;
                break;

            case PowerUpPlayerChangeItem.CT_MISSILE_PACK:
                xTexture = 24;
                yTexture = 16;
                break;

            case PowerUpPlayerChangeItem.CT_MARU_MARI:
                xTexture = 40;
                yTexture = 0;
                break;

            case PowerUpPlayerChangeItem.CT_BOMBS:
                xTexture = 40;
                yTexture = 16;
                break;

            case PowerUpPlayerChangeItem.CT_VARIA_SUIT:
                xTexture = 56;
                yTexture = 0;
                break;
        
            case PowerUpPlayerChangeItem.CT_HIGH_JUMP:
                xTexture = 56;
                yTexture = 16;
                break;

            case PowerUpPlayerChangeItem.CT_SCREW_ATTACK:
                xTexture = 72;
                yTexture = 0;
                break;

            case PowerUpPlayerChangeItem.CT_LONG_BEAM:
                xTexture = 72;
                yTexture = 16;
                break;

            case PowerUpPlayerChangeItem.CT_ICE_BEAM:
                xTexture = 72;
                yTexture = 16;
                break;

            case PowerUpPlayerChangeItem.CT_WAVE_BEAM:
                xTexture = 72;
                yTexture = 16;
                break;

            default:
                break;
        }
        transform.position.setPoints(x, y, x, y);
        runTick();
    }

    public byte getChangeType() {
        return changeType;
    }

    @Override
    public void runTick() {
        super.runTick();
        setAnimation();
    }

    private void setAnimation() {
        if (animationTimer.run()) {
            animationTimer.reset();

            frame ++;
            if (frame >= palette.length) {
                frame = 0;
            }

            spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(xTexture, yTexture, renderSize, renderSize));
            spriteRenderer.swapColors(Palette.PALETTE_00,  palette[frame]);
            switch (this.changeType) {
                case CT_LONG_BEAM:
                    spriteRenderer.getSprite().createGraphics().drawImage(SpriteSheet.ITEM_SHEET_SubImage(0, 16, renderSize/2, renderSize/2), 8, 0, null);
                    break;
    
                case CT_ICE_BEAM:
                    spriteRenderer.getSprite().createGraphics().drawImage(SpriteSheet.ITEM_SHEET_SubImage(8, 16, renderSize/2, renderSize/2), 8, 0, null);
                    break;
    
                case CT_WAVE_BEAM:
                    spriteRenderer.getSprite().createGraphics().drawImage(SpriteSheet.ITEM_SHEET_SubImage(0, 24, renderSize/2, renderSize/2), 8, 0, null);
                    break;
            
                default:
                    break;
            }
        }
    }

    public void setConsider(boolean consider) {
        this.consider = consider;
    }

    public boolean getConsider() {
        return consider;
    }

}