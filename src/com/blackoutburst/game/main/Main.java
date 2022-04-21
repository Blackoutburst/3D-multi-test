package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix4f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.Camera;
import com.blackoutburst.game.core.Cube;

public class Main {

    public static Matrix4f projection = new Matrix4f();

    public static void main(String[] args) {
        Display display = new Display().setTitle("Meinraft").create();

        projection.setIdentity();
        Matrix4f.projectionMatrix(90, 1000, 0.01f, projection);

        Cube.init();
        Camera.init();

        Cube cube = new Cube(new Texture(""), new Vector3f(), new Vector3f(1), new Vector3f(1));

        while (display.isOpen()) {
            display.clear();
            Camera.update();

            cube.draw();
            display.update();
        }
        display.destroy();
    }

}
