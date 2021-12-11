package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Shape;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static Matrix4f projection;
	public static double closest = 0;

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

		Shape cursor = new Shape(Shape.ShapeType.CIRCLE, new Vector2f(Display.getWidth() / 2, Display.getHeight() / 2), new Vector2f(10), new Color(1,1,1,0.5f));

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		while(display.isOpen()) {
			display.clear();

			Cube selected = null;
			closest = 100;
			for (Cube c : cubes) {
				double distance = Math.sqrt(
						Math.pow((Camera.position.x - c.position.x), 2) +
								Math.pow((Camera.position.y - c.position.y), 2) +
								Math.pow((Camera.position.z - c.position.z), 2));
				Color color = new Color();
				if (distance < 6) {
					color = c.interact();
				}

				/*
				if (color.r == 255 && color.g == 0 && color.b == 0) System.out.println("FRONT");
				if (color.r == 0 && color.g == 255 && color.b == 0) System.out.println("BACK");
				if (color.r == 0 && color.g == 0 && color.b == 255) System.out.println("LEFT");
				if (color.r == 255 && color.g == 255 && color.b == 0) System.out.println("BOTTOM");
				if (color.r == 255 && color.g == 0 && color.b == 255) System.out.println("RIGHT");
				if (color.r == 0 && color.g == 255 && color.b == 255) System.out.println("TOP");
				*/

				if (color.r != 0 || color.g != 0 || color.b != 0) {
					double dist = Math.sqrt(
							Math.pow((Camera.position.x - c.position.x), 2) +
									Math.pow((Camera.position.y - c.position.y), 2) +
									Math.pow((Camera.position.z - c.position.z), 2));
					if (dist < closest) {
						closest = dist;
						selected = new Cube(null, c.position.copy(), new Vector3f(1.01f), c.rotation.copy(), new Color(1,1,1,0.5f));
					}
				}
			}

			display.clear();
			Camera.update();

			for (Cube c : cubes)
				c.draw();

			if (selected != null) {
				selected.drawBoundingbox();
			}
			cursor.draw();
			display.update();
		}
	}
}
