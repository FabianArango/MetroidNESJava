package main.java.script.interactive;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javalgl.controll.Input;
import javalgl.object.Font;
import javalgl.object.GameObject;
import javalgl.object.component.BoxCollider2D;
import javalgl.object.component.RigidBox2D;
import javalgl.object.component.SpriteRenderer;
import main.java.script.Game;
import main.java.script.controll.StateOneSceneController;
import main.java.script.misc.FrameTimer;
import main.java.script.misc.RecoloredObject;
import main.java.script.general.Palette;
import main.java.script.general.Sound;
import main.java.script.general.SpriteSheet;

public class Player extends GameObject implements RecoloredObject {
    // render ---------------
    private final SpriteRenderer spriteRenderer = new SpriteRenderer(this);

    private final short[][] iddleStates = {{1, 42}, {1, 42}, {1, 42}};
    private final short[][] walkStates = {iddleStates[0], iddleStates[0], {34, 42}, {67, 42}, {100, 42}};
    private final short[][] jumpStates = {walkStates[3], walkStates[3], {133, 42}};
    private final short[][] spinningJumpStates = {jumpStates[2], jumpStates[2], {100, 1}};
    private final short[][] morphBallStates = {walkStates[2], spinningJumpStates[2], {34, 1}, {67, 1}, {67, 1}, {34, 1}};
    private final short[][] facingScreenStates = {{1, 1}, {1, 1}, {1, 1}};
    private short[][] spriteStates = iddleStates;
    private short[][] lastStates = null;

    private final boolean[] morphBallFlipStates = {true, true, true, true, false, false};

    private final byte initNumberFrame = 2;
    private byte frame = (byte)spriteStates.length;

    private final byte normalFrameLimit = 3;
    private final byte acceleratedlFrameLimit = 2;

    private byte frameLimit = normalFrameLimit;
    private final FrameTimer animationChronometer = new FrameTimer(frameLimit, frameLimit);

    private final String[][][][] generalPalette = {{{Palette.PALETTE_05, Palette.PALETTE_06}, {Palette.PALETTE_07, Palette.PALETTE_08}},  
                                                   {{Palette.PALETTE_01, Palette.PALETTE_02}, {Palette.PALETTE_03, Palette.PALETTE_04}}};
    private final String[][] metroidPalette = {Palette.PALETTE_12};
    private final String[][] spinningJumpPalette = {Palette.PALETTE_19, {}, Palette.PALETTE_12, Palette.PALETTE_13};
    private String[][] palette = new String[][] {generalPalette[0][0][0]};

    private byte paletteFrame = (byte)palette.length; 

    private final String alphaPaletteDamaged = "7a";
    private final String alphaPaletteBriggingToLive = "50";

    private String alphaPalette = "ff";

    private final short[] renderSizes = {32, 40};

    // physics --------------
    private final byte[][] sizeStates = {{8, 30}, {8, 14}}; //{8, 14}

    private final RigidBox2D rigidBox2D = new RigidBox2D(this, 4, 1, sizeStates[0][0], sizeStates[0][1]);
    private final BoxCollider2D morphBallRev = new BoxCollider2D(this, 4, -16, 8, 17); // MorphBall

    private float[] force = {0, 0, 0, 0}; // left / right / top / down
    private float[] externalForce = {0, 0}; // left / right

    private final float externalDecc = 0.03658f;
    private final float walkVel = 1.5f;
    private final int walkVelJump = 1;
    
    private final float normalJumpVel = -3.935f; // 
    private final float superJumpVel = -4.57f; //
    private final float bombJumpVel = -2.17f; //
    private final float enJumpVel = -3.25f; //
    private final float outMorpBallJumpVel = -1f;

    private final float morpBallamm = -2.1f;

    private final float gravityMax = 10f; // 2.25f
    private final float gravityAcc = 0.095f; //

    // power ups ----------
    public static final byte NONE_BEAM = 0;
    public static final byte ICE_BEAM = 1;
    public static final byte WAVE_BEAM = 2;

