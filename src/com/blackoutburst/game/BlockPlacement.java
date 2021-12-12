package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.client.C02BreakBlock;
import com.blackoutburst.network.client.C03PlaceBlock;

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


        try {
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
                        selected = new Cube(null, c.position.copy(), new Vector3f(1.01f), c.rotation.copy(), new Color(1, 1, 1, 0.5f));
                        selectedId = idx;
                    }
                }
                idx++;
            }
        } catch (Exception e) {}
    }

    private static void breakBlock() {
        if (selectedId != -1 && Mouse.getLeftButton().isPressed()) {
            new C02BreakBlock(selectedId).writePacketData().sendPacket();
        }
    }

    private static void placeBlock() {
        if (face != null && selectedId != -1 && Mouse.getRightButton().isPressed()) {
            switch (face) {
                case TOP:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x, selected.position.y + 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
                break;
                case BOTTOM:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x, selected.position.y - 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
                break;
                case FRONT:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x, selected.position.y, selected.position.z - 1), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
                break;
                case LEFT:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x - 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
                break;
                case BACK:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x, selected.position.y, selected.position.z + 1), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
                break;
                case RIGHT:
                    new C03PlaceBlock("BRICKS", new Vector3f(selected.position.x + 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE).writePacketData().sendPacket();
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
