package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Core;
import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.core.Keyboard;
import com.blackoutburst.bogel.core.Time;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.RenderManager;
import com.blackoutburst.bogel.graphics.Shape;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.Connection;
import org.lwjgl.glfw.GLFW;

public class Main {

	public static Connection connection = null;
	private static Thread connectionThread = null;

	public static Matrix4f projection;

	private static void startConnectionThread() {
		connectionThread = new Thread(() -> {
			while (!connection.socket.isClosed()) {
				connection.read();
			}
		});
		connectionThread.setDaemon(true);
		connectionThread.setName("Connection input thread");
		connectionThread.start();
	}
	public static String toIntVector(Vector3f vec) {
		return ((int)(vec.x)+","+(int)(vec.y)+","+(int)(vec.z));
	}

	public static void main(String[] args) {
		Display display = new Display().setFullscreenMode(Display.FullScreenMode.BORDERLESS)
				.setClearColor(new Color(76.0f / 255.0f, 124.0f / 255.0f, 156.0f / 255.0f))
				.create().setVSync(true);

		Textures.loadTextures();

		Cube.init();
		World.init();

		connection = new Connection();
		connection.connect();
		startConnectionThread();

		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(90, 1000, 0.01f, projection);

		GLFW.glfwSetInputMode(Display.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		Camera.init();

		final Shape cursor = new Shape(Shape.ShapeType.CIRCLE, new Vector2f(Display.getWidth() / 2.0f, Display.getHeight() / 2.0f), new Vector2f(10), new Color(1,1,1,0.5f));

		while(display.isOpen()) {

			// Clean
			display.clear();

			// Update stuff
			Camera.update();

			EntityManager.update();
			BlockPlacement.update();
			HotBar.update();

			// Draw 3D
			EntityManager.render();
			World.draw();
			BlockPlacement.drawBoundingBox();

			// Draw 2D
			RenderManager.disableDepth();

			cursor.draw();
			HotBar.render();

			RenderManager.enableDepth();

			display.update();
		}

		connectionThread.interrupt();
		connection.close();
	}
}
