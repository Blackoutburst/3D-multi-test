package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;

public class Main {

    public static void main(String[] args) {
        Display display = new Display().create();

        while (display.isOpen()) {
            display.update();
            display.clear();
        }
        display.destroy();
    }

}