    public static boolean armour = false;
    private boolean maruMari = false;
    private boolean bombs = false;
    private boolean variaSuit = false;
    public static boolean longBeam = false;
    public static byte powerBeam = NONE_BEAM;
    private boolean highJump = false;
    private boolean screwAttack = false;
    
    // stade --------------
    private final float minimEnergy = 34;
    private final byte maxEnergyTanks = 7;
    private final short maxMissilePacks = 255;

    private final short shootVal = (short)(renderSizes[1]+1);
    private final short aimUpVaL = (short)(shootVal*2);
    private final short armourVal = (short)((shootVal*5));

    private short missilePacks = 50;
    private short missileAmmunition = 10;
    private float energy = minimEnergy;
    private byte energyTanks = 1;

    private final byte wallJumpTime = 8;

    private boolean xFilp = true;
    private boolean lastXFilp = true;
    private final FrameTimer lastFlipTimer = new FrameTimer(wallJumpTime);

    private boolean lastEdge0 = true;
    private final FrameTimer lastEdge0Timer = new FrameTimer(wallJumpTime);
    private boolean lastEdge1 = true;
    private final FrameTimer lastEdge1Timer = new FrameTimer(wallJumpTime);

    private boolean shoot = false;
    private boolean aimUp = false;
    private boolean spinningJump = false;
    private boolean autoSteps = false;
    private short degereesSpinningJump = 0;
    private boolean morphball = false;
    private boolean missile = false;
    private boolean facingScreen = false;
    private boolean metroidOnSamus = false;
    private boolean samusOnLava = false;
    private boolean death = false;

    private final short firstTimeToAutoShoot = 32;
    private final short secondTimeToAutoShoot = 9;

    private final FrameTimer automaticShootChronometer = new FrameTimer(firstTimeToAutoShoot);
    private final FrameTimer invincibilityTime = new FrameTimer(49, 49);
    private final FrameTimer bringToLiveTime = new FrameTimer(240, 240); // corregir tiempo

    // HUD ---------------
    Font HUDFont = new Font("", 0, 0, 5, "ff ff ff ff");

    // TEST --------------
    Font testData = new Font("", 0, 0, 5, "ff ff ff ff");

    public Player() {
        spriteRenderer.setOffSet(-8, -8);
        invincibilityTime.setOneReturn(true);
        runTick();
    }

    
    public void bringToLive(float x, float y) {
        facingScreen = true;
        bringToLiveTime.reset();
        setMorphball(false);
        force[0] = 0;
        force[1] = 0;
        force[2] = 0;
        force[3] = 0;
        externalForce[0] = 0;
        externalForce[1] = 0;

        xFilp = true;
        shoot = false;
        aimUp = false;
        spinningJump = false;
        autoSteps = false;
        degereesSpinningJump = 0;
        
        missile = false;
        invincibilityTime.setFrame(invincibilityTime.getLimit());
        transform.position.setPoints(x, y, x, y);
        spriteRenderer.setEnabled(true);
        death = false;
    }

    @Deprecated
    private void powerUPTEST() {
        if (Input.getKey('0')) {
            if (Input.getKeyPressed('1')) {
                maruMari = !maruMari;
            }
            if (Input.getKeyPressed('2')) {
                bombs = !bombs;
            }
            if (Input.getKeyPressed('3')) {
                variaSuit = !variaSuit;
            }
            if (Input.getKeyPressed('4')) {
                longBeam = !longBeam;
            }
            if (Input.getKeyPressed('5')) {
                powerBeam = (powerBeam == ICE_BEAM)? NONE_BEAM: ICE_BEAM;
            }
            if (Input.getKeyPressed('6')) {
                powerBeam = (powerBeam == WAVE_BEAM)? NONE_BEAM: WAVE_BEAM;
            }
            if (Input.getKeyPressed('7')) {
                highJump = !highJump;
            }
            if (Input.getKeyPressed('8')) {
                screwAttack = !screwAttack;
            }
            if (Input.getKeyPressed('9')) {
                armour = !armour;
            }
            if (Input.getKeyPressed('P')) {
                metroidOnSamus = !metroidOnSamus;
            }
            if (Input.getKeyPressed(KeyEvent.VK_MINUS)) {
                addMissileAmmunition((short)5);
            }
            if (Input.getKeyPressed(KeyEvent.VK_UP)) {
                addEnergy(10);
            }
            if (Input.getKeyPressed(KeyEvent.VK_DOWN)) {
                addEnergy(-10);
            }
            if (Input.getKeyPressed(KeyEvent.VK_LEFT)) {
                addEnergyTanks((byte)-1);
            }
            if (Input.getKeyPressed(KeyEvent.VK_RIGHT)) {
                addEnergyTanks((byte)+1);
            }
        } else {
            if (Input.getKeyPressed(KeyEvent.VK_LEFT)) {
                transform.position.overWrite(-1, 0);
            }
            if (Input.getKeyPressed(KeyEvent.VK_RIGHT)) {
                transform.position.overWrite(+1, 0);
            }
            if (Input.getKeyPressed(KeyEvent.VK_UP)) {
                transform.position.overWrite(0, -1);
            }
            if (Input.getKeyPressed(KeyEvent.VK_DOWN)) {
                transform.position.overWrite(0, +1);
            }
        }
    }


