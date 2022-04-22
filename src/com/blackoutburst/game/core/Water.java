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
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f
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

        final Shader vertexShader = Shader.loadShader(Shader.VERTEX, "waterWavy.vert");
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

    private static void setUniforms(ShaderProgram program) {
        Matrix model = new Matrix();
        Matrix.setIdentity(model);

        program.setUniform3f("lightColor", Color.WHITE);
        program.setUniform3f("viewPos", Camera.position);
        program.setUniform3f("color", Color.GREEN);
        program.setUniformMat4("projection", Main.projection);
        program.setUniformMat4("model", model);
        program.setUniformMat4("view", Camera.view);
        program.setUniform1f("time", (float) Time.getRuntime());
    }

    public static void setCubeOffset(int cubesNumber, List<Water> toDraw) {
        final float[] translation = new float[cubesNumber * 3];

        int idx = 0;
        for (int i = 0; i < cubesNumber; i++) {
            translation[idx] = toDraw.get(i).position.x;
            translation[idx + 1] = toDraw.get(i).position.y;
            translation[idx + 2] = toDraw.get(i).position.z;
            idx += 3;
        }

        final int instanceVBO = glGenBuffers();
        final FloatBuffer offsetBuffer = BufferUtils.createFloatBuffer(translation.length);
        ((Buffer) offsetBuffer.put(translation)).flip();

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

        ((Buffer)offsetBuffer).clear();
    }

    public static void draw(List<Water> toDraw) {
        final int cubesNumber = toDraw.size();

        setUniforms(program);

        glBindTexture(GL_TEXTURE_2D, toDraw.get(0).texture.getTexture());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glUseProgram(program.getID());
        glBindVertexArray(vaoID);

        glDrawArraysInstanced(GL_TRIANGLES, 0, 36, cubesNumber);

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
