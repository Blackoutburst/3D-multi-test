package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector3f;

public class BlockPlacement {

    private enum Face {
        TOP,
        BOTTOM,
        FRONT,
        LEFT,
        BACK,
        RIGHT
    }

    private static Cube selected = null;
    private static int selectedId = -1;
    private static Face face = null;

    private static void pickBlock() {
        Color color = new Color();
        double closest = 100;
        int idx = 0;

        selected = null;
        selectedId = -1;
        face = null;


        for (Cube c : Main.cubes) {
            double distance = Math.sqrt(
                    Math.pow((Camera.position.x - c.position.x), 2) +
                    Math.pow((Camera.position.y - c.position.y), 2) +
                    Math.pow((Camera.position.z - c.position.z), 2));

            if (distance < 6) color = c.interact();

            if (color.r == 255 && color.g == 0 && color.b == 0) face = Face.FRONT;
            if (color.r == 0 && color.g == 255 && color.b == 0) face = Face.BACK;
            if (color.r == 0 && color.g == 0 && color.b == 255) face = Face.LEFT;
            if (color.r == 255 && color.g == 255 && color.b == 0) face = Face.BOTTOM;
            if (color.r == 255 && color.g == 0 && color.b == 255) face = Face.RIGHT;
            if (color.r == 0 && color.g == 255 && color.b == 255) face = Face.TOP;

            if (color.r != 0 || color.g != 0 || color.b != 0) {
                double dist = Math.sqrt(
                        Math.pow((Camera.position.x - c.position.x), 2) +
                        Math.pow((Camera.position.y - c.position.y), 2) +
                        Math.pow((Camera.position.z - c.position.z), 2));
                if (dist < closest) {
                    closest = dist;
                    selected = new Cube(null, c.position.copy(), new Vector3f(1.01f), c.rotation.copy(), new Color(1,1,1,0.5f));
                    selectedId = idx;
                }
            }
            idx++;
        }
    }

    private static void breakBlock() {
        if (selectedId != -1 && Mouse.getLeftButton().isPressed())
            Main.cubes.remove(selectedId);
    }

    private static void placeBlock() {
        if (face != null && selectedId != -1 && Mouse.getRightButton().isPressed()) {
            switch (face) {
                case TOP:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x, selected.position.y + 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
                case BOTTOM:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x, selected.position.y - 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
                case FRONT:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x, selected.position.y, selected.position.z - 1), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
                case LEFT:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x - 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
                case BACK:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x, selected.position.y, selected.position.z + 1), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
                case RIGHT:
                    Main.cubes.add(new Cube(Textures.BRICKS, new Vector3f(selected.position.x + 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
                    break;
            }
        }
    }

    public static void update() {
        pickBlock();
        breakBlock();
        placeBlock();
    }

    public static void drawBoundingBox() {
        if (selected != null)
            selected.drawBoundingbox();
    }
}
