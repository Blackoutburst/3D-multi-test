package com.blackoutburst.bogel.core;

import org.lwjgl.glfw.GLFWScrollCallbackI;

/**
 * <h1>Mouse Scroll CallBack</h1>
 * 
 * <p>
 * Update mouse scroll <b>do not call</b>
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class MouseScrollCallBack implements GLFWScrollCallbackI {

	@Override
	public void invoke(long window, double xOffset, double yOffset) {
		Mouse.setScroll((float) (Mouse.getScroll() + yOffset));
	}
}
