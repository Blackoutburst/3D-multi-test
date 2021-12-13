package com.blackoutburst.game;


import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.client.C00SendMovement;
import com.blackoutburst.network.client.C01SendRotation;

public class EntityPlayer extends Entity {

	public EntityPlayer(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		super(id, type, position, scale, rotation);
	}

	@Override
	protected void update() {
		C00SendMovement movement = new C00SendMovement(id, Camera.position).writePacketData();
		C01SendRotation rotation = new C01SendRotation(id, new Vector3f(0, (float)Math.toRadians(-Camera.rotation.x), 0)).writePacketData();

		movement.sendPacket();
		rotation.sendPacket();

		movement = null;
		rotation = null;
	}

	@Override
	protected void render() {}
}
