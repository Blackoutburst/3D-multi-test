package dev.blackoutburst.game.utils;

import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import dev.blackoutburst.game.core.Display;

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
	}
}