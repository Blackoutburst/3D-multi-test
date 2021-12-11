package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Shader;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBProgramInterfaceQuery.GL_UNIFORM;
import static org.lwjgl.opengl.ARBProgramInterfaceQuery.glGetProgramResourceLocation;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL41.glProgramUniform3f;
import static org.lwjgl.opengl.GL41.glProgramUniformMatrix4fv;

public class Cube {

	private static int vaoID;

	public static final Vector3f lightColor = new Vector3f(1);
	public static final Vector3f lightPos = new Vector3f(0, 5, 0);

	protected Matrix4f model;
	protected Texture texture;
	protected Vector3f position;
	protected Vector3f scale;
	protected Vector3f rotation;
	protected Color color;
	
	public static int program;
	
	private static final float VERTICES[] = {
	        -0.5f, -0.5f, -0.5f,  1.0f, 1.0f,  0.0f,  0.0f, -1.0f,
	         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,  0.0f,  0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,  0.0f,  0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,  0.0f,  0.0f, -1.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f,  0.0f, -1.0f,
	        -0.5f, -0.5f, -0.5f,  1.0f, 1.0f,  0.0f,  0.0f, -1.0f,

	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,  0.0f,  0.0f,  1.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f,  0.0f,  1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 0.0f,  0.0f,  0.0f,  1.0f,
	         0.5f,  0.5f,  0.5f,  0.0f, 0.0f,  0.0f,  0.0f,  1.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,  0.0f,  0.0f,  1.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,  0.0f,  0.0f,  1.0f,

	        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, -1.0f,  0.0f,  0.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, -1.0f,  0.0f,  0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, -1.0f,  0.0f,  0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, -1.0f,  0.0f,  0.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f, -1.0f,  0.0f,  0.0f,
	        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, -1.0f,  0.0f,  0.0f,

	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  1.0f,  0.0f,  0.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,  1.0f,  0.0f,  0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  1.0f,  0.0f,  0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  1.0f,  0.0f,  0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 1.0f,  1.0f,  0.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  1.0f,  0.0f,  0.0f,

	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  0.0f, -1.0f,  0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 0.0f,  0.0f, -1.0f,  0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f, -1.0f,  0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f, -1.0f,  0.0f,
	        -0.5f, -0.5f,  0.5f,  1.0f, 1.0f,  0.0f, -1.0f,  0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  0.0f, -1.0f,  0.0f,

	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f,  1.0f,  0.0f,
	         0.5f,  0.5f, -0.5f,  0.0f, 0.0f,  0.0f,  1.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  0.0f,  1.0f,  0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  0.0f,  1.0f,  0.0f,
	        -0.5f,  0.5f,  0.5f,  1.0f, 1.0f,  0.0f,  1.0f,  0.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f,  1.0f,  0.0f
	    };
	 
	public Cube(Texture texture, Vector3f position, Vector3f scale, Vector3f rotation, Color color) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.color = color;

		this.model = new Matrix4f();
		Matrix4f.setIdentity(this.model);

		vaoID = glGenVertexArrays();
		int vbo = glGenBuffers();
		
		glBindVertexArray(vaoID);
		
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.length);
		((Buffer) verticesBuffer.put(VERTICES)).flip();

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
		glEnableVertexAttribArray(0);
		
		// UV
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 32, 12);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, 3, GL_FLOAT, false, 32, 20);
		glEnableVertexAttribArray(2);
		
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
	
	private void setUniforms() {
		int loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "model");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(this.model));
		
		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "view");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(Camera.view));
		
		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "projection");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(Main.projection));

		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "lightColor");
		glProgramUniform3f(program, loc, lightColor.x, lightColor.y, lightColor.z);

		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "lightPos");
		glProgramUniform3f(program, loc, lightPos.x, lightPos.y, lightPos.z);

		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "viewPos");
		glProgramUniform3f(program, loc, -Camera.position.x, -Camera.position.y, -Camera.position.z);

		loc = glGetProgramResourceLocation(Cube.program, GL_UNIFORM, "color");
		glProgramUniform3f(program, loc, color.r, color.g, color.b);
	}
	
	public void draw() {
		Matrix4f.setIdentity(this.model);
		Matrix4f.translate(this.position, this.model, this.model);
		Matrix4f.rotate(this.rotation.x, new Vector3f(1, 0, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.y, new Vector3f(0, 1, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.z, new Vector3f(0, 0, 1), this.model, this.model);
		Matrix4f.scale(this.scale, this.model, this.model);

		setUniforms();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		glUseProgram(program);
		glBindVertexArray(vaoID);
		
		GL11.glDrawArrays(GL_TRIANGLES, 0, 36);
		
		glBindVertexArray(0);
		glUseProgram(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
