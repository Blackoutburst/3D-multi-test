package com.blackoutburst.bogel.graphics;

import com.blackoutburst.bogel.core.Camera;
import com.blackoutburst.bogel.maths.Matrix;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * <h1>RenderTriangle</h1>
 * 
 * <p>
 * Manger the triangle render process
 * </p>
 * 
 * @since 0.2
 * @author Blackoutburst
 */
public class RenderTriangle {

	/**Model matrix*/
	public static Matrix model;
	
	/**
	 * <p>
	 * Initialize important values<br>
	 * </p>
	 * 
	 * @since 0.2
	 * @author Blackoutburst
	 */
	protected static void initRenderer(Shape s) {
		final float[] vertices =
		{
				-0.5f, -0.5f, (0.0f + s.xo) / s.xdiv, (0.0f + s.yo) / s.ydiv, // Left
				0.5f, -0.5f, (1.0f + s.xo) / s.xdiv, (0.0f + s.yo) / s.ydiv, // Right
				0.0f, 0.5f, (0.5f + s.xo) / s.xdiv, (1.0f + s.yo) / s.ydiv  // Top
		};

		s.vaoID = glGenVertexArrays();
		s.vboID = glGenBuffers();
		
		glBindVertexArray(s.vaoID);

		final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		((Buffer) verticesBuffer.put(vertices)).flip();

		glBindBuffer(GL_ARRAY_BUFFER, s.vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
		glEnableVertexAttribArray(1);
		
		model = new Matrix();
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
	private static void setDefaultUniform(Shape shape) {
		shape.shaderProgram.setUniform4f("color", shape.color);
		shape.shaderProgram.setUniform1f("radius", 1.0f);
	}
	
	/**
	 * <p>
	 * Set default shader matrices
	 * </p>
	 * 
	 * @param shape the shape render on screen
	 * @since 0.2
	 * @author Blackoutburst
	 */
	private static void setMatricesUniform(Shape shape) {
		shape.shaderProgram.setUniformMat4("projection", RenderManager.projection);
		shape.shaderProgram.setUniformMat4("model", model);
		shape.shaderProgram.setUniformMat4("view", Camera.getMatrix());
	}
	
	/**
	 * <p>
	 * Apply shape transformation
	 * </p>
	 * 
	 * @param shape the shape render on screen
	 * @since 0.2
	 * @author Blackoutburst
	 */
	private static void setTransformation(Shape shape) {
		Matrix.setIdentity(model);
		Matrix.translate(shape.position, model);
		Matrix.rotate((float) Math.toRadians(shape.rotation), model);
		Matrix.scale(shape.size, model);
	}
	

	/**
	 * <p>
	 * Render the shape on screen
	 * </p>
	 * 
	 * @param shape the shape render on screen
	 * @since 0.2
	 * @author Blackoutburst
	 */
	protected static void draw(Shape shape) {
		if (RenderManager.outOfFrame(shape)) return;
		
		if (shape.reactToLight)
			RenderManager.setLightUniform(shape);
		
		if (!shape.textureless)
			glBindTexture(GL_TEXTURE_2D, shape.getTextureID());
		
		if (shape.texture != null)
			RenderManager.setTextureParameter(shape);
		
		setTransformation(shape);
		setMatricesUniform(shape);
		setDefaultUniform(shape);
		
		glUseProgram(shape.shaderProgram.getID());
		glBindVertexArray(shape.vaoID);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glBindVertexArray(0);
		glUseProgram(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
