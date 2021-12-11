package blackout.entity;

import blackout.core.Vector3f;

public class EntityPlayer extends Entity {

	public EntityPlayer(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		super(id, type, position, scale, rotation);
		type = EntityTypes.PLAYER;
	}
}
