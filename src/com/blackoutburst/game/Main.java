package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Display.FullScreenMode;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;

public class Main {
	
	public static Texture texture;
	
	public static void main(String[] args) {
		Display display = new Display().setFullscreenMode(FullScreenMode.BORDERLESS).setDecoration(false).setTransparent(true).setClearColor(Color.TRANSPARENT).create();
		
		Cube.init();
		
		texture = new Texture("icon128.png");
		while(display.isOpen()) {
			display.clear();

			Cube.draw(0, 0, -10, 2);
			
			display.update();
		}
	}
}
