package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Shader;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.ARBInstancedArrays.glVertexAttribDivisorARB;
import static org.lwjgl.opengl.ARBProgramInterfaceQuery.GL_UNIFORM;
import static org.lwjgl.opengl.ARBProgramInterfaceQuery.glGetProgramResourceLocation;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL41.glProgramUniform3f;
import static org.lwjgl.opengl.GL41.glProgramUniformMatrix4fv;

public class World {

	public static List<Cube> cubes = new ArrayList<>();

	private static int vaoID;

	private static final Vector3f LIGHT_COLOR = new Vector3f(1);
	private static final Vector3f LIGHT_POSITION = new Vector3f(0, 5, 0);

	public static int program;

	private static final float[] VERTICES = new float[] {
			//FRONT
			-0.5f, -0.5f, -0.5f,  0, 0,  0.0f, 0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  1, 1,  0.0f, 0.0f, -1.0f,
	         0.5f, -0.5f, -0.5f,  1, 0,  0.0f, 0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  1, 1,  0.0f, 0.0f, -1.0f,
	        -0.5f, -0.5f, -0.5f,  0, 0,  0.0f, 0.0f, -1.0f,
	        -0.5f,  0.5f, -0.5f,  0, 1,  0.0f, 0.0f, -1.0f,

			//BACK
	        -0.5f, -0.5f,  0.5f,  0, 0,  0.0f, 0.0f, 1.0f,
	         0.5f, -0.5f,  0.5f,  1, 0,  0.0f, 0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  1, 1,  0.0f, 0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  1, 1,  0.0f, 0.0f, 1.0f,
	        -0.5f,  0.5f,  0.5f,  0, 1,  0.0f, 0.0f, 1.0f,
	        -0.5f, -0.5f,  0.5f,  0, 0,  0.0f, 0.0f, 1.0f,

			//LEFT
	        -0.5f,  0.5f,  0.5f,  1, 0, -1.0f, 0.0f, 0.0f,
	        -0.5f,  0.5f, -0.5f,  1, 1, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0, 1, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0, 1, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  0, 0, -1.0f, 0.0f, 0.0f,
	        -0.5f,  0.5f,  0.5f,  1, 0, -1.0f, 0.0f, 0.0f,

			//RIGHT
	         0.5f,  0.5f,  0.5f,  1, 0,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  0, 1,  1.0f, 0.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  1, 1,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  0, 1,  1.0f, 0.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1, 0,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  0, 0,  1.0f, 0.0f, 0.0f,

			//BOTTOM
	        -0.5f, -0.5f, -0.5f,  0, 1,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  1, 1,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  1, 0,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  1, 0,  0.0f, -1.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  0, 0,  0.0f, -1.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0, 1,  0.0f, -1.0f, 0.0f,

			//TOP
	        -0.5f,  0.5f, -0.5f,  0, 1,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1, 0,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  1, 1,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1, 0,  0.0f, 1.0f, 0.0f,
	        -0.5f,  0.5f, -0.5f,  0, 1,  0.0f, 1.0f, 0.0f,
	        -0.5f,  0.5f,  0.5f,  0, 0,  0.0f, 1.0f, 0.0f,
	    };

	public static void init() {
		final Shader vertexShader = Shader.loadShader(Shader.VERTEX, "cubeWorld.vert");
		final Shader fragmentShader = Shader.loadShader(Shader.FRAGMENT, "cubeWorld.frag");

		vaoID = glGenVertexArrays();

		final int vbo = glGenBuffers();

		final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.length);
		((Buffer) verticesBuffer.put(VERTICES)).flip();

		glBindVertexArray(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		// Pos
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
		glEnableVertexAttribArray(0);

		// UV
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 32, 12);
		glEnableVertexAttribArray(1);

