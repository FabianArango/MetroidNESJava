package main.java.script.general;

import java.awt.image.BufferedImage;
import javalgl.object.component.SpriteRenderer;
import main.java.Main;

public final class SpriteSheet {
    public static final BufferedImage SAMUS_ARAN_SHEET = load("/main/resources/sprite/Samus Aran.png");
    public static final BufferedImage TILE_SHEET_UNPALETTE = load("/main/resources/sprite/Tile.png");
    public static BufferedImage TILE_SHEET = SpriteRenderer.copySprite(TILE_SHEET_UNPALETTE);
    public static BufferedImage ITEM_SHEET = load("/main/resources/sprite/Item.png");
    public static BufferedImage ENEMY_SHEET = load("/main/resources/sprite/Enemy.png");

    private static BufferedImage load(String path) {
        return SpriteRenderer.load(Main.class.getResource(path));
    }

    public static BufferedImage ITEM_SHEET_SubImage(int x, int y, int w, int h) {
        return SpriteRenderer.copySprite(ITEM_SHEET.getSubimage(x, y, w, h));
    }

    public static BufferedImage TILE_SHEET_SubImage(int x, int y, int w, int h) {
        return TILE_SHEET.getSubimage(x, y, w, h);
    }

    public static BufferedImage ENEMY_SHEET_SubImage(int x, int y, int w, int h) {
        return SpriteRenderer.copySprite(ENEMY_SHEET.getSubimage(x, y, w, h));
    }

    public static void TILE_SHEET_PALETTE_SWAP(String[] oldHexPalette, String[] newHexPalette) {
        TILE_SHEET = SpriteRenderer.copySprite(TILE_SHEET_UNPALETTE);
        SpriteRenderer.swapColors(TILE_SHEET, oldHexPalette, newHexPalette);
    }
}


