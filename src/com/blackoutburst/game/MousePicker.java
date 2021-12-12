package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.bogel.maths.Vector4f;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    public MousePicker() {
        this.currentRay = new Vector3f();
        this.projectionMatrix = new Matrix4f(Main.projection);
        this.viewMatrix = new Matrix4f(Camera.view);
    }

    public Vector3f getCurrentRay() {
        return (currentRay);
    }

    public void update() {
        viewMatrix = new Matrix4f(Camera.view);
        currentRay = calculateRay();
    }

    private Vector3f calculateRay() {
        final float mouseX = Display.getWidth() / 2;
        final float mouseY = Display.getHeight() / 2;

        final Vector2f normMouseCoord = normalizeScreenCoord(mouseX, mouseY);
        final Vector4f clipCoord = new Vector4f(normMouseCoord.x, normMouseCoord.y, -1f, 1f);
        final Vector4f eyeCoord = toEyeCoord(clipCoord);
        final Vector3f worldRay = toWorldCoord(eyeCoord);

        return (worldRay);
    }

    private Vector3f toWorldCoord(Vector4f eyeCoord) {
        final Matrix4f invert = Matrix4f.invert(viewMatrix, null);
        final Vector4f rayWorld = Matrix4f.transform(invert, eyeCoord, null);
        final Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);

        return (mouseRay.normalize());
    }

    private Vector4f toEyeCoord(Vector4f clipCoord) {
        final Matrix4f invert = Matrix4f.invert(projectionMatrix, null);
        final Vector4f eyeCoord = Matrix4f.transform(invert, clipCoord, null);

        return (new Vector4f(eyeCoord.x, eyeCoord.y, -1f, 0f));
    }

    private Vector2f normalizeScreenCoord(float mouseX, float mouseY) {
        final float x = (2f * mouseX) / Display.getWidth() - 1f;
        final float y = (2f * mouseY) / Display.getHeight() - 1f;

        return (new Vector2f(x, y));
    }

}
