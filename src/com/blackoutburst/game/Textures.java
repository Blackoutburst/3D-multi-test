package com.blackoutburst.game;

import com.blackoutburst.bogel.graphics.Texture;

public class Textures {

    public static Texture GRASS;
    public static Texture STONEBRICKS;
    public static Texture BRICKS;
    public static Texture COBBLESTONE;
    public static Texture DIAMOND_BLOCK;
    public static Texture GLASS;
    public static Texture LOG_OAK;
    public static Texture NOTEBLOCK;
    public static Texture PLANKS_OAK;
    public static Texture MUSH;
    public static Texture SELECTOR;
    public static Texture ATLAS;


    public static void loadTextures() {
        GRASS = new Texture("grass.png");
        STONEBRICKS = new Texture("stonebrick.png");
        BRICKS = new Texture("brick.png");
        COBBLESTONE = new Texture("cobblestone.png");
        DIAMOND_BLOCK = new Texture("diamond_block.png");
        GLASS = new Texture("glass.png");
        LOG_OAK = new Texture("log_oak.png");
        NOTEBLOCK = new Texture("noteblock.png");
        PLANKS_OAK = new Texture("planks_oak.png");
        MUSH = new Texture("mush.png");
        SELECTOR = new Texture("selector.png");
        ATLAS = new Texture("atlas.png");
    }

}
