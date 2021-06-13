package main.java.script.interactive;

import java.awt.Graphics2D;

import javalgl.object.GameObject;
import javalgl.object.component.BoxCollider2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.Game;
import main.java.script.controll.StateOneSceneController;
import main.java.script.misc.FrameTimer;
import main.java.script.misc.RecoloredObject;
import main.java.script.general.Palette;
import main.java.script.general.Sound;
import main.java.script.general.SpriteSheet;

public class Ammunition extends GameObject implements RecoloredObject {
    protected final SpriteRenderer spriteRenderer = new SpriteRenderer(this);
    protected final BoxCollider2D boxCollider2D;

    public final byte renderSize = 8;

    protected boolean canTileCollide = true;
    protected boolean canEnemyCollide = true;
    protected boolean collideDeath = true;
    protected boolean screenDeath = true;

    protected float xVel;
    protected float yVel;

    public static final float X_VEL = 4f;
    public static final float Y_VEL = 4f;

    public static final int MAX_BEAM_SCREEN = 3;
    public static final int MAX_MISSILE_SCREEN = 1;
    public static final int MAX_BOMB_SCREEN = 3;

    public static final byte DAMAGE_1 = 1;
    public static final byte DAMAGE_2 = 2;
    public static final byte DAMAGE_3 = 16;

    protected float damage =  DAMAGE_1;

    protected String[] palette = StateOneSceneController.player.getPalette();

    public Ammunition(float x, float y, float xVel, float yVel) {
        transform.position.setPoints(x, y, x, y);
        this.xVel = xVel;
        this.yVel = yVel;

        boolean xFilp = false;
        short rotation = 0;
        
        if (xVel < 0) {
            xFilp = false;
        } else {
            xFilp = true;
        }

        if (yVel < 0) {
            rotation = -90;
        } else if (yVel > 0) {
            rotation =  90;
        }

        spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(0, 0, renderSize, renderSize));
        spriteRenderer.swapColors(Palette.PALETTE_00, palette);
        boxCollider2D = new BoxCollider2D(this, 1, 1, 5, 5);

        spriteRenderer.setFlip(!xFilp, false);
        transform.setRotation(rotation);
    }

    protected void explosion() {
        destroy();
    }

    private void tileCollide() {
        for (GameObject gameObject : StateOneSceneController.TILE_GROUP) {
            Tile tile = (Tile) gameObject;
            if (boxCollider2D.compareArea(tile.boxCollider2D)) {
                doTileCollide(tile);
                if (collideDeath) {
                    explosion();
                    break;
                } 
                
            }
        }
    }

    protected void doTileCollide(Tile tile) {
        if (DisintegrableTile.class.isAssignableFrom(tile.getClass())) {
            boolean bool = false;

            if ((FiveHitDoor.class.isAssignableFrom(tile.getClass()))) {
                bool = ((FiveHitDoor)tile).getMissile();
            }

            if ((bool && !MissileAmmunition.class.isAssignableFrom(this.getClass()))? false: true){
                DisintegrableTile disintegrableTile = (DisintegrableTile) tile;
                disintegrableTile.disintegrate();
            }
        }

    }

    private void enemyCollide() {
        for (GameObject gameObject : StateOneSceneController.ENEMY_GROUP) {
            Enemy enemy = (Enemy) gameObject;
            if (boxCollider2D.compareArea(enemy.rigidBox2D)) {
                doEnemyCollide(enemy);
                if (collideDeath) explosion();
            }
        }
    }

    protected void doEnemyCollide(Enemy enemy) {
        enemy.damageEnemy(damage);
    }

    protected void beforeCollide() {

    }

    protected void afterCollider() {

    }

    @Override
    public void runTick() {
        super.runTick();

        beforeCollide();

        if (canEnemyCollide) enemyCollide();
        if (canTileCollide) tileCollide();

        afterCollider();

        transform.position.overWrite(xVel, yVel);

        //float limit = Game.SCREEN_SIZE[0]/Variables.pixelsPerUnit[0];
        if (screenDeath) { // ?
            float limit = Game.SCREEN.getWidth();

            if (transform.getScreenPosition()[0]+boxCollider2D.getOffSet()[0] < 0 || transform.getScreenPosition()[0]+boxCollider2D.getSize()[0] > limit) {
                explosion();
            }

            if (transform.getScreenPosition()[1]+boxCollider2D.getOffSet()[1] < 0 || transform.getScreenPosition()[1]+boxCollider2D.getSize()[1] > limit) {
                explosion();
            }
        }
    }
    
    @Override
    public void render(Graphics2D graphics2d) {
        super.render(graphics2d);
        spriteRenderer.render(graphics2d);
        if (StateOneSceneController.debugMode) boxCollider2D.renderLine(graphics2d);
    }

    @Override
    public String[] getPalette() {
        return palette;
    }
}


