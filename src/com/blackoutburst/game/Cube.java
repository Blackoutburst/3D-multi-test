package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Shader;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBProgramInterfaceQuery.GL_UNIFORM;
import static org.lwjgl.opengl.ARBProgramInterfaceQuery.glGetProgramResourceLocation;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.opengl.GL41C.glProgramUniform3f;
import static org.lwjgl.opengl.GL41C.glProgramUniformMatrix4fv;

public class Cube {

	private static int vaoID;

	public static final Vector3f LIGHT_COLOR = new Vector3f(1);
	public static final Vector3f LIGHT_POSITION = new Vector3f(0, 5, 0);

	protected Matrix4f model;
	protected Texture texture;
	protected Vector3f position;
	protected Vector3f scale;
	protected Vector3f rotation;
	protected Vector2f textureOffset;
	protected Color color;
	protected boolean transparent;
	protected double distance;

	public static int program;
	public static int boxProgram;

	private static final float[] VERTICES = new float[] {
			//FRONT
			-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,  0.0f, 0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,  0.0f, 0.0f, -1.0f,
	         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,  0.0f, 0.0f, -1.0f,
	         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,  0.0f, 0.0f, -1.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,  0.0f, 0.0f, -1.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f, 0.0f, -1.0f,

			//BACK
	        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,  0.0f, 0.0f, 1.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f, 0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 1.0f,  0.0f, 0.0f, 1.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 1.0f,  0.0f, 0.0f, 1.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,  0.0f, 0.0f, 1.0f,
	        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,  0.0f, 0.0f, 1.0f,

			//LEFT
	        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, -1.0f, 0.0f, 0.0f,
	        -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
	        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, -1.0f, 0.0f, 0.0f,

			//RIGHT
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  1.0f, 0.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  1.0f, 0.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  1.0f, 0.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  0.0f, 0.0f,  1.0f, 0.0f, 0.0f,

			//BOTTOM
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f, -0.5f,  1.0f, 1.0f,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f, -1.0f, 0.0f,
	         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,  0.0f, -1.0f, 0.0f,
	        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,  0.0f, -1.0f, 0.0f,
	        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,  0.0f, -1.0f, 0.0f,

			//TOP
	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,  0.0f, 1.0f, 0.0f,
	         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
	        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,  0.0f, 1.0f, 0.0f,
	        -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,  0.0f, 1.0f, 0.0f,
	    };

	public static void init() {
		vaoID = glGenVertexArrays();

		final int vbo = glGenBuffers();

		glBindVertexArray(vaoID);

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.length);
		((Buffer) verticesBuffer.put(VERTICES)).flip();

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

		final Shader vertexShader = Shader.loadShader(Shader.VERTEX, "cube.vert");
		final Shader fragmentShader = Shader.loadShader(Shader.FRAGMENT, "cube.frag");

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

		final Shader vertexBoxShader = Shader.loadShader(Shader.VERTEX, "boundingBox.vert");
		final Shader fragmentBoxShader = Shader.loadShader(Shader.FRAGMENT, "boundingBox.frag");

		boxProgram = glCreateProgram();
		glAttachShader(boxProgram, vertexBoxShader.id);
		glAttachShader(boxProgram, fragmentBoxShader.id);
		glLinkProgram(boxProgram);
		glDetachShader(boxProgram, vertexBoxShader.id);
		glDetachShader(boxProgram, fragmentBoxShader.id);
		glDeleteShader(vertexBoxShader.id);
		glDeleteShader(fragmentBoxShader.id);

		log = glGetProgramInfoLog(boxProgram);