    private void setMorphball(boolean value) {
        boolean rev = false;
        if (value) {
            if (maruMari && rigidBox2D.getEdge()[3]) {
                if (!morphball) frame = -1;
                morphball = true;
                rigidBox2D.setSize(sizeStates[1][0], sizeStates[1][1]);
            }
        } else {
            for (GameObject gameObject : StateOneSceneController.TILE_GROUP) {
                Tile tile = (Tile) gameObject;
                if (morphBallRev.compareArea(tile.boxCollider2D)) {
                    rev = true;
                    break;
                }
            }

            for (GameObject gameObject : StateOneSceneController.ENEMY_GROUP) {
                Enemy enemy = (Enemy) gameObject;
                if (enemy.getFreeze()) {
                    if (morphBallRev.compareArea(enemy.rigidBox2D)) {
                        rev = true;
                        break;
                    }
                }    
            }

            if (!bringToLiveTime.timeUp()) rev = false;

            if (!rev) {
                transform.position.overWrite(0, sizeStates[1][1]-sizeStates[0][1]);
                morphball = false;
                force[3] = outMorpBallJumpVel;
                rigidBox2D.setSize(sizeStates[0][0], sizeStates[0][1]);
            }
        }
    }

    private void manageAmmunition() {
        if (morphball) {
            if (bombs && StateOneSceneController.BOMB_GROUP.size() < Ammunition.MAX_BOMB_SCREEN) {
                float x = 4;
                float y = 7;
                BombAmmunition ammunition = new BombAmmunition(transform.position.getX()+x, transform.position.getY()+y, 0, 0);
                StateOneSceneController.BOMB_GROUP.add(ammunition);
            }

        } else {
            float x;
            float y;
            short xVel;
            short yVel;

            if (aimUp) {
                if (xFilp) {
                    x = 6; y = -5-5; xVel = 0; yVel = -1;
                } else {
                    x = 3; y = -5-5; xVel = 0; yVel = -1;
                }
            } else {
                if (xFilp) {
                    x = 16; y = 7; xVel = 1; yVel = 0;
                } else {
                    x = -4-4; y = 7; xVel = -1; yVel = 0;
                }
            }

            if (missile) {
                if (StateOneSceneController.MISSILE_GROUP.size() < Ammunition.MAX_MISSILE_SCREEN) {
                    MissileAmmunition ammunition = new MissileAmmunition(transform.position.getX()+x, transform.position.getY()+y, Ammunition.X_VEL*xVel, Ammunition.Y_VEL*yVel);
                    StateOneSceneController.MISSILE_GROUP.add(ammunition);
                    missileAmmunition--;
                }

            } else {
                if (StateOneSceneController.BEAM_GROUP.size() < Ammunition.MAX_BEAM_SCREEN) {
                    BeamAmmunition ammunition = new BeamAmmunition(transform.position.getX()+x, transform.position.getY()+y, Ammunition.X_VEL*xVel, Ammunition.Y_VEL*yVel);
                    StateOneSceneController.BEAM_GROUP.add(ammunition);
                }
            }
        }
    }

