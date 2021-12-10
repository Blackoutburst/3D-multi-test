package com.blackoutburst.game;

import static org.lwjgl.opengl.ARBProgramInterfaceQuery.GL_UNIFORM;
import static org.lwjgl.opengl.ARBProgramInterfaceQuery.glGetProgramResourceLocation;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL41.glProgramUniformMatrix4fv;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.blackoutburst.bogel.core.Shader;
import com.blackoutburst.bogel.core.Time;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;

public class Cube {

	private static int vaoID;
	
	private static Matrix4f projection;
	private static Matrix4f view;
	private static Matrix4f model;
	
	public static int program;
	
	private static float vertices[] = {
	        -0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
	        -0.5f,  0.5f, -0.5f,  1.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  1.0f, 1.0f,

	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
	        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,

	        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,

	         0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 1.0f,

	        -0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, 1.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, 1.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,
	        -0.5f, -0.5f, -0.5f,  1.0f, 0.0f,

	        -0.5f,  0.5f, -0.5f,  1.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
	        -0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
	        -0.5f,  0.5f, -0.5f,  1.0f, 0.0f
	    };
	 
	public static void init() {
		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(70, 1000, 0.01f, projection);
		
		view = new Matrix4f();
		view.setIdentity();
		
		Matrix4f.translate(new Vector3f(0, 0, -5.0f), view, view);
		
		model = new Matrix4f();
		Matrix4f.setIdentity(model);
		Matrix4f.translate(new Vector2f(0.0f, 0.0f), model, model);
		Matrix4f.rotate((float) Math.toRadians(0), new Vector3f(0, 0, 1), model, model);
		Matrix4f.scale(new Vector3f(1, 1, 1), model, model);
		
		
		vaoID = glGenVertexArrays();
		int vbo = glGenBuffers();
		
		glBindVertexArray(vaoID);
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		((Buffer) verticesBuffer.put(vertices)).flip();

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
		glEnableVertexAttribArray(0);
		
		// UV
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		Shader vertexShader = Shader.loadShader(Shader.VERTEX, "cube.vert");
		Shader fragmentShader = Shader.loadShader(Shader.FRAGMENT, "cube.frag");
		
		program = glCreateProgram();
		glAttachShader(program, vertexShader.id);
		glAttachShader(program, fragmentShader.id);
		glLinkProgram(program);
		glDetachShader(program, vertexShader.id);
		glDetachShader(program, fragmentShader.id);
		glDeleteShader(vertexShader.id);
		glDeleteShader(fragmentShader.id);
		
		String log = glGetProgramInfoLog(program);
		
		if (log.length() != 0)
		    System.out.println(log);
	}
	
	private static void setUniforms() {
		int loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "model");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(model));
		
		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "view");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(view));
		
		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "projection");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(projection));
	}
	
	public static void draw(float x, float y, float z, float size) {
		Matrix4f.setIdentity(model);
		Matrix4f.translate(new Vector3f(x, y, z), model, model);
		Matrix4f.rotate((float) Math.toRadians(Time.getRuntime() * 10.0), new Vector3f(1, 0, 0), model, model);
		Matrix4f.rotate((float) Math.toRadians(Time.getRuntime() * 10.0), new Vector3f(0, 1, 0), model, model);
		Matrix4f.rotate((float) Math.toRadians(Time.getRuntime() * 10.0), new Vector3f(0, 0, 1), model, model);
		Matrix4f.scale(new Vector3f(size), model, model);
		
		
		setUniforms();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Main.texture.getTexture());
		glUseProgram(program);
		glBindVertexArray(vaoID);
		
		GL11.glDrawArrays(GL_TRIANGLES, 0, 36);
		
		glBindVertexArray(0);
		glUseProgram(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
