package com.blackoutburst.game;

import com.blackoutburst.bogel.graphics.Texture;

public class Textures {

    public static Texture GRASS;
    public static Texture BRICKS;
    public static Texture MUSH;

    public static void loadTextures() {
        GRASS = new Texture("grass.png");
        BRICKS = new Texture("stonebrick.png");
        MUSH = new Texture("mush.png");
    }

}
