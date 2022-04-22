package com.blackoutburst.game.core;

import com.blackoutburst.bogel.core.Shader;
import com.blackoutburst.bogel.core.ShaderProgram;
import com.blackoutburst.bogel.core.Time;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.main.Main;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBInstancedArrays.glVertexAttribDivisorARB;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.opengl.GL31C.glDrawArraysInstanced;

public class Water {

    private static int vaoID;
    protected Matrix model;
    protected Texture texture;
    protected Vector3f position;
    protected Vector3f scale;
    protected Vector3f rotation;
    public static ShaderProgram program;

    private static final float[] VERTICES = new float[] {

            //TOP
            -0.5f,  0.5f, -0.5f,  0.0f * 512f, 1.0f * 512f,
            0.5f,  0.5f,  0.5f,  1.0f * 512f, 0.0f * 512f,
            0.5f,  0.5f, -0.5f,  1.0f * 512f, 1.0f * 512f,
            0.5f,  0.5f,  0.5f,  1.0f * 512f, 0.0f * 512f,
            -0.5f,  0.5f, -0.5f,  0.0f * 512f, 1.0f * 512f,
            -0.5f,  0.5f,  0.5f,  0.0f * 512f, 0.0f * 512f
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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 20, 0);
        glEnableVertexAttribArray(0);

        // UV
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 20, 12);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        final Shader vertexShader = Shader.loadShader(Shader.VERTEX, "water.vert");
        final Shader fragmentShader = Shader.loadShader(Shader.FRAGMENT, "water.frag");

        program = new ShaderProgram(vertexShader, fragmentShader);
    }

    public Water(Texture texture, Vector3f position, Vector3f scale, Vector3f rotation) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;

        this.model = new Matrix();
        Matrix.setIdentity(this.model);
    }

    private void setUniforms(ShaderProgram program) {

        program.setUniformMat4("projection", Main.projection);
        program.setUniformMat4("model", this.model);
        program.setUniformMat4("view", Camera.view);
        program.setUniform1f("time", (float) Time.getRuntime());
    }

   public void draw() {
        Matrix.setIdentity(this.model);
        Matrix.translate(this.position, this.model);
        Matrix.rotate(this.rotation.x, new Vector3f(1, 0, 0), this.model);
        Matrix.rotate(this.rotation.y, new Vector3f(0, 1, 0), this.model);
        Matrix.rotate(this.rotation.z, new Vector3f(0, 0, 1), this.model);
        Matrix.scale(this.scale, this.model);

        setUniforms(program);

        glBindTexture(GL_TEXTURE_2D, texture.getTexture());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glUseProgram(program.getID());
        glBindVertexArray(vaoID);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindVertexArray(0);
        glUseProgram(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
