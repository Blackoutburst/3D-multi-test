package com.blackoutburst.game.main;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Shape;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.core.Camera;
import com.blackoutburst.game.core.Cube;

public class Main {

    public static Matrix projection = new Matrix();
    public static void main(String[] args) {

        Display display = new Display().setTitle("Meinraft").create();
        Matrix.projectionMatrix(90, 0.001F, 10000, projection);


        Camera.init();
        Cube.init();

        Cube c = new Cube(new Texture(""), new Vector3f(0, 0, -10), new Vector3f(1), new Vector3f(45));

        RenderManager.enableDepth();
        while (display.isOpen()) {
            display.clear();

            Camera.update();
            c.draw();

            new Shape(Shape.ShapeType.CIRCLE, new Texture("icon128.png"), new Vector2f(100), new Vector2f( 100), 0, false).draw();

            display.update();
        }
        display.destroy();
    }

}
