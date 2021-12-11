package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

public interface EntityNetworkController {

	default void addEntity(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		if (EntityManager.myId == id) return;
		for (Entity e : EntityManager.entities) {
			if (id == e.getId()) return;
		}

		switch(type) {
			case PLAYER: EntityManager.entities.add(new EntityOtherPlayer(id, type, position, scale, rotation));
		}
	}
	
	default void moveEntity(int id, Vector3f position) {
		for (Entity e : EntityManager.entities) {
			if (e.id == id && id != EntityManager.myId) {
				e.position = position;
				break;
			}
		}
	}
	
	default void updateRotation(int id, Vector3f rotation) {
		for (Entity e : EntityManager.entities) {
			if (e.id == id && id != EntityManager.myId) {
				e.rotation = rotation;
				break;
			}
		}
	}
	
	default void deleteEntity(int id) {
		Entity rm = null;
		
		for (Entity e : EntityManager.entities) {
			if (e.id == id) {
				rm = e;
				break;
			}
		}
		
		if (rm != null)
			EntityManager.entities.remove(rm);
	}
}
