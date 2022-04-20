package com.blackoutburst.bogel.core;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.blackoutburst.bogel.maths.Vector2f;

/**
 * <h1>Mouse Position CallBack</h1>
 * 
 * <p>
 * Update mouse position <b>do not call</b>
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class MousePositionCallBack implements GLFWCursorPosCallbackI {

	@Override
	public void invoke(long window, double xPos, double yPos) {
		Mouse.setX((float) xPos);
		Mouse.setY((float) (Display.getHeight() - yPos));
		Mouse.setPosition(new Vector2f((float) xPos, (float) (Display.getHeight() - yPos)));
	}
}
