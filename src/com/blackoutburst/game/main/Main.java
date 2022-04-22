package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.Camera;
import com.blackoutburst.game.core.Cube;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_LESS;
import static org.lwjgl.opengl.GL11C.glDepthFunc;

public class Main {

    // Create a global variable projection matrix
    public static Matrix projection = new Matrix();

    public static void main(String[] args) {
        //Create the window
        Display display = new Display().setTitle("Meinraft").create();

        //Init projection matrix
        Matrix.projectionMatrix(90, 1000, 0.1F, projection);

        //Init Camera aka view matrix
        Camera.init();


        //Init cube VAO / VBO / Shader
        Cube.init();

        //Create a new cube
        Cube head = new Cube(new Texture(""), new Vector3f(0, 0, -10), new Vector3f(0.8f), new Vector3f(0));
        Cube torso = new Cube(new Texture(""), new Vector3f(0, -1.0f, -10), new Vector3f(0.8f, 1.2f, 0.4f), new Vector3f(0));
        Cube leftArm = new Cube(new Texture(""), new Vector3f(0.6f, -1.0f, -10), new Vector3f(0.4f, 1.2f, 0.4f), new Vector3f(0));
        Cube rightArm = new Cube(new Texture(""), new Vector3f(-0.6f, -1.0f, -10), new Vector3f(0.4f, 1.2f, 0.4f), new Vector3f(0));
        Cube leftLeg = new Cube(new Texture(""), new Vector3f(0.2f, -2.2f, -10), new Vector3f(0.4f, 1.2f, 0.4f), new Vector3f(0));
        Cube rightLeg = new Cube(new Texture(""), new Vector3f(-0.2f, -2.2f, -10), new Vector3f(0.4f, 1.2f, 0.4f), new Vector3f(0));

        //Disable the mouse cursor / hide it
        GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        RenderManager.enableDepth();

        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                display.close();
            }

            //Update the camera
            Camera.update();

            //Draw the cube
            head.draw();
            torso.draw();
            leftArm.draw();
            rightArm.draw();
            leftLeg.draw();
            rightLeg.draw();

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