		if (log.length() != 0)
			System.out.println(log);
	}

	public Cube(Texture texture, Vector3f position, Vector3f scale, Vector3f rotation, Color color, Vector2f textureOffset, boolean transparent) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.color = color;
		this.textureOffset = textureOffset;
		this.transparent = transparent;
		this.distance = 0;

		this.model = new Matrix4f();
		Matrix4f.setIdentity(this.model);
	}

	private void setUniforms(int program) {
		int loc = glGetProgramResourceLocation(program, GL_UNIFORM, "model");
		glProgramUniformMatrix4fv(program, loc, false, Matrix4f.getValues(this.model));
		
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

		loc = glGetProgramResourceLocation(program, GL_UNIFORM, "color");
		glProgramUniform3f(program, loc, color.r, color.g, color.b);
	}

	public Vector3f add(Vector3f init, Vector3f other) {
		return new Vector3f(init.x + other.x, init.y + other.y, init.z + other.z);
	}

	public Vector3f scale(Vector3f init, float f) {
		return new Vector3f(init.x * f, init.y * f, init.z * f);
	}

	public float dot(Vector3f init, Vector3f other) {
		return init.x * other.x + init.y * other.y + init.z * other.z;
	}

	/**
	 * Determines the point of intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
	 *
	 * @param planePoint    A point on the plane.
	 * @param planeNormal   The normal vector of the plane.
	 * @param linePoint     A point on the line.
	 * @param lineDirection The direction vector of the line.
	 * @return The point of intersection between the line and the plane, null if the line is parallel to the plane.
	 */
	public Vector3f lineIntersection(Vector3f planePoint, Vector3f planeNormal, Vector3f linePoint, Vector3f lineDirection) {
		if (dot(planeNormal, lineDirection.normalize()) == 0) {
			return null;
		}

		float t = (dot(planeNormal, planePoint) - dot(planeNormal, linePoint)) / dot(planeNormal, lineDirection.normalize());

		return add(linePoint, scale(lineDirection.normalize(), t));
	}

	public BlockPlacement.Face interact(Vector3f ray) {
		// Top
		Vector3f planePoint = new Vector3f(position.x, position.y + scale.y / 2, position.z);
		Vector3f planeNormal = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.y < 0f && (a.x <= position.x + scale.x / 2 && a.x >= position.x - scale.x / 2 &&
				a.z <= position.z + scale.z / 2 && a.z >= position.z - scale.z / 2)) return (BlockPlacement.Face.TOP);
		// Bottom
		planePoint = new Vector3f(position.x, position.y - scale.y / 2, position.z);
		planeNormal = new Vector3f(0.0f, -1.0f, 0.0f);
		a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.y > 0f && (a.x <= position.x + scale.x / 2 && a.x >= position.x - scale.x / 2 &&
				a.z <= position.z + scale.z / 2 && a.z >= position.z - scale.z / 2)) return (BlockPlacement.Face.BOTTOM);
		// Right
		planePoint = new Vector3f(position.x + scale.x / 2, position.y, position.z);
		planeNormal = new Vector3f(1.0f, 0, 0.0f);
		a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.x < 0f && (a.y <= position.y + scale.y / 2 && a.y >= position.y - scale.y / 2 &&
				a.z <= position.z + scale.z / 2 && a.z >= position.z - scale.z / 2)) return (BlockPlacement.Face.RIGHT);
		// Left
		planePoint = new Vector3f(position.x - scale.x / 2, position.y, position.z);
		planeNormal = new Vector3f(-1.0f, 0, 0.0f);
		a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.x > 0f && (a.y <= position.y + scale.y / 2 && a.y >= position.y - scale.y / 2 &&
				a.z <= position.z + scale.z / 2 && a.z >= position.z - scale.z / 2)) return (BlockPlacement.Face.LEFT);
		// Back
		planePoint = new Vector3f(position.x, position.y, position.z + scale.z / 2);
		planeNormal = new Vector3f(0.0f, 0, 1.0f);
		a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.z < 0f && (a.y <= position.y + scale.y / 2 && a.y >= position.y - scale.y / 2 &&
				a.x <= position.x + scale.x / 2 && a.x >= position.x - scale.x / 2)) return (BlockPlacement.Face.BACK);
		// Front
		planePoint = new Vector3f(position.x, position.y, position.z - scale.z / 2);
		planeNormal = new Vector3f(0.0f, 0, -1.0f);
		a = lineIntersection(planePoint, planeNormal, Camera.position.copy(), ray.copy());
		if (ray.z > 0f && (a.y <= position.y + scale.y / 2 && a.y >= position.y - scale.y / 2 &&
				a.x <= position.x + scale.x / 2 && a.x >= position.x - scale.x / 2)) return (BlockPlacement.Face.FRONT);

		return (null);
	}

	public void draw() {
		Matrix4f.setIdentity(this.model);
		Matrix4f.translate(this.position, this.model, this.model);
		Matrix4f.rotate(this.rotation.x, new Vector3f(1, 0, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.y, new Vector3f(0, 1, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.z, new Vector3f(0, 0, 1), this.model, this.model);
		Matrix4f.scale(this.scale, this.model, this.model);

		setUniforms(program);

		glBindTexture(GL_TEXTURE_2D, texture.getTexture());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		glUseProgram(program);
		glBindVertexArray(vaoID);
		
		glDrawArrays(GL_TRIANGLES, 0, 36);
		
		glBindVertexArray(0);
		glUseProgram(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void drawBoundingbox() {
		Matrix4f.setIdentity(this.model);
		Matrix4f.translate(this.position, this.model, this.model);
		Matrix4f.rotate(this.rotation.x, new Vector3f(1, 0, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.y, new Vector3f(0, 1, 0), this.model, this.model);
		Matrix4f.rotate(this.rotation.z, new Vector3f(0, 0, 1), this.model, this.model);
		Matrix4f.scale(this.scale, this.model, this.model);

		setUniforms(boxProgram);

		glUseProgram(boxProgram);
		glBindVertexArray(vaoID);

		glDrawArrays(GL_TRIANGLES, 0, 36);

		glBindVertexArray(0);
		glUseProgram(0);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
