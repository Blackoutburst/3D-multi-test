package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityManager implements EntityNetworkController {
	
	public static List<Entity> entities = new ArrayList<Entity>();
	public static int myId;
	
	public static void init(int id) {
		myId = id;

		entities.add(new EntityPlayer(id, EntityTypes.PLAYER, new Vector3f(0, 2.80f, 0), new Vector3f(1), new Vector3f(0)));
	}
	
	public static void update() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
		}
	}
	
	public static void render() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render();
		}
	}
}
