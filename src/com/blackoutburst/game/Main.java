package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Shape;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static List<Cube> cubes = new ArrayList<Cube>();
	public static Matrix4f projection;

	public static void main(String[] args) {
		Display display = new Display().setFullscreenMode(Display.FullScreenMode.BORDERLESS).setClearColor(new Color(76.0f / 255.0f, 124.0f / 255.0f, 156.0f / 255.0f)).create();

		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(90, 1000, 0.01f, projection);

		GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		Camera.init();


		for (int x = -10; x < 10; x++) {
			for (int z = -10; z < 10; z++) {
				cubes.add(new Cube(Textures.GRASS, new Vector3f(x, 0, z), new Vector3f(1), new Vector3f(), new Color(0.09f, 0.27f, 0.06f)));
			}
		}
		cubes.add(new Cube(Textures.BRICKS, new Vector3f(5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(Textures.BRICKS, new Vector3f(-5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(Textures.BRICKS, new Vector3f(5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube(Textures.BRICKS, new Vector3f(-5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));

		Shape cursor = new Shape(Shape.ShapeType.CIRCLE, new Vector2f(Display.getWidth() / 2, Display.getHeight() / 2), new Vector2f(10), new Color(1,1,1,0.5f));

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		while(display.isOpen()) {
			// Must be between two clear
			display.clear();
			BlockPlacement.update();
			display.clear();

			Camera.update();

			for (Cube c : cubes)
				c.draw();

			BlockPlacement.drawBoundingBox();

			cursor.draw();
			display.update();
		}
	}
}
