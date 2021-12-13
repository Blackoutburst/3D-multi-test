package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EntityManager implements EntityNetworkController {
	
	public final static List<Entity> ENTITIES = new ArrayList<>();
	public static int myId;
	
	public static void init(int id) {
		myId = id;

		ENTITIES.add(new EntityPlayer(id, EntityTypes.PLAYER, new Vector3f(0, 2.80f, 0), new Vector3f(1), new Vector3f(0)));
	}
	
	public static void update() {
		for (Entity e : ENTITIES) {
			e.update();
		}
	}
	
	public static void render() {
		for (Entity e : ENTITIES) {
			e.render();
		}
	}
}
