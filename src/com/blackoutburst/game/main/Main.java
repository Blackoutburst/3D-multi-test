package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.*;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Shape;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.*;
import com.blackoutburst.game.core.Camera;
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

        List<Cube> map = new ArrayList<>();
        List<Grass> grass = new ArrayList<>();

        final int A = 256;
        final int SEED = new Random().nextInt();
        final int HEIGHT = 12;
        final float SMOOTH = 200;
        System.out.println("Seed: " + SEED);
        Texture t = new Texture("grass.png");
        Texture t2 = new Texture("tallgrass.png");
        for (int x = -A; x < A; x++) {
            for (int z = -A; z < A; z++) {
                map.add(new Cube(t, new Vector3f(x, OpenSimplex2.noise2(SEED, x / SMOOTH, z / SMOOTH) * HEIGHT, z), new Vector3f(1), new Vector3f()));
                if (OpenSimplex2.noise2(SEED, x / SMOOTH, z / SMOOTH) * HEIGHT > -2.0) {
                    grass.add(new Grass(t2, new Vector3f(x + (new Random().nextFloat() * 2 - 1) / 2.0f,  OpenSimplex2.noise2(SEED, x / SMOOTH, z / SMOOTH) * HEIGHT + 0.7f, z + (new Random().nextFloat() * 2 - 1) / 2.0f), new Vector3f(1), new Vector3f()));
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
                if (OpenSimplex2.noise2(SEED, x / SMOOTH, z / SMOOTH) * HEIGHT < -2.0)
                    water.add(new Water(t, new Vector3f(x, -2.2f, z), new Vector3f(1, 1, 1), new Vector3f()));
            }
        }

        List<Water> wat = new ArrayList<>(water);
        int watNumber = wat.size();

        Water.setCubeOffset(watNumber, wat);

        Shape skybox = new Shape(Shape.ShapeType.QUAD, new Vector2f(Display.getFramebufferSize().x / 2.0f,Display.getFramebufferSize().y / 3.0f), new Vector2f(1407 * 2.1f, 2000 * 2.1f), 0, false);
        skybox.setColor(new Color(1,1,1, 0.5f));
        Shader v = Shader.loadShader(Shader.VERTEX, "quad.vert");
        Shader f = Shader.loadShader(Shader.FRAGMENT, "test2.frag");
        ShaderProgram skyboxProgram = new ShaderProgram(v, f);
        skybox.setShaderProgram(skyboxProgram);
        skybox.getShaderProgram().setUniform2f("resolution", new Vector2f(Display.getFramebufferSize().x,Display.getFramebufferSize().y));

        System.gc();
        while (display.isOpen()) {
            //Clear both depth and color buffer
            display.clear();

            //Close the window when pressing escape
            if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
                display.close();
            }
            skybox.getShaderProgram().setUniform1f("time", (float) Time.getRuntime());
            skybox.getShaderProgram().setUniform3f("cameraPosition", Camera.position);

            //Update the camera
            Camera.update();

            Cube.draw(cubesNumber);

            RenderManager.disableCulling();
            Water.draw(watNumber);

            Grass.draw(gn);
            RenderManager.enableCulling();

            RenderManager.disableDepth();
            skybox.draw();
            RenderManager.enableDepth();

            //Swap buffer
            display.update();
        }

        //Clean stuff
        display.destroy();
    }

}
