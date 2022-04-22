package com.blackoutburst.bogel.core;

import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector2i;
import org.lwjgl.system.MemoryStack;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;


/**
 * <h1>Mouse</h1>
 * 
 * <p>
 * A simple Mouse binding
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Mouse {
	
	private static float x;
	private static float y;
	private static float scroll;
	
	private static Vector2f position = new Vector2f();

	/** The mouse left button */
	private static final MouseButton leftButton = new MouseButton(0);

	/** The mouse right button */
	private static final MouseButton rightButton = new MouseButton(1);

	/** The mouse middle button */
	private static final MouseButton middleButton = new MouseButton(2);
	
	/**
	 * <p>
	 * Return the mouse X position
	 * </p>
	 * 
	 * @return float x
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static float getX() {
		return x;
	}
	
	/**
	 * <p>
	 * Set the mouse X position
	 * </p>
	 * 
	 * @param x the x position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void setX(float x) {
		Mouse.x = x;
	}
	
	/**
	 * <p>
	 * Return the mouse Y position
	 * </p>
	 * 
	 * @return float y
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static float getY() {
		return y;
	}
	
	/**
	 * <p>
	 * Set the mouse Y position
	 * </p>
	 * 
	 * @param y the y position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void setY(float y) {
		Mouse.y = y;
	}
	
	/**
	 * <p>
	 * Return the mouse position
	 * </p>
	 * 
	 * @return Vector2f position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static Vector2f getPosition() {
		return position;
	}
	
	/**
	 * <p>
	 * Set the mouse position
	 * </p>
	 * 
	 * @param position the mouse position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void setPosition(Vector2f position) {
		Mouse.position = position;
	}
	
	/**
	 * <p>
	 * Return the mouse left button
	 * </p>
	 * 
	 * @return MouseButton left
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static MouseButton getLeftButton() {
		return leftButton;
	}
	
	/**
	 * <p>
	 * Return the mouse right button
	 * </p>
	 * 
	 * @return MouseButton right
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static MouseButton getRightButton() {
		return rightButton;
	}
	
	/**
	 * <p>
	 * Return the mouse middle button (scroll wheel)
	 * </p>
	 * 
	 * @return MouseButton middle (scroll wheel)
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static MouseButton getMiddleButton() {
		return middleButton;
	}
	
	/**
	 * <p>
	 * Return the mouse scroll amount
	 * </p>
	 * 
	 * @return float scroll
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static float getScroll() {
		return scroll;
	}
	
	/**
	 * <p>
	 * Set the mouse scroll
	 * </p>
	 * 
	 * @param scroll the scroll amount
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void setScroll(float scroll) {
		Mouse.scroll = scroll;
	}

	public static Vector2f getRawPosition() {
		Vector2f size = new Vector2f();

		try (MemoryStack stack = MemoryStack.stackPush()) {
			DoubleBuffer width = stack.mallocDouble(1);
			DoubleBuffer height = stack.mallocDouble(1);
			glfwGetCursorPos(Display.getWindow(), width, height);
			size.set((float) width.get(), (float) height.get());
			((Buffer)width).clear();
			((Buffer)height).clear();
		} catch (Exception e) {
			System.err.println("Error while getting cursor position: "+e);
		}
		return (size);
	}
}
