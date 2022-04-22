package com.blackoutburst.bogel.graphics;

import com.blackoutburst.bogel.core.Camera;
import com.blackoutburst.bogel.core.Core;
import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.maths.Matrix;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

/**
 * <h1>RenderManager</h1>
 * 
 * <p>
 * Manger the global OpenGL render pipeline
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class RenderManager {
	
	/**Projection matrix*/
	public static Matrix projection = new Matrix();

	/**
	 * <h1>This is automatically called when creating the window</h1>
	 * <p>
	 * Initialize important values<br>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void init() {
		setOrtho();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Lights.init();
		enableCulling();
	}
	
	/**
	 * <p>
	 * Set the projection matrix and viewport<br>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void setOrtho() {
		final int width = Core.OS_NAME.contains("mac") ? Display.getFramebufferSize().x : Display.getSize().x;
		final int height = Core.OS_NAME.contains("mac") ? Display.getFramebufferSize().y : Display.getSize().y;

		glViewport(0, 0, width, height);
		Matrix.ortho2D(projection, 0, width, 0, height, -1, 1);
	}
	
	/**
	 * <p>
	 * Enable backface culling
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void enableCulling() {
		glEnable(GL_CULL_FACE); 
		glCullFace(GL_BACK); 
	}
	
	/**
	 * <p>
	 * Disable backface culling
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void disableCulling() {
		glDisable(GL_CULL_FACE); 
	}
	
	/**
	 * <p>
	 * Enable depth test
	 * </p>
	 * 
	 * @since 0.4
	 * @author Blackoutburst
	 */
	public static void enableDepth() {
		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * <p>
	 * Disable depth test
	 * </p>
	 * 
	 * @since 0.4
	 * @author Blackoutburst
	 */
	public static void disableDepth() {
		glDisable(GL_DEPTH_TEST); 
	}
	
	/**
	 * <p>
	 * Enable additive blending
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void enableBlending() {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
	}
	
	/**
	 * <p>
	 * Disable additive blending
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void disableBlending() {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * <p>
	 * Enable wireframe render
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void enableWireFrame() {
		glPolygonMode(GL_FRONT_AND_BACK , GL_LINE);
	}
	
	/**
	 * <p>
	 * Disable wireframe render
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void disableWireFrame() {
		glPolygonMode(GL_FRONT_AND_BACK , GL_FILL);
	}

	/**
	 * <p>
	 * Set default shader uniform
	 * </p>
	 *
	 * @param shape the shape render on screen
	 * @since 0.2
	 * @author Blackoutburst
	 */
	protected static void setLightUniform(Shape shape) {
		shape.shaderProgram.setUniform2f("resolution", Display.getSizeF());
		shape.shaderProgram.setUniform3f("ambient", Lights.ambient);

		for (int i = 0; i < 100; i++) {
			if (i >= Lights.lights.size()) break;

			Light l = Lights.lights.get(i);
			shape.shaderProgram.setUniform2f("lights["+i+"].position", l.getPosition());
			shape.shaderProgram.setUniform3f("lights["+i+"].color", l.getColor());
			shape.shaderProgram.setUniform1f("lights["+i+"].intensity", l.getIntensity());
		}
	}

	/**
	 * <p>
	 * Apply shape texture processing
	 * </p>
	 *
	 * @param shape the shape render on screen
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void setTextureParameter(Shape shape) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

		if (shape.texture.missing) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, shape.isSmoothTexture() ? GL_LINEAR : GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, shape.isSmoothTexture() ? GL_LINEAR : GL_NEAREST);
		}
	}

	/**
	 * <p>
	 * Check if the shape is out of frame
	 * </p>
	 *
	 * @param shape the shape render on screen
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static boolean outOfFrame(Shape shape) {
		if (shape.position.x + shape.size.x / 2 < Camera.getPosition().x)
			return (true);
		if (shape.position.x - shape.size.x / 2 > Display.getWidth() + Camera.getPosition().x)
			return (true);
		if (shape.position.y - shape.size.y / 2 > Display.getHeight() + Camera.getPosition().y)
			return (true);
		return shape.position.y + shape.size.y / 2 < Camera.getPosition().y;
	}
}
