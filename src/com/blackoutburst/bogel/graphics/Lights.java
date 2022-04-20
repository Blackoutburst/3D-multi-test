package com.blackoutburst.bogel.graphics;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.util.ArrayList;
import java.util.List;

import com.blackoutburst.bogel.core.Camera;
import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.ShaderProgram;
import com.blackoutburst.bogel.graphics.Shape.ShapeType;
import com.blackoutburst.bogel.maths.Vector2f;

/**
 * 
 * <p>
 * Manager and render lights on screen
 * </p>
 * 
 * @author Blackoutburst
 * @since 0.2
 *
 */
public class Lights {
	
	/** The list of lights used limited to 100 */
	protected static List<Light> lights = new ArrayList<>();
	
	public static Color ambient = Color.WHITE;
	
	/** The plane used to draw lights*/
	private static Shape plane;
	
	/**
	 * <p>
	 * Initialize important values
	 * </p>
	 * 
	 * @author Blackoutburst
	 * @since 0.2
	 */
	protected static void init() {
		plane = new Shape(ShapeType.QUAD, new Vector2f(), new Vector2f(), 0, false).setShaderProgram(ShaderProgram.COLOR_LIGHT);
	}
	
	/**
	 * <p>
	 * Draw every lights on screen<br>
	 * <b>CALL AFTER DRAWING EVERY ELEMENTS OTHERWISE THEY MIGHT TURN INVISIBLE</b>
	 * </p>
	 * 
	 * @author Blackoutburst
	 * @since 0.2 
	 */
	public static void draw() {
		plane.setPosition((Display.getWidth() / 2.0f) + Camera.getPosition().x, (Display.getHeight() / 2.0f) + Camera.getPosition().y);
		plane.setSize(Display.getSizeF());
		
		plane.shaderProgram.setUniform2f("resolution", Display.getSizeF());
		
		for (int i = 0; i < 100; i++) {
			if (i >= lights.size()) break;
			
			Light l = lights.get(i);
			plane.shaderProgram.setUniform2f("lights["+i+"].position", l.getPosition().x - Camera.getPosition().x, l.getPosition().y - Camera.getPosition().y);
			plane.shaderProgram.setUniform3f("lights["+i+"].color", l.getColor());
			plane.shaderProgram.setUniform1f("lights["+i+"].intensity", l.getIntensity());
		}
		
		glBlendFunc(GL_ONE, GL_ONE);
		plane.draw();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
}
