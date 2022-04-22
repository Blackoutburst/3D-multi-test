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
import com.blackoutburst.game.core.Water;
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
        for (int x = -256; x < 256; x++) {
            for (int z = -256; z < 256; z++) {
                map.add(new Cube(t, new Vector3f(-x, (int) (OpenSimplex2.noise2(0, x / 40f, z / 40f) * 4), -z), new Vector3f(1), new Vector3f()));
            }
        }
        final List<Cube> tmp = new ArrayList<>(map);
        final int cubesNumber = tmp.size();

        Cube.setCubeOffset(cubesNumber, tmp);

        Water.init();
        Water w = new Water(t, new Vector3f(0, -1.8f, 0), new Vector3f(512, 0, 512), new Vector3f());

        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                display.close();
            }

            //Update the camera
            Camera.update();

            Cube.draw(map);
            //w.draw();

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
