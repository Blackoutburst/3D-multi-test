package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.Camera;
import com.blackoutburst.game.core.Cube;
import com.blackoutburst.game.core.OpenSimplex2;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

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

        Display.clearColor = new Color(115/255.0f, 186/255.0f, 215/255.0f);


        //Init cube VAO / VBO / Shader
        Cube.init();

        //Disable the mouse cursor / hide it
        GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        RenderManager.enableDepth();

        List<Cube> map = new ArrayList<>();

        Texture t = new Texture("grass.png");
        for (int x = 0; x < 64; x++) {
            for (int z = 0; z < 64; z++) {
                map.add(new Cube(t, new Vector3f(-x, (int) (OpenSimplex2.noise2(0, x / 40f, z / 40f) * 3), -z), new Vector3f(1), new Vector3f()));
            }
        }

        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                display.close();
            }

            //Update the camera
            Camera.update();

            for (Cube c : map) {
                c.draw();
            }

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
