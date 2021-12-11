package com.blackoutburst.game;

import com.blackoutburst.bogel.core.Display;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector3f;

public class Main {

	public static Matrix4f projection;

	public static void main(String[] args) {
		Display display = new Display().create();

		projection = new Matrix4f();
		projection.setIdentity();
		Matrix4f.projectionMatrix(70, 1000, 0.01f, projection);


		Camera.init();
		Cube cube = new Cube(new Texture("icon128.png"), new Vector3f(1), new Vector3f(1), new Vector3f(1));

		while(display.isOpen()) {
			display.clear();

			cube.draw();

			display.update();
		}
	}
}
