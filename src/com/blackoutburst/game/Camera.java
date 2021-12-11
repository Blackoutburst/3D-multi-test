package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.core.Mouse;
import com.blackoutburst.bogel.core.Time;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;

public class Camera {

    public static Matrix4f view;
    public static Vector3f position;
    public static Vector2f rotation;

    private static Vector2f lastMousePosition;

    private static final float SENSITIVITY = 0.1f;
    private static float speed = 0.005f;

    public static void init() {
        lastMousePosition = new Vector2f(Display.getWidth() / 2, Display.getHeight() / 2);
        position = new Vector3f(0, 0, -5.0F);
        rotation = new Vector2f(0, 0);
        view = new Matrix4f();
        view.setIdentity();

        Matrix4f.translate(position, view, view);
    }

    private static void rotate() {
        Vector2f mousePosition = Mouse.getPosition();

        float xOffset = mousePosition.x - lastMousePosition.x;
        float yOffset = lastMousePosition.y - mousePosition.y;
        lastMousePosition.x = mousePosition.x;
        lastMousePosition.y = mousePosition.y;

        xOffset *= SENSITIVITY;
        yOffset *= SENSITIVITY;

        rotation.x += xOffset;
        rotation.y += yOffset;

        if(rotation.y > 89.0f)
            rotation.y =  89.0f;
        if(rotation.y < -89.0f)
            rotation.y = -89.0f;
    }

    private static void move() {
        Vector3f velocity = new Vector3f();

        if (Keyboard.isKeyDown(Keyboard.W)) {
            velocity.x += (float) (Math.sin(-rotation.x * Math.PI / 180));
            velocity.z += (float) (Math.cos(-rotation.x * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.S)) {
            velocity.x -= (float) (Math.sin(-rotation.x * Math.PI / 180));
            velocity.z -= (float) (Math.cos(-rotation.x * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.A)) {
            velocity.x -= (float) (Math.sin((-rotation.x - 90) * Math.PI / 180));
            velocity.z -= (float) (Math.cos((-rotation.x - 90) * Math.PI / 180));
        }
        if (Keyboard.isKeyDown(Keyboard.D)) {
            velocity.x -= (float) (Math.sin((-rotation.x + 90) * Math.PI / 180));
            velocity.z -= (float) (Math.cos((-rotation.x + 90) * Math.PI / 180));
        }

        if (Keyboard.isKeyDown(Keyboard.SPACE)) velocity.y -= 1;
        if (Keyboard.isKeyDown(Keyboard.LEFT_SHIFT)) velocity.y += 1;
        speed = Keyboard.isKeyDown(Keyboard.LEFT_CONTROL) ? 0.0075f : 0.005f;
        velocity = velocity.normalize();

        position.x += velocity.x * Time.getDelta() * speed;
        position.y += velocity.y * Time.getDelta() * speed;
        position.z += velocity.z * Time.getDelta() * speed;
    }

    public static void update() {
        rotate();
        move();

        view.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(1, 0, 0), view, view);
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(0, 1, 0), view, view);
        Matrix4f.translate(position, view, view);
    }
}
