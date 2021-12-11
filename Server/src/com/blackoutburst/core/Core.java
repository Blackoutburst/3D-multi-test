package blackout.core;

import java.util.ArrayList;
import java.util.List;

import blackout.network.Connection;

public class Core {

	public static List<Cube> cubes = new ArrayList<Cube>();
	
	public static boolean running = true;
	public static Connection connection = null;
	
	public void init() {
		
		for (int x = -10; x < 10; x++) {
			for (int z = -10; z < 10; z++) {
				cubes.add(new Cube("GRASS", new Vector3f(x, 0, z), new Vector3f(1), new Vector3f(), new Color(0.09f, 0.27f, 0.06f)));
			}
		}
		cubes.add(new Cube("BRICKS", new Vector3f(5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube("BRICKS", new Vector3f(-5, 1, 5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube("BRICKS", new Vector3f(5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));
		cubes.add(new Cube("BRICKS", new Vector3f(-5, 1, -5), new Vector3f(1), new Vector3f(), Color.WHITE));
		
		connection = new Connection();
		connection.startServer();
		update();
	}
	
	private void update() {
		while (running) {
			connection.acceptClient();
		}
		clean();
	}
	
	private void clean() {
		
	}
}
