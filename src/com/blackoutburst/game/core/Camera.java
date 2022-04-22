package com.blackoutburst.game.core;

import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.core.Time;
import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;

public class Camera {

    public static Matrix view;
    public static Vector3f position;
    public static Vector2f rotation;

    private static Vector2f lastMousePosition;

    private static final float SENSITIVITY = 0.1f;

    public static void init() {
        lastMousePosition = Mouse.getRawPosition();
        position = new Vector3f(0, 10f, 0);
        rotation = new Vector2f(0, 0);
        view = new Matrix();
        Matrix.setIdentity(view);

        Matrix.translate(position, view);
    }

    private static void rotate() {
        final Vector2f mousePosition = Mouse.getRawPosition();

        float xOffset = mousePosition.x - lastMousePosition.x;
        float yOffset = mousePosition.y - lastMousePosition.y;

        lastMousePosition = mousePosition.copy();

        xOffset *= SENSITIVITY;
        yOffset *= SENSITIVITY;


        rotation.x += xOffset;
        rotation.y += yOffset;

        if (rotation.y > 89.0f) rotation.y =  89.0f;
        if (rotation.y < -89.0f) rotation.y = -89.0f;
    }

    private static void move() {
        Vector3f velocity = new Vector3f();
        float speed;

        if (Keyboard.isKeyDown(Keyboard.W)) {
            velocity.x -= (float) (Math.sin(-rotation.x * Math.PI / 180));
            velocity.z -= (float) (Math.cos(-rotation.x * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.S)) {
            velocity.x += (float) (Math.sin(-rotation.x * Math.PI / 180));
            velocity.z += (float) (Math.cos(-rotation.x * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.A)) {
            velocity.x += (float) (Math.sin((-rotation.x - 90) * Math.PI / 180));
            velocity.z += (float) (Math.cos((-rotation.x - 90) * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.D)) {
            velocity.x += (float) (Math.sin((-rotation.x + 90) * Math.PI / 180));
            velocity.z += (float) (Math.cos((-rotation.x + 90) * Math.PI / 180));
        }

        if (Keyboard.isKeyDown(Keyboard.SPACE)) velocity.y += 1;
        if (Keyboard.isKeyDown(Keyboard.LEFT_SHIFT)) velocity.y -= 1;

        speed = Keyboard.isKeyDown(Keyboard.LEFT_CONTROL) ? 0.1f : 0.0075f;

        velocity = velocity.normalize();

        position.x += velocity.x * speed * Time.getDelta();
        position.y += velocity.y * speed * Time.getDelta();
        position.z += velocity.z * speed * Time.getDelta();
    }

    public static void update() {
        rotate();
        move();

        Matrix.setIdentity(view);

        Matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(1, 0, 0), view);
        Matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(0, 1, 0), view);

        final Vector3f reverse = new Vector3f(-position.x, -position.y, -position.z);
        Matrix.translate(reverse, view);
    }
}
