package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.*;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    // Create a global variable projection matrix
    public static Matrix projection = new Matrix();

    public static void main(String[] args) {
        //Create the window
        Display display = new Display().setFullscreenMode(Display.FullScreenMode.NONE).setTitle("Meinraft").create();

        //Init projection matrix
        Matrix.projectionMatrix(90, 1000, 0.1F, projection);

        //Init Camera aka view matrix
        Camera.init();

        Display.clearColor = new Color(115/255.0f, 186/255.0f, 255/255.0f);

        //Init cube VAO / VBO / Shader
        Cube.init();

        Grass.init();
        Water.init();

        //Disable the mouse cursor / hide it
        GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        RenderManager.enableDepth();

        List<Cube> map = new ArrayList<>();
        List<Grass> grass = new ArrayList<>();

        int A = 350;
        Texture t = new Texture("grass.png");
        Texture t2 = new Texture("tallgrass.png");
        for (int x = -A; x < A; x++) {
            for (int z = -A; z < A; z++) {
                map.add(new Cube(t, new Vector3f(x, (int) (OpenSimplex2.noise2(8, x / 100f, z / 100f) * 7), z), new Vector3f(1), new Vector3f()));
                if ((int) (OpenSimplex2.noise2(8, x / 100f, z / 100f) * 7) > -2.0) {
                    grass.add(new Grass(t2, new Vector3f(x + new Random().nextFloat() / 2.0f, (int) (OpenSimplex2.noise2(8, x / 100f, z / 100f) * 7) + 0.7f, z + new Random().nextFloat() / 2.0f), new Vector3f(1), new Vector3f()));
                }
            }
        }
        List<Cube> tmp = new ArrayList<>(map);
        int cubesNumber = tmp.size();

        Cube.setCubeOffset(cubesNumber, tmp);

        List<Grass> tmp2 = new ArrayList<>(grass);
        int gn = tmp2.size();

        Grass.setCubeOffset(gn, tmp2);


        List<Water> water = new ArrayList<>();

        for (float x = -A; x < A; x++) {
            for (float z = -A; z < A ; z++) {
                if ((int) (OpenSimplex2.noise2(8, x / 100f, z / 100f) * 7) < -2.0)
                    water.add(new Water(t, new Vector3f(x, -2.2f, z), new Vector3f(1, 1, 1), new Vector3f()));
            }
        }
        System.out.println(water.size());

        List<Water> wat = new ArrayList<>(water);
        int watNumber = wat.size();

        Water.setCubeOffset(watNumber, wat);


        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            if (Mouse.getRightButton().isPressed()) {
                map.clear();
                wat.clear();
                grass.clear();
                tmp.clear();
                tmp2.clear();
                water.clear();

                final int SEED = new Random().nextInt();
                map = new ArrayList<>();
                grass = new ArrayList<>();

                A = 350;
                for (int x = -A; x < A; x++) {
                    for (int z = -A; z < A; z++) {
                        map.add(new Cube(t, new Vector3f(x, (int) (OpenSimplex2.noise2(SEED, x / 100f, z / 100f) * 7), z), new Vector3f(1), new Vector3f()));
                        if ((int) (OpenSimplex2.noise2(SEED, x / 100f, z / 100f) * 7) > -2.0) {
                            grass.add(new Grass(t2, new Vector3f(x + new Random().nextFloat() / 2.0f, (int) (OpenSimplex2.noise2(SEED, x / 100f, z / 100f) * 7) + 0.7f, z + new Random().nextFloat() / 2.0f), new Vector3f(1), new Vector3f()));
                        }
                    }
                }
                tmp = new ArrayList<>(map);
                cubesNumber = tmp.size();

                Cube.setCubeOffset(cubesNumber, tmp);

                tmp2 = new ArrayList<>(grass);
                gn = tmp2.size();

                Grass.setCubeOffset(gn, tmp2);

                water = new ArrayList<>();

                for (float x = -A; x < A; x++) {
                    for (float z = -A; z < A ; z++) {
                        if ((int) (OpenSimplex2.noise2(SEED, x / 100f, z / 100f) * 7) < -2.0)
                            water.add(new Water(t, new Vector3f(x, -2.2f, z), new Vector3f(1, 1, 1), new Vector3f()));
                    }
                }

                wat = new ArrayList<>(water);
                watNumber = wat.size();

                Water.setCubeOffset(watNumber, wat);

            }

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                display.close();
            }

            //Update the camera
            Camera.update();

            Cube.draw(cubesNumber);

            Water.draw(watNumber);

            RenderManager.disableCulling();
            Grass.draw(gn);
            RenderManager.enableCulling();

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