    private void manageAutomaticShoot() {
        if (automaticShootChronometer.run()) {
            automaticShootChronometer.reset(secondTimeToAutoShoot);
            manageAmmunition();
        }
    }

    private void addMissileAmmunition(short newMissile) {
        missileAmmunition += newMissile;
        if (missileAmmunition >= missilePacks) {
            missileAmmunition = missilePacks;
        }
    }

    public void addMissilePacks(short newMissilePacks) {
        missilePacks += newMissilePacks;
        if (missilePacks >= maxMissilePacks) {
            missilePacks = maxMissilePacks;
        }
    }

    private void addEnergy(float plusEnergy) {
        energy += plusEnergy;
        if (energy >= energyTanks*100) {
            energy = energyTanks*100;
        }
    }

    private void damagePlayer(float damage) {
        energy -= damage;
        invincibilityTime.reset();
        if (energy <= 0) {
            death = true;
            energy = minimEnergy;
            spriteRenderer.setEnabled(false);
        }
    }

    private void addEnergyTanks(byte plusEnergyTanks) {
        energyTanks += plusEnergyTanks;

        if (energyTanks >= maxEnergyTanks) {
            energyTanks = maxEnergyTanks;
        }
    }

    private boolean ableToWallJumping() {
        return (!lastFlipTimer.timeUp() && spinningJump && (!lastEdge0Timer.timeUp() || !lastEdge1Timer.timeUp()) && force[3] >= 0);
    }

    public RigidBox2D getRigidBox2D() {
        return rigidBox2D;
    }

    public void playerTestPosition() {
        if (StateOneSceneController.debugMode) {
            testData.setText("----Position----\n"+
                            "\nX:"+new DecimalFormat("##.0").format(transform.position.getX())+
                            "\nY:"+new DecimalFormat("##.0").format(transform.position.getY())+
                            "\nX screen:"+new DecimalFormat("##.0").format(transform.getScreenPosition()[0])+
                            "\nY screen:"+new DecimalFormat("##.0").format(transform.getScreenPosition()[1])+
                            "\n\n-----Power Ups---\n"+
                            "\nMaru Mari:   "+maruMari+
                            "\nBombs:       "+bombs+
                            "\nVaria Suit:  "+variaSuit+
                            "\nLong Beam:   "+longBeam+
                            "\nIce Beam:    "+(powerBeam == ICE_BEAM)+
                            "\nWave Beam:   "+(powerBeam == WAVE_BEAM)+
                            "\nHigh Jump:   "+highJump+  
                            "\nScrew Attack:"+screwAttack
                        );
            testData.setScreenPosition(Game.SCREEN.getWidth()-70, 8);
        }
        testData.setEnabled(StateOneSceneController.debugMode);
    }

    public void playerHUD(){
        HUDFont.setEnabled(bringToLiveTime.timeUp());
        if (HUDFont.getEnabled()) {
            
            HUDFont.setText("energy tanks:"+energyTanks+"\nenergy:"+energy+"\nmissile packs:"+missilePacks+"\nmissile ammunition:"+missileAmmunition);
            HUDFont.setScreenPosition(24, 24);
        }
    }

    @Override
    public String[] getPalette() {
        return spinningJumpPalette[1];
    }
    
