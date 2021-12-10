package com.blackoutburst.game;

import org.lwjgl.opengl.GL11;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Display.FullScreenMode;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Texture;

public class Main {
	
	public static Texture texture;
	
	public static void main(String[] args) {
		Display display = new Display().setFullscreenMode(FullScreenMode.BORDERLESS).setDecoration(false).setTransparent(true).setClearColor(Color.TRANSPARENT).create();
		
		Cube.init();
		
		texture = new Texture("icon128.png");
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		while(display.isOpen()) {
			display.clear();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

			RenderManager.enableWireFrame();
			Cube.draw(0, 0, 0, 1);
			Cube.draw(5, 1, -10, 2);
			Cube.draw(-5, 1, -10, 2);
			RenderManager.disableWireFrame();
			
			display.update();
		}
	}
}
