package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Shape;

public class HotBar {

    public static int slot = 0;

    public static void update() {
        if (Mouse.getScroll() < 0) slot++;
        if (Mouse.getScroll() > 0) slot--;
        if (slot < 0) slot = 8;
        if (slot > 8) slot = 0;
    }

    public static void render() {
        new Shape(Shape.ShapeType.CIRCLE, Textures.BRICKS, Display.getWidth() / 2 - 50 * 4, 25, 50, 50, slot == 0 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.COBBLESTONE, Display.getWidth() / 2 - 50 * 3, 25, 50, 50, slot == 1 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.DIAMOND_BLOCK, Display.getWidth() / 2 - 50 * 2, 25, 50, 50, slot == 2 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.GLASS, Display.getWidth() / 2 - 50, 25, 50, 50, slot == 3 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.GRASS, Display.getWidth() / 2, 25, 50, 50, slot == 4 ? new Color(0.09f, 0.27f, 0.06f) : new Color(0.09f, 0.27f, 0.06f).mul(Color.DARK_GRAY)).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.LOG_OAK, Display.getWidth() / 2 + 50, 25, 50, 50, slot == 5 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.NOTEBLOCK, Display.getWidth() / 2 + 50 * 2, 25, 50, 50, slot == 6 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.PLANKS_OAK, Display.getWidth() / 2 + 50 * 3, 25, 50, 50, slot == 7 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
        new Shape(Shape.ShapeType.CIRCLE, Textures.STONEBRICKS, Display.getWidth() / 2 + 50 * 4, 25, 50, 50, slot == 8 ? Color.WHITE : Color.DARK_GRAY).setSmoothTexture(false).draw();
    }
}