    private void manageKeys() {
        powerUPTEST();
        if (Input.getKeyPressed(KeyEvent.VK_SPACE)) {
            missile = !missile;
        }

        if (missileAmmunition == 0) {
            missile = false;
        }

        if (Input.getKeyPressed('S')) {
            setMorphball(true);
        }

        if (!rigidBox2D.getEdge()[0] && Input.getKey('A')) {
            force[0] = rigidBox2D.getEdge()[3]? walkVel*-1: walkVelJump*-1;
        } else {
            force[0] = 0;
        }

        if (!rigidBox2D.getEdge()[1] && Input.getKey('D')) {
            force[1] = rigidBox2D.getEdge()[3]? walkVel: walkVelJump;
        } else {
            force[1] = 0;
        }

        if (Input.getKey('W')) {
            if (morphball) {
                setMorphball(false);
            } else {
                aimUp = true;
            }   
        } else {
            aimUp = false;
        }

        if (Input.getKeyPressed('L')) {
            if (morphball) {
                setMorphball(false);
            } else {
                if (rigidBox2D.getEdge()[3] || ableToWallJumping()) {
                    if (highJump) {
                        force[3] = superJumpVel;
                    } else {
                        force[3] = normalJumpVel;
                    }
        
                    if ((Input.getKey('A') || Input.getKey('D')) && !(rigidBox2D.getEdge()[0] || rigidBox2D.getEdge()[1])) {
                        spinningJump = true;
                        autoSteps = true;
                        if (screwAttack) {
                            Sound.SCREW_ATTACK.stop();
                            Sound.SCREW_ATTACK.play();
                        }
                    }
                    Sound.SAMUS_JUMP.stop();
                    Sound.SAMUS_JUMP.play();
                }     
                if ((rigidBox2D.getEdge()[0] || rigidBox2D.getEdge()[1]) && spinningJump) {
                    
                }
            }
        }

        if (force[3] < 0 && Input.getKeyReleased('L')) {
            force[3] = 0;
        }

        if (Input.getKeyPressed('K')) {
            shoot = true;
            spinningJump = false;
            automaticShootChronometer.reset(firstTimeToAutoShoot);
            manageAmmunition();
        }

        if (Input.getKey('K')) {
            //spinningJump = false;
            manageAutomaticShoot();
        } else {
            shoot = false;
        }
    }

    private void manageBombColllide() {
        for (GameObject gameObject: StateOneSceneController.BOMB_GROUP) {
            BombAmmunition bomb = (BombAmmunition) gameObject;
            if (!bomb.playerCollide && bomb.canEnemyCollide && rigidBox2D.compareArea(bomb.boxCollider2D)) {
                float myMiddle = rigidBox2D.getPoints()[0][0]+(rigidBox2D.getSize()[0]/2);
                float bombMiddle = bomb.boxCollider2D.getPoints()[0][0]+(bomb.boxCollider2D.getSize()[0]/2);
                float ambMiddle = rigidBox2D.getSize()[0]/2+bomb.boxCollider2D.getSize()[0]/2;

                force[3] = bombJumpVel+Math.abs((myMiddle-bombMiddle)/(ambMiddle*2));
                if (myMiddle < bombMiddle) {
                    externalForce[0] += ((-ambMiddle*1.3f)+(myMiddle-bombMiddle))/ambMiddle;

                } else if (myMiddle > bombMiddle) {
                    externalForce[1] += ((+ambMiddle*1.3f)-(bombMiddle-myMiddle))/ambMiddle;
                }
                bomb.playerCollide = true;
            }
        }
    }

    private void manageExternalForce() {
        externalForce[0] += externalDecc;
        if (externalForce[0] >= 0) {
            externalForce[0] = 0;
        }

        externalForce[1] -= externalDecc;
        if (externalForce[1] <= 0) {
            externalForce[1] = 0;
        }
    }

    private void manageGravity() {
        if (morphball && rigidBox2D.getEdge()[3] && force[3] > 0) {
            force[3] /= morpBallamm;
        }
        if (rigidBox2D.getEdge()[2]) {
            force[3] = 0;
        }
        if (!rigidBox2D.getEdge()[3]) {
            float mulVal = 1;
            if (!invincibilityTime.timeUp()) mulVal = 2f;
            force[3] += gravityAcc*mulVal;

            if (force[3] >= gravityMax) {
                force[3] = gravityMax;
            }
        } else if (!(force[3] < 0)) {
            force[3] = 0;
            if (autoSteps) {
                Sound.SCREW_ATTACK.stop();
            }
            spinningJump = false; // cancel spininng jump
            autoSteps = false;
        }
    }

    private void manageFacingScreen() {
        if (facingScreen) {
            xFilp = true;
            if (force[0]+force[1]+force[2]+externalForce[0]+externalForce[1] != 0 || force[3] < 0 || aimUp || shoot || morphball) {
                facingScreen = false;
            }
        }
    }

