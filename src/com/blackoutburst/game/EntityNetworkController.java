package com.blackoutburst.game;

import com.blackoutburst.bogel.maths.Vector3f;

public interface EntityNetworkController {

	default void addEntity(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		if (EntityManager.myId == id) return;
		for (Entity e : EntityManager.ENTITIES) {
			if (id == e.getId()) return;
		}

		switch(type) {
			case PLAYER: EntityManager.ENTITIES.add(new EntityOtherPlayer(id, type, position, scale, rotation)); break;
			default: break;
		}
	}
	
	default void moveEntity(int id, Vector3f position) {
		for (Entity e : EntityManager.ENTITIES) {
			if (e.id == id && id != EntityManager.myId) {
				e.newPosition = position.copy();

				Vector3f velocity = new Vector3f();
				velocity.x = e.newPosition.x - e.lastPosition.x;
				velocity.y = e.newPosition.y - e.lastPosition.y;
				velocity.z = e.newPosition.z - e.lastPosition.z;

				e.lastPosition = e.newPosition.copy();

				e.position.x += velocity.x;
				e.position.y += velocity.y;
				e.position.z += velocity.z;
				break;
			}
		}
	}
	
	default void updateRotation(int id, Vector3f rotation) {
		for (Entity e : EntityManager.ENTITIES) {
			if (e.id == id && id != EntityManager.myId) {
				e.rotation = rotation;
				break;
			}
		}
	}
	
	default void deleteEntity(int id) {
		Entity rm = null;
		
		for (Entity e : EntityManager.ENTITIES) {
			if (e.id == id) {
				rm = e;
				break;
			}
		}
		
		if (rm != null)
			EntityManager.ENTITIES.remove(rm);
	}
}
