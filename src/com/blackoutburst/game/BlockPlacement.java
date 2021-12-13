package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.client.C02BreakBlock;
import com.blackoutburst.network.client.C03PlaceBlock;

public class BlockPlacement {

    public enum Face {
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
    private static MousePicker picker = new MousePicker();

    private static void pickBlock() {
        double closest = 100;
        Face tmpface = null;
        int idx = 0;

        selected = null;
        selectedId = -1;
        face = null;

        picker.update();
        try {
            for (Cube c : World.cubes) {
                double distance = Math.sqrt(
                    Math.pow((Camera.position.x - c.position.x), 2) +
                    Math.pow((Camera.position.y - c.position.y), 2) +
                    Math.pow((Camera.position.z - c.position.z), 2));

                if (distance < 7) tmpface = c.interact(picker.getCurrentRay());

                if (tmpface != null && distance < closest) {
                    closest = distance;
                    selected = new Cube(null, c.position, new Vector3f(1.01f), c.rotation.copy(), new Color(1, 1, 1, 0.5f), new Vector2f());
                    selectedId = idx;
                    face = tmpface;
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

    private static String getBlockFromSlot() {
        switch (HotBar.slot) {
            case 0: return("BRICKS");
            case 1: return("COBBLESTONE");
            case 2: return("DIAMOND_BLOCK");
            case 3: return("GLASS");
            case 4: return("GRASS");
            case 5: return("LOG_OAK");
            case 6: return("NOTEBLOCK");
            case 7: return("PLANKS_OAK");
            case 8: return("STONEBRICKS");
        }
        return "BRICKS";
    }

    private static Color getBlockColorFromSlot() {
        return (HotBar.slot == 4 ? new Color(0.09f, 0.27f, 0.06f) : Color.WHITE);
    }

    private static void placeBlock() {
        if (face != null && selectedId != -1 && Mouse.getRightButton().isPressed()) {
            switch (face) {
                case TOP:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x, selected.position.y + 1, selected.position.z), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                case BOTTOM:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x, selected.position.y - 1, selected.position.z), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                case FRONT:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x, selected.position.y, selected.position.z - 1), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                case LEFT:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x - 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                case BACK:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x, selected.position.y, selected.position.z + 1), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                case RIGHT:
                    new C03PlaceBlock(getBlockFromSlot(), new Vector3f(selected.position.x + 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), getBlockColorFromSlot()).writePacketData().sendPacket();
                break;
                default: return;
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