    private void manageXFlip() {
        lastXFilp = xFilp;
        if (force[0]+force[1] < 0) {
            xFilp = false;
        } else if (force[0]+force[1] > 0) {
            xFilp = true;
        }
        if (lastXFilp != xFilp) {
            lastFlipTimer.reset();
        }
        lastFlipTimer.run();
        spriteRenderer.setFlip(!xFilp, false);
    }

    private void manageLastWallCollide() {
        if (lastEdge0 != rigidBox2D.getEdge()[0]) {
            lastEdge0Timer.reset();
        }
        lastEdge0Timer.run();
        lastEdge0 = rigidBox2D.getEdge()[0];

        if (lastEdge1 != rigidBox2D.getEdge()[1]) {
            lastEdge1Timer.reset();
        }
        lastEdge1Timer.run();
        lastEdge1 = rigidBox2D.getEdge()[1];
    }

    private void manageAutoSteps() {
        if (autoSteps) {
            if (!xFilp) {
                force[0] = rigidBox2D.getEdge()[3]? walkVel*-1: walkVelJump*-1;
            } else {
                force[1] = rigidBox2D.getEdge()[3]? walkVel: walkVelJump;
            }
        }
    }

    private void managePosition() {
        float xMag = (!death)? (force[0]+force[1])+(externalForce[0]+externalForce[1]): 0;
        float yMag = (!death)? force[2]+force[3]: 0;
        
        rigidBox2D.setPreData();
        transform.position.overWrite(xMag, 0);
        for (GameObject gameObject: StateOneSceneController.SLOPE_GROUP) {
            Slope slope = (Slope) gameObject;
            //(xMag != 0? slope.boxCollider2D.getSize()[1]: 0)
            rigidBox2D.slopeCollideResponse(slope.boxCollider2D, xMag, yMag, slope.getHorizontal(), slope.getVertical());
        }

        for (GameObject gameObject: StateOneSceneController.TILE_GROUP) {
            Tile tile = (Tile) gameObject;
            if (!(rigidBox2D.getSlopeCollide() && tile.getXPassableOnSlopeCollide())) {
                rigidBox2D.xTileCollideResponse(tile.boxCollider2D, xMag);
            }
        }

        if ( !(screwAttack && spinningJump) ) {
            for (GameObject gameObject: StateOneSceneController.ENEMY_GROUP) {
                Enemy enemy = (Enemy) gameObject;
                if (enemy.getFreeze()) {
                    rigidBox2D.xTileCollideResponse(enemy.rigidBox2D, xMag);
                }
            }
        }

        transform.position.overWrite(0, yMag);
        for (GameObject gameObject: StateOneSceneController.TILE_GROUP) {
            Tile tile = (Tile) gameObject;
            if (!(rigidBox2D.getSlopeCollide() && tile.getYPassableOnSlopeCollide())) {
                rigidBox2D.yTileCollideResponse(tile.boxCollider2D, yMag);
            }
        }

        if (!(screwAttack && spinningJump)) {
            for (GameObject gameObject: StateOneSceneController.ENEMY_GROUP) {
                Enemy enemy = (Enemy) gameObject;
                if (enemy.getFreeze()) {
                    rigidBox2D.yTileCollideResponse(enemy.rigidBox2D, yMag);
                }
            }
        }
        if (transform.position.getY()+rigidBox2D.getSize()[1] >= Game.SCREEN.getHeight()) {
            transform.position.setY(Game.SCREEN.getHeight()-rigidBox2D.getSize()[1]);
            rigidBox2D.getEdge()[3] = true;
        }
    }

