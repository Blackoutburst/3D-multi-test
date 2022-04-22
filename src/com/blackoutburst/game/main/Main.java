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

public class Main {

    // Create a global variable projection matrix
    public static Matrix projection = new Matrix();

    public static void main(String[] args) {
        //Create the window
        Display display = new Display().setTitle("Meinraft").create();

        //Init projection matrix
        Matrix.projectionMatrix(90, 0.001F, 10000, projection);

        //Init Camera aka view matrix
        Camera.init();


        //Init cube VAO / VBO / Shader
        Cube.init();

        //Create a new cube
        Cube c = new Cube(new Texture(""), new Vector3f(0, 0, -10), new Vector3f(1), new Vector3f(45));

        //Disable the mouse cursor / hide it
        GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        //Enable depth buffer
        RenderManager.enableDepth();

        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE))
                display.close();

            //Update the camera
            Camera.update();

            //Draw the cube
            c.draw();

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
