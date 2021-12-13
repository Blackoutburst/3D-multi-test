package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Shape;

public class HotBar {

    public static int slot = 0;

    private static final float HALF = Display.getWidth() / 2.0f;

    private static final Shape SLOT0 = new Shape(Shape.ShapeType.CIRCLE, Textures.BRICKS, HALF - 50 * 4, 25, 50, 50, slot == 0 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT1 = new Shape(Shape.ShapeType.CIRCLE, Textures.COBBLESTONE, HALF - 50 * 3, 25, 50, 50, slot == 1 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT2 = new Shape(Shape.ShapeType.CIRCLE, Textures.DIAMOND_BLOCK, HALF - 50 * 2, 25, 50, 50, slot == 2 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT3 = new Shape(Shape.ShapeType.CIRCLE, Textures.GLASS, HALF - 50, 25, 50, 50, slot == 3 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT4 = new Shape(Shape.ShapeType.CIRCLE, Textures.GRASS, HALF, 25, 50, 50, slot == 4 ? new Color(0.09f, 0.27f, 0.06f) : new Color(0.09f, 0.27f, 0.06f).mul(Color.DARK_GRAY)).setSmoothTexture(false);
    private static final Shape SLOT5 = new Shape(Shape.ShapeType.CIRCLE, Textures.LOG_OAK, HALF + 50, 25, 50, 50, slot == 5 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT6 = new Shape(Shape.ShapeType.CIRCLE, Textures.NOTEBLOCK, HALF + 50 * 2, 25, 50, 50, slot == 6 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT7 = new Shape(Shape.ShapeType.CIRCLE, Textures.PLANKS_OAK, HALF + 50 * 3, 25, 50, 50, slot == 7 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);
    private static final Shape SLOT8 = new Shape(Shape.ShapeType.CIRCLE, Textures.STONEBRICKS, HALF + 50 * 4, 25, 50, 50, slot == 8 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false);

    public static void update() {
        if (Mouse.getScroll() < 0) slot++;
        if (Mouse.getScroll() > 0) slot--;
        if (slot < 0) slot = 8;
        if (slot > 8) slot = 0;
    }

    public static void render() {
        SLOT0.draw();
        SLOT1.draw();
        SLOT2.draw();
        SLOT3.draw();
        SLOT4.draw();
        SLOT5.draw();
        SLOT6.draw();
        SLOT7.draw();
        SLOT8.draw();
    }
}