    private void manageItemCollide() {
        for (GameObject gameObject: StateOneSceneController.ITEM_GROUP) {
            if (PowerUpPlayerChangeItem.class.isAssignableFrom(gameObject.getClass())) {
                PowerUpPlayerChangeItem powerUpPlayerChangeItem = (PowerUpPlayerChangeItem) gameObject;
                if (!powerUpPlayerChangeItem.getConsider()) {
                    switch (powerUpPlayerChangeItem.getChangeType()) {
                        case PowerUpPlayerChangeItem.CT_MARU_MARI:
                            maruMari = true;
                            break;

                        case PowerUpPlayerChangeItem.CT_BOMBS:
                            bombs = true;
                            break;

                        case PowerUpPlayerChangeItem.CT_VARIA_SUIT:
                            variaSuit = true;
                            break;
                    
                        case PowerUpPlayerChangeItem.CT_HIGH_JUMP:
                            highJump = true;
                            break;

                        case PowerUpPlayerChangeItem.CT_SCREW_ATTACK:
                            screwAttack = true;
                            break;

                        case PowerUpPlayerChangeItem.CT_LONG_BEAM:
                            longBeam = true;
                            break;
                    
                        case PowerUpPlayerChangeItem.CT_ICE_BEAM:
                            powerBeam = ICE_BEAM;
                            break;

                        case PowerUpPlayerChangeItem.CT_WAVE_BEAM:
                            powerBeam = WAVE_BEAM;
                            break;

                        case PowerUpPlayerChangeItem.CT_ENERGY_TANK:
                            addEnergyTanks(PowerUpPlayerChangeItem.ENERGY_TANK_ADD);
                            break;
            
                        case PowerUpPlayerChangeItem.CT_MISSILE_PACK:
                            addMissilePacks(PowerUpPlayerChangeItem.MISSILE_PACK_ADD);
                            break;

                        default:
                            break;
                    }
                    powerUpPlayerChangeItem.destroy();
                }
            }

            if (rigidBox2D.compareArea(((Item)gameObject).boxCollider2D)) {
                if (StadePlayerChangeItem.class.isAssignableFrom(gameObject.getClass())) {
                    StadePlayerChangeItem stadePlayerChangeItem = (StadePlayerChangeItem) gameObject;
                    if (stadePlayerChangeItem.getChangeType() == StadePlayerChangeItem.CT_ENERGY) {
                        addEnergy(stadePlayerChangeItem.getResources());
                    }

                    if (stadePlayerChangeItem.getChangeType() == StadePlayerChangeItem.CT_MISSILE) {
                        addMissileAmmunition(stadePlayerChangeItem.getResources());
                    }
                    stadePlayerChangeItem.destroy();
                }
                

                if (PowerUpPlayerChangeItem.class.isAssignableFrom(gameObject.getClass())) {
                    PowerUpPlayerChangeItem powerUpPlayerChangeItem = (PowerUpPlayerChangeItem) gameObject;
                    if (powerUpPlayerChangeItem.getConsider()) {
                        StateOneSceneController.playerCollideItemTimer.reset();
                        powerUpPlayerChangeItem.setConsider(false);
                    } 
                }
                break;
            }
        }

    }

    private void manageEnemyCollide() {
        for (GameObject gameObject: StateOneSceneController.ENEMY_GROUP) {
            Enemy enemy = (Enemy) gameObject;

            if (rigidBox2D.compareArea(enemy.rigidBox2D)) {
                float resistance = 1;
                if (variaSuit) {
                    resistance = 2;
                }

                if (screwAttack && spinningJump) {
                    enemy.damageEnemy(Ammunition.DAMAGE_3);
                } else {
                    if (invincibilityTime.timeUp()) {
                        float middle = rigidBox2D.getPoints()[0][0]+rigidBox2D.getSize()[0]/2f;
                        float enMiddle = enemy.rigidBox2D.getPoints()[0][0]+enemy.rigidBox2D.getSize()[0]/2f;
                        if (force[0] == 0 && middle < enMiddle) {
                            externalForce[0] -= walkVel*1.25f;
                        } 
                        
                        if (force[1] == 0 && middle > enMiddle) {
                            externalForce[1] += walkVel*1.25f;
                        }

                        if (force[3] >= 0) {
                            force[3] += enJumpVel;
                        } 

                        damagePlayer(enemy.getDamage()/resistance);
                    }
                    break;         
                }
            }
        }
    }

    private void manageFrameTimeUp() {
        if (spinningJump || morphball) {
            frameLimit = acceleratedlFrameLimit; 
        } else {
            frameLimit = normalFrameLimit;
        }
    }

