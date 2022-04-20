package com.blackoutburst.bogel.core;

import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

import com.blackoutburst.bogel.graphics.RenderManager;

/**
 * <h1>Window Size CallBack</h1>
 * 
 * <p>
 * Update window size <b>do not call</b>
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class WindowCallBack implements GLFWWindowSizeCallbackI {

	@Override
	public void invoke(long window, int width, int height) {
		Display.callBackSetSize(width, height);
		RenderManager.setOrtho(width, height);
	}
}
