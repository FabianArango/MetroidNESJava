package main.java.script.interactive;

import java.awt.Graphics2D;

import javalgl.object.GameObject;
import javalgl.object.component.RigidBox2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.controll.StateOneSceneController;
import main.java.script.misc.FrameTimer;
import main.java.script.general.SpriteSheet;

public class Enemy extends GameObject {
    private SpriteRenderer spriteRenderer = new SpriteRenderer(this, SpriteSheet.ENEMY_SHEET_SubImage(0, 0, 16, 16));
    public RigidBox2D rigidBox2D = new RigidBox2D(this, spriteRenderer);
    private static final byte damageRecoveryTime = 10;
    private FrameTimer damageRecoveryChronometer = new FrameTimer(damageRecoveryTime, damageRecoveryTime);
    private short live = 10;
    private boolean damaged = false;
    private boolean freeze = false;
    private short freezeHits = 0;
    private short freezeHitsLimit = 1;
    private boolean canDeFreezeByHit = true;

    public Enemy(float x, float y) {
        transform.position.setPoints(x, y, x, y);
    }

    public void damageEnemy(float damage) {
        if (canDeFreezeByHit && freezeHits >= freezeHitsLimit) {
            freeze = false;
            freezeHits = 0;
        }

        if (!freeze) {
            if (!damaged) {
                live -= damage; 
                damageRecoveryChronometer.reset();
            }
            if (live <= 0) {
                explosion();
            }
        } else {
            freezeHits ++;
        }
    }

    public float getDamage() {
        return 10;
    }

    public void freeze() {
        freeze = true;
    }

    public boolean getFreeze() {
        return freeze;
    }

    private void explosion() {
        destroy();
    }

    

    @Override
    public void runTick() {
        super.runTick();
        if (damageRecoveryChronometer.run()) {
            damaged = false;
            if (freeze) {
                spriteRenderer.setSprite(SpriteSheet.ENEMY_SHEET_SubImage(0, 0, 16, 16));
                spriteRenderer.swapColors(new String[] {"9c4a00ff"}, new String[] {"103239ff"});
            } else {
                spriteRenderer.setSprite(SpriteSheet.ENEMY_SHEET_SubImage(0, 0, 16, 16));
            }
        } else {
            damaged = true;
            spriteRenderer.setSprite(SpriteSheet.ENEMY_SHEET_SubImage(0, 0, 16, 16));
            spriteRenderer.swapColors(new String[] {"9c4a00ff"}, new String[] {"ff0000ff"});
        }
    }

    @Override
    public void render(Graphics2D graphics2d) {
        super.render(graphics2d);
        spriteRenderer.render(graphics2d);
        if (StateOneSceneController.debugMode) rigidBox2D.renderLine(graphics2d);
    }
}