class BeamAmmunition extends Ammunition {
    private float xFinal = 0;
    private float yFinal = 0;

    private float initX = 0;
    private float initY = 0;

    private float xWaveVel = 0;
    private float yWaveVel = 0;
    private float waveLimit = 12;
    public static float waveVel = 6f;
    private byte powerBeam;

    private static boolean interSound = false;

    public BeamAmmunition(float x, float y, float xVel, float yVel) {
        super(x, y, xVel, yVel);

        initX = x;
        initY = y;

        xFinal = 42*(Math.abs(xVel)/xVel);
        yFinal = 45*(Math.abs(yVel)/yVel);

        powerBeam = Player.powerBeam;

        if (powerBeam == Player.NONE_BEAM) {
            if (interSound) {
                Sound.NORMAL_BEAM_1.stop();
                Sound.NORMAL_BEAM_1.play();
            } else {
                Sound.NORMAL_BEAM_0.stop();
                Sound.NORMAL_BEAM_0.play();
            }
        }

        if (powerBeam == Player.WAVE_BEAM) {
            spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(8, 0,renderSize, renderSize));
            spriteRenderer.swapColors(Palette.PALETTE_00, palette);
            boxCollider2D.setOffSet(0, 0);
            boxCollider2D.setSize(8, 8);
            
            collideDeath = false;

            this.damage = Ammunition.DAMAGE_2;

            if (xVel != 0) yWaveVel = waveVel;
            if (yVel != 0) xWaveVel = waveVel;

            waveVel*=-1;
            if (interSound) {
                Sound.WAVE_BEAM_1.stop();
                Sound.WAVE_BEAM_1.play();
            } else {
                Sound.WAVE_BEAM_0.stop();
                Sound.WAVE_BEAM_0.play();
            }
        }

        if (powerBeam == Player.ICE_BEAM) {
            palette = Palette.PALETTE_12;
            spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(0, 0, renderSize, renderSize));
            spriteRenderer.swapColors(Palette.PALETTE_00, palette);
            if (interSound) {
                Sound.ICE_BEAM_1.stop();
                Sound.ICE_BEAM_1.play();
            } else {
                Sound.ICE_BEAM_0.stop();
                Sound.ICE_BEAM_0.play();
            }
        }
        //spriteRenderer.swapColors(Palette.PALETTE_00, palette[4]);
        interSound = !interSound;
    }

    @Override
    protected void beforeCollide() {
        super.beforeCollide();
        if (!Player.longBeam) {
            if (xVel < 0 && boxCollider2D.getPoints()[0][0] < initX+xFinal) explosion();
            if (xVel > 0 && boxCollider2D.getPoints()[0][0] > initX+xFinal) explosion();
            if (yVel < 0 && boxCollider2D.getPoints()[0][1] < initY+yFinal) explosion();
            if (yVel > 0 && boxCollider2D.getPoints()[0][1] > initY+yFinal) explosion();
        }

        if (powerBeam == Player.WAVE_BEAM) {
            if (boxCollider2D.getPoints()[0][1] < initY-waveLimit) yWaveVel*=-1;           
            if (boxCollider2D.getPoints()[0][1] > initY+waveLimit-2) yWaveVel*=-1;
            
            if (boxCollider2D.getPoints()[0][0] < initX-waveLimit) xWaveVel*=-1;           
            if (boxCollider2D.getPoints()[0][0] > initX+waveLimit-2) xWaveVel*=-1;   

            transform.position.overWrite(xWaveVel, yWaveVel);
        }
    }

    @Override
    protected void doEnemyCollide(Enemy enemy) {
        if (powerBeam == Player.ICE_BEAM) {
            enemy.freeze();
        }
        super.doEnemyCollide(enemy);
    }

    @Override
    public void destroy() {
        super.destroy();    
    }
}