		// Norm
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 32, 20);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);

		program = glCreateProgram();
		glAttachShader(program, vertexShader.id);
		glAttachShader(program, fragmentShader.id);
		glLinkProgram(program);
		glDetachShader(program, vertexShader.id);
		glDetachShader(program, fragmentShader.id);
		glDeleteShader(vertexShader.id);
		glDeleteShader(fragmentShader.id);

		final String log = glGetProgramInfoLog(program);

		if (log.length() != 0)
			System.out.println(log);
	}

	private static void setUniforms() {
		Matrix4f model = new Matrix4f();

		int loc = glGetProgramResourceLocation(program, GL_UNIFORM, "model");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(model.setIdentity()));
		
		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "view");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(Camera.view));
		
		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "projection");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(Main.projection));

		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "lightColor");
		glProgramUniform3f(program, loc, LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z);

		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "lightPos");
		glProgramUniform3f(program, loc, LIGHT_POSITION.x, LIGHT_POSITION.y, LIGHT_POSITION.z);

		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "viewPos");
		glProgramUniform3f(program, loc, -Camera.position.x, -Camera.position.y, -Camera.position.z);
	}

	private static void setCubeOffset(int cubesNumber) {
		final float[] translation = new float[cubesNumber * 3];

		int idx = 0;
		for (int i = 0; i < cubesNumber; i++) {
			try {cubes.get(i);} catch(Exception e) {break;}
			if (cubes.get(i) == null) break;
			translation[idx] = cubes.get(i).position.x;
			translation[idx + 1] = cubes.get(i).position.y;
			translation[idx + 2] = cubes.get(i).position.z;
			idx += 3;
		}

		final int instanceVBO = glGenBuffers();
		final FloatBuffer offsetBuffer = BufferUtils.createFloatBuffer(translation.length);
		((Buffer) offsetBuffer.put(translation)).flip();

		glBindVertexArray(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glBufferData(GL_ARRAY_BUFFER, offsetBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableVertexAttribArray(4);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glVertexAttribDivisorARB(4, 1);
		glBindVertexArray(0);

		glDeleteBuffers(instanceVBO);

		offsetBuffer.clear();
	}

	private static void setCubeColor(int cubesNumber) {
		final float[] color = new float[cubesNumber * 3];

		int idx = 0;
		for (int i = 0; i < cubesNumber; i++) {
			try {cubes.get(i);} catch(Exception e) {break;}
			if (cubes.get(i) == null) break;
			color[idx] = cubes.get(i).color.r;
			color[idx + 1] = cubes.get(i).color.g;
			color[idx + 2] = cubes.get(i).color.b;
			idx += 3;
		}

		final int instanceVBO = glGenBuffers();
		final FloatBuffer offsetBuffer = BufferUtils.createFloatBuffer(color.length);
		((Buffer) offsetBuffer.put(color)).flip();

		glBindVertexArray(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glBufferData(GL_ARRAY_BUFFER, offsetBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableVertexAttribArray(3);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glVertexAttribDivisorARB(3, 1);
		glBindVertexArray(0);

		glDeleteBuffers(instanceVBO);

		offsetBuffer.clear();
	}

	private static void setCubeTextureOffset(int cubesNumber) {
		final float[] uvo = new float[cubesNumber * 2];

		int idx = 0;
		for (int i = 0; i < cubesNumber; i++) {
			try {cubes.get(i);} catch(Exception e) {break;}
			if (cubes.get(i) == null) break;
			uvo[idx] = cubes.get(i).textureOffset.x;
			uvo[idx + 1] = cubes.get(i).textureOffset.y;
			idx += 2;
		}

		final int instanceVBO = glGenBuffers();
		final FloatBuffer offsetBuffer = BufferUtils.createFloatBuffer(uvo.length);
		((Buffer) offsetBuffer.put(uvo)).flip();

		glBindVertexArray(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glBufferData(GL_ARRAY_BUFFER, offsetBuffer, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableVertexAttribArray(5);
		glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
		glVertexAttribPointer(5, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glVertexAttribDivisorARB(5, 1);
		glBindVertexArray(0);

		glDeleteBuffers(instanceVBO);

		offsetBuffer.clear();
	}

	public static void draw() {
		final int cubesNumber = cubes.size();

		setCubeOffset(cubesNumber);
		setCubeColor(cubesNumber);
		setCubeTextureOffset(cubesNumber);
		setUniforms();

		glBindTexture(GL_TEXTURE_2D, Textures.ATLAS.getTexture());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		glUseProgram(program);
		glBindVertexArray(vaoID);

		glDrawArraysInstanced(GL_TRIANGLES, 0, 36, cubesNumber);

		glBindVertexArray(0);
		glUseProgram(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
