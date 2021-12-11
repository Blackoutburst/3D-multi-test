package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static Matrix4f projection;

	public static void main(String[] args) {
		Display display = new Display().setClearColor(new Color(76.0f / 255.0f, 124.0f / 255.0f, 156.0f / 255.0f)).create();

		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(70, 1000, 0.01f, projection);

		GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		Camera.init();

		List<Cube> cubes = new ArrayList<Cube>();
		Texture grass = new Texture("grass.png");
		Texture bricks = new Texture("stonebrick.png");

		for (int x = -10; x < 10; x++) {
			for (int z = -10; z < 10; z++) {
				cubes.add(new Cube(grass, new Vector3f(x, 0, z), new Vector3f(1), new Vector3f(), new Color(0.09f, 0.27f, 0.06f)));
			}
		}
		cubes.add(new Cube(bricks, new Vector3f(5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(bricks, new Vector3f(-5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(bricks, new Vector3f(5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(bricks, new Vector3f(-5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));

		while(display.isOpen()) {
			display.clear();

			Camera.update();

			for (Cube c : cubes)
				c.draw();

			display.update();
		}
	}
}
