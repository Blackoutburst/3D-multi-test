package com.blackoutburst.bogel.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.blackoutburst.bogel.core.Camera;
import com.blackoutburst.bogel.maths.Matrix;

/**
 * <h1>Render shape</h1>
 * 
 * <p>
 * Manger the quad render process
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class RenderQuad {

	/**Model matrix*/
	public static Matrix model;

	private static final int[] indices =
	{
		0, 1, 3,
		1, 2, 3
	}; 
	
	/**
	 * <p>
	 * Initialize important values<br>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	protected static void initRenderer(Shape s) {
		final float[] vertices =
		{
				0.5f, -0.5f, (1.0f + s.xo) / s.xdiv, (1.0f + s.yo) / s.ydiv, // Bottom right
				0.5f, 0.5f, (1.0f + s.xo) / s.xdiv, (0.0f + s.yo) / s.ydiv, // Top right
				-0.5f, 0.5f, (0.0f + s.xo) / s.xdiv, (0.0f + s.yo) / s.ydiv,  // Top left
				-0.5f, -0.5f, (0.0f + s.xo) / s.xdiv, (1.0f + s.yo) / s.ydiv // Bottom left
		};

		s.vaoID = glGenVertexArrays();
		s.vboID = glGenBuffers();
		s.eboID = glGenBuffers();
		
		glBindVertexArray(s.vaoID);

		final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		((Buffer) verticesBuffer.put(vertices)).flip();

		glBindBuffer(GL_ARRAY_BUFFER, s.vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		((Buffer) indicesBuffer.put(indices)).flip();
		
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		
		// UV
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, s.eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW); 
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		model = new Matrix();
	}
	
	/**
	 * <p>
	 * Set default shader uniform
	 * </p>
	 * 
	 * @param shape the shape render on screen
	 * @since 0.1
	 * @author Blackoutburst
	 */
	private static void setDefaultUniform(Shape shape) {
		shape.shaderProgram.setUniform4f("color", shape.color);
		shape.shaderProgram.setUniform1f("radius", shape.isCircle ? 0.5f : 1.0f);
	}
	
	/**
	 * <p>
	 * Set default shader matrices
	 * </p>
	 * 
	 * @param shape the shape render on screen
	 * @since 0.1
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
	 * @since 0.1
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
	 * @since 0.1
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
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
		glUseProgram(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
