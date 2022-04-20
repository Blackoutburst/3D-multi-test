package com.blackoutburst.bogel.core;

import org.lwjgl.glfw.GLFW;

/**
 * <h1>Time</h1>
 * 
 * <p>
 * manager Time
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Time {

	private static long init = System.nanoTime();
	private static long lastTime = System.nanoTime();
	
	private static double deltaTime = 0;

	/** The update time */
	private static final double UPDATE = 1000000000.0 / 60.0;
	
	/**
	 * If possible use Delta Time
	 *
	 * This cap the game update at 60 times per seconds
	 * <pre>
	 * while (Display.isOpen()) {
	 *   display.clear();
	 *   
	 *   //draw game
	 *   
	 *   if (Time.doUpdate()) {
	 *     //update game logic
	 *   }
	 *   
	 *   display.update();
	 * }
	 * </pre>
	 * 
	 * @return boolean canUpdate
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static boolean doUpdate() {
		if (System.nanoTime() - Time.init > Time.UPDATE) {
			Time.init += Time.UPDATE;
			return (true);
		}
		return (false);
	}

	/**
	 * <p>
	 * Update the delta time
	 * </p>
	 * 
	 * @since 0.2
	 * @author Blackoutburst
	 */
	protected static void updateDelta() {
	    long time = System.nanoTime();
	    deltaTime = ((time - lastTime) / 1000000.0f);
	    lastTime = time;
	}
	
	
	/**
	 * <p>
	 * Get the delta time
	 * </p>
	 * 
	 * @return float deltaTime
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public static double getDelta() {
	    return (deltaTime);
	}
	
	/**
	 * <p>
	 * Give the time since the program started
	 * </p>
	 * 
	 * @return double time
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static double getRuntime() {
		return (GLFW.glfwGetTime());
	}
}