    private void manageStates() {
        if (facingScreen) {
            spriteStates = facingScreenStates;
        } else {
            if (morphball) {
                spriteStates = morphBallStates;
            } else {
                if (force[2] + force[3] != 0) {
                    if (spinningJump) {
                        if (ableToWallJumping()) {
                            spriteStates = jumpStates;
                        } else {
                            spriteStates = spinningJumpStates;
                        }                        
                    } else {
                        spriteStates = jumpStates;
                    }
                } else {
                    if (force[0] + force[1] != 0) {
                        spriteStates = walkStates;
                    } else {
                        spriteStates = iddleStates;
                    }
                }   
            }
        }        
    }

    private void managePalette() {
        alphaPalette = (bringToLiveTime.timeUp())? ((!invincibilityTime.timeUp())?  alphaPaletteDamaged: "ff"): alphaPaletteBriggingToLive;
        spinningJumpPalette[1] = metroidOnSamus? metroidPalette[0] : generalPalette[(armour)? 1: 0][(variaSuit)? 1 : 0][(missile)? 1 : 0];
        palette = (screwAttack && spinningJump)? spinningJumpPalette: new String[][] {spinningJumpPalette[1]};

        for (int i = 0; i < palette.length; i++) {
            for (int j = 0; j < palette[i].length; j++) {
                palette[i][j] = palette[i][j].substring(0,palette[i][j].length()-2);
                palette[i][j] += alphaPalette;
            }
        }
    }

    private void setAnimation() {
        if (spriteStates != lastStates) {
            frame = 0;
            animationChronometer.setFrame(frameLimit);
        }
        lastStates = spriteStates;

        if (animationChronometer.run()) {
            animationChronometer.reset(frameLimit);
            paletteFrame ++;

            if (paletteFrame >= palette.length) {
                paletteFrame = 0;
            }

            frame ++;
            if (frame >= spriteStates.length) {
                frame = initNumberFrame;
                if (spinningJump) { // Rotation ----
                    if (!xFilp) {
                        degereesSpinningJump -= 90;
                        if (degereesSpinningJump <= -360) {
                            degereesSpinningJump = 0;
                        }
                    } else {
                        degereesSpinningJump += 90;
                        if (degereesSpinningJump >= 360) {
                            degereesSpinningJump = 0;
                        }
                    }
                }
            }

            if (!spinningJump || ableToWallJumping()) { // Rotation ----
                degereesSpinningJump = 0;
            }

            transform.setRotation(degereesSpinningJump);

            short x = spriteStates[frame][0];
            short y = spriteStates[frame][1];

            if (y == 42) { // idle / shooting / jumping
                y += (aimUp? aimUpVaL: 0)+(shoot? shootVal: 0);
            }

            y += (armour? armourVal: 0);

            if (spriteStates == morphBallStates) spriteRenderer.setFlip(!xFilp != !morphBallFlipStates[frame], false);

            spriteRenderer.setSprite(SpriteRenderer.copySprite(SpriteSheet.SAMUS_ARAN_SHEET.getSubimage(x, y, renderSizes[0], renderSizes[1])));

            spriteRenderer.swapColors(Palette.PALETTE_00, palette[paletteFrame]); 
        } 
    }

    @Override
    public void runTick() {
        super.runTick();
        if (bringToLiveTime.run() && !death) {
            manageKeys(); // manage force
            manageBombColllide();
            manageExternalForce();
            manageGravity(); // manage force
        }

        manageFacingScreen();
        manageXFlip();
        manageLastWallCollide();
        manageAutoSteps();
        
        managePosition();
        manageItemCollide();
        if (!facingScreen && !death) manageEnemyCollide();

        invincibilityTime.run();

        manageFrameTimeUp(); // animation
        manageStates(); // animation
        managePalette();
        setAnimation(); // animation
    }

    @Override
    public void render(Graphics2D graphics2d) {
        super.render(graphics2d);
        spriteRenderer.render(graphics2d);
        if (StateOneSceneController.debugMode) {
            rigidBox2D.renderLine(graphics2d);
            morphBallRev.renderLine(graphics2d);
        }
    }

    public static void generatePlayer(float x, float y) {
        Player player = new Player();
        player.bringToLive(x, y);
        //StateOneSceneController.PLAYER_GROUP.add(player);
    }

}
