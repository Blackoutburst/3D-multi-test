package com.blackoutburst.core;

import java.util.ArrayList;
import java.util.List;

import com.blackoutburst.network.Connection;

public class Core {

	public static List<Cube> cubes = new ArrayList<Cube>();
	
	public static boolean running = true;
	public static Connection connection = null;
	
	public void init() {
		
		for (int x = -10; x <= 10; x++) {
			for (int y = 0; y >= -10; y--) {
				for (int z = -10; z <= 10; z++) {
					cubes.add(new Cube(y == 0 ? "GRASS" : "COBBLESTONE", new Vector3f(x, y, z), new Vector3f(1), new Vector3f(), y == 0 ? new Color(0.09f, 0.27f, 0.06f) : Color.WHITE));
				}
			}
		}
		
		connection = new Connection();
		connection.startServer();
		update();
	}
	
	private void update() {
		while (running) {
			connection.acceptClient();
		}
	}
}
