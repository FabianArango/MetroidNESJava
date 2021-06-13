package main.java.script.controll;

import javalgl.controll.Base;
import javalgl.controll.Input;
import javalgl.object.GameObjectGroup;
import java.awt.Component;
import main.java.Main;
import main.java.script.Game;
import main.java.script.general.Sound;
import main.java.script.interactive.Player;
import main.java.script.interactive.Slope;
import main.java.script.interactive.Tile;
import main.java.script.misc.FrameTimer;
import java.awt.event.KeyEvent;
import java.util.ConcurrentModificationException;
import java.awt.Graphics2D;

public abstract class StateOneSceneController {
    //public static final GameObjectGroup PLAYER_GROUP = new GameObjectGroup();
    public static final GameObjectGroup TILE_GROUP = new GameObjectGroup();
    public static final GameObjectGroup SLOPE_GROUP = new GameObjectGroup();
    public static final GameObjectGroup ENEMY_GROUP = new GameObjectGroup();
    public static final GameObjectGroup BOMB_GROUP = new GameObjectGroup();
    public static final GameObjectGroup BEAM_GROUP = new GameObjectGroup();
    public static final GameObjectGroup MISSILE_GROUP = new GameObjectGroup();
    public static final GameObjectGroup ITEM_GROUP = new GameObjectGroup();

    public static boolean gamePaused = false;
    public static boolean debugMode = false;
    public static FrameTimer playerCollideItemTimer = new FrameTimer(255, 255); // corregir tiempos
   
    public static boolean xScroll = true;
    public static boolean yScroll = true;
    public static byte xScrollOffset = 8;
    public static byte yScrollOffset = 16;

    public static final Player player = new Player();

    public static void init() {
        //Player.generatePlayer(0, 1);
        player.bringToLive(0, 1);
        Tile tile = new Tile(0, 32, 0, 0, true, true);
        TILE_GROUP.add(tile);
    }
    
    public static void stateOneRunTick(Component component) {
        if (Input.getKeyPressed(KeyEvent.VK_F11)) debugMode = !debugMode;
        if (Input.getKeyPressed(KeyEvent.VK_ENTER)) {
            gamePaused = !gamePaused;
            if (gamePaused) {
                Sound.PAUSE.stop();
                Sound.PAUSE.play();
            }
        }

        if (!gamePaused) {
            if (Input.getKeyPressed('X')) {
                xScroll = !xScroll;
            }
            if (Input.getKeyPressed('Y')) {
                yScroll = !yScroll;
            }
            if (Input.getKeyPressed(KeyEvent.VK_CONTROL)) {
                player.bringToLive(0, 1);
            }

            if (playerCollideItemTimer.run()) {
                try {
                    player.runTick();
                    BOMB_GROUP.runTick();
                    BEAM_GROUP.runTick();
                    MISSILE_GROUP.runTick();
                    ENEMY_GROUP.runTick();
                    ITEM_GROUP.runTick();
                    TILE_GROUP.runTick();
                    SLOPE_GROUP.runTick();
                } catch (Exception e) {
                    if (!(e instanceof ConcurrentModificationException)) {
                        throw e;
                    }
                }
            }

            manageScroll(player);
            managePlayerHUD(player);

            putTile(component);
        }
    }

    public static void stateOneRender(Graphics2D graphics2d) {
        player.render(graphics2d);
        BOMB_GROUP.render(graphics2d);
        BEAM_GROUP.render(graphics2d);
        MISSILE_GROUP.render(graphics2d);
        ENEMY_GROUP.render(graphics2d);
        ITEM_GROUP.render(graphics2d);
        TILE_GROUP.render(graphics2d);
        SLOPE_GROUP.render(graphics2d);
    }

    public static void manageScroll(Player player) {
        if (xScroll) {
            float middle = (Game.SCREEN.getWidth())/2f;
            if (player.getRigidBox2D().getCameraPoints()[0][0] < middle-xScrollOffset) {
                float xMag = player.getRigidBox2D().getCameraPoints()[0][0] - (middle-xScrollOffset);
                Base.overWriteCameraPosition(xMag, 0); // Scroll
                
            }
            if (player.getRigidBox2D().getCameraPoints()[2][0] > middle+xScrollOffset) {
                float xMag = player.getRigidBox2D().getCameraPoints()[2][0] - (middle+xScrollOffset);
                Base.overWriteCameraPosition(xMag, 0); // Scroll
            } 
        }

        if (yScroll) {
            float middle = (Game.SCREEN.getHeight())/2f;
            if (player.getRigidBox2D().getCameraPoints()[0][1] < middle-yScrollOffset) {
                float yMag = player.getRigidBox2D().getCameraPoints()[0][1] - (middle-yScrollOffset);
                Base.overWriteCameraPosition(0, yMag); // Scroll
            }
            if (player.getRigidBox2D().getCameraPoints()[2][1] > middle+yScrollOffset) {
                float yMag = player.getRigidBox2D().getCameraPoints()[2][1] - (middle+yScrollOffset);
                Base.overWriteCameraPosition(0, yMag); // Scroll
            }
        }     
    }

    public static void managePlayerHUD(Player player) {
        player.playerHUD();
        player.playerTestPosition();
    }

    @Deprecated
    private static void putTile(Component component) {
        if (!Input.getKey('0')) {
            float pixelsPerUnit = Main.game.getPixelsPerUnit();
            float x = (((float)Input.getMousePosition(Main.game)[0]-Main.game.getScreenDataSize()[0])/pixelsPerUnit)+Base.cameraPosition[0];
            float y = (((float)Input.getMousePosition(Main.game)[1]-Main.game.getScreenDataSize()[1])/pixelsPerUnit)+Base.cameraPosition[1];
            x = ((int)(x/16))*16;
            y = ((int)(y/16))*16;
            Tile.generateTileEXP(x, y);
            Slope.generateTileEXP(x, y);

        }
    }

}
