package com.blackoutburst.entity;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.network.server.S01MoveEntity;
import com.blackoutburst.network.server.S02UpdateEntityRotation;

public interface EntityNetworkController {

	default void addEntity(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		for (Entity e : EntityManager.entities) {
			if (id == e.getId()) return;
		}
		switch(type) {
			case PLAYER: EntityManager.entities.add(new EntityPlayer(id, type, position, scale, rotation)); break;
			default: return;
		}
	}
	
	default void moveEntity(int id, Vector3f position) {
		for (Entity e : EntityManager.entities) {
			if (e.id == id) {
				e.position = position;
				new S01MoveEntity(e.id, position).writePacketData().sendPacketToAll();
				break;
			}
		}
	}
	
	default void updateRotation(int id, Vector3f rotation) {
		for (Entity e : EntityManager.entities) {
			if (e.id == id) {
				e.rotation = rotation;
				new S02UpdateEntityRotation(e.id, e.rotation).writePacketData().sendPacketToAll();
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
