package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

public class Camera {

    public static Matrix4f view;

    public static void init() {
        view = new Matrix4f();
        view.setIdentity();

        Matrix4f.translate(new Vector3f(0, 0, -5.0f), view, view);
    }
}
