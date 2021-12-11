package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Mouse;
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

	private enum Face {
		TOP,
		BOTTOM,
		FRONT,
		LEFT,
		BACK,
		RIGHT
	}

	public static Matrix4f projection;
	public static double closest = 0;

	public static void main(String[] args) {
		Display display = new Display().setFullscreenMode(Display.FullScreenMode.BORDERLESS).setClearColor(new Color(76.0f / 255.0f, 124.0f / 255.0f, 156.0f / 255.0f)).create();

		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(90, 1000, 0.01f, projection);

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
			int selectedId = -1;
			int idx = 0;
			Face face = null;
			for (Cube c : cubes) {
				double distance = Math.sqrt(
						Math.pow((Camera.position.x - c.position.x), 2) +
								Math.pow((Camera.position.y - c.position.y), 2) +
								Math.pow((Camera.position.z - c.position.z), 2));
				Color color = new Color();
				if (distance < 6) {
					color = c.interact();
				}


				if (color.r == 255 && color.g == 0 && color.b == 0) face = Face.FRONT;
				if (color.r == 0 && color.g == 255 && color.b == 0) face = Face.BACK;
				if (color.r == 0 && color.g == 0 && color.b == 255) face = Face.LEFT;
				if (color.r == 255 && color.g == 255 && color.b == 0) face = Face.BOTTOM;
				if (color.r == 255 && color.g == 0 && color.b == 255) face = Face.RIGHT;
				if (color.r == 0 && color.g == 255 && color.b == 255) face = Face.TOP;


				if (color.r != 0 || color.g != 0 || color.b != 0) {
					double dist = Math.sqrt(
							Math.pow((Camera.position.x - c.position.x), 2) +
									Math.pow((Camera.position.y - c.position.y), 2) +
									Math.pow((Camera.position.z - c.position.z), 2));
					if (dist < closest) {
						closest = dist;
						selected = new Cube(null, c.position.copy(), new Vector3f(1.01f), c.rotation.copy(), new Color(1,1,1,0.5f));
						selectedId = idx;
					}
				}
				idx++;
			}

			if (selectedId != -1 && Mouse.getLeftButton().isPressed())
				cubes.remove(selectedId);

			if (face != null && selectedId != -1 && Mouse.getRightButton().isPressed()) {
				switch (face) {
					case TOP:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x, selected.position.y + 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
					case BOTTOM:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x, selected.position.y - 1, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
					case FRONT:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x, selected.position.y, selected.position.z - 1), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
					case LEFT:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x - 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
					case BACK:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x, selected.position.y, selected.position.z + 1), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
					case RIGHT:
						cubes.add(new Cube(bricks, new Vector3f(selected.position.x + 1, selected.position.y, selected.position.z), new Vector3f(1), new Vector3f(), Color.WHITE));
					break;
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