class MissileAmmunition extends Ammunition {
    private static boolean interSound = false;

    public MissileAmmunition(float x, float y, float xVel, float yVel) {
        super(x, y-2, xVel, yVel);
        spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(0, 8, renderSize*2,renderSize));
        spriteRenderer.setOffSet(-4, 0);
        this.damage = Ammunition.DAMAGE_3; // Por confirmar 
        if (interSound) {
            Sound.MISSILE_1.stop();
            Sound.MISSILE_1.play();
        } else {
            Sound.MISSILE_0.stop();
            Sound.MISSILE_0.play();
        }
        interSound = !interSound;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}


class BombAmmunition extends Ammunition {
    // timer --------------------
    private FrameTimer timeToExp = new FrameTimer(50);
    private FrameTimer expTime = new FrameTimer(25);
    private FrameTimer animatioChronometer = new FrameTimer(3, 3);
    private FrameTimer resizeTime = new FrameTimer(7);

    // render -------------------
    private short[][] spriteStates = {{16, 8}, {16, 16}};
    private byte frame = (byte)spriteStates.length;

    // player -------------------
    protected boolean playerCollide = false;

    // hitBox -------------------
    protected short[][] hitBoxStates = {{-1, -1, 10, 10}, {-4, -4, 16, 16}, {-12, -12, 32, 32}};
    private byte index = 0;

    private static boolean interSoundPut = false;
    private static boolean interSoundExplosion = false;

    public BombAmmunition(float x, float y, float xVel, float yVel) {
        super(x, y, xVel, yVel); 
        canEnemyCollide = false;
        collideDeath = false;
        screenDeath = false;

        xVel = 0;
        yVel = 0;
        //boxCollider2D = new BoxCollider2D(this, 2, 2, 6, 6);
        boxCollider2D.setOffSet(2, 2);
        boxCollider2D.setSize(4, 4);
        setAnimation();

        if (interSoundPut) {
            Sound.PUT_A_BOMB_1.stop();
            Sound.PUT_A_BOMB_1.play();
        } else {
            Sound.PUT_A_BOMB_0.stop();
            Sound.PUT_A_BOMB_0.play();
        }

        interSoundPut = !interSoundPut;
    }

    @Override
    protected void beforeCollide() {
        super.beforeCollide();
        if (timeToExp.run()) {
            canEnemyCollide = true;
            spriteRenderer.setEnabled(false);

            if (resizeTime.run()) {
                resizeTime.reset();
                boxCollider2D.setOffSet(hitBoxStates[index][0], hitBoxStates[index][1]);
                boxCollider2D.setSize(hitBoxStates[index][2], hitBoxStates[index][3]);
                if (index == 0) {
                    if (interSoundExplosion) {
                        Sound.BOMB_1.stop();
                        Sound.BOMB_1.play();
                    } else {
                        Sound.BOMB_0.stop();
                        Sound.BOMB_0.play();
                    }
                    interSoundExplosion = !interSoundExplosion;
                }
                index ++;
                if (index >= hitBoxStates.length) {
                    index = (byte)(hitBoxStates.length);
                }
            }

            if (expTime.run()) {
                destroy();
            }
        } else {
            canEnemyCollide = false;
            setAnimation();
        }
    }

    private void setAnimation() {
        if (animatioChronometer.run()) {
            animatioChronometer.reset();
            frame ++;
            if (frame >= spriteStates.length) {
                frame = 0;
            }
            spriteRenderer.setSprite(SpriteSheet.ITEM_SHEET_SubImage(spriteStates[frame][0], spriteStates[frame][1], renderSize, renderSize));
            spriteRenderer.swapColors(Palette.PALETTE_00, palette);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}