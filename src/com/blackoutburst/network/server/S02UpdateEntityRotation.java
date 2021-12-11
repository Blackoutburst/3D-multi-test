package com.blackout.network.server;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayIn;
import com.blackout.network.PacketUtils;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.EntityManager;

public class S02UpdateEntityRotation extends PacketPlayIn implements PacketUtils {

	private int id;
	private Vector3f rotation;
	
	@Override
	public void readPacketData(String data) {
		try {
			rotation = new Vector3f();

			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			rotation.x = buffer.readFloat();
			rotation.y = buffer.readFloat();
			rotation.z = buffer.readFloat();
			
			new EntityManager().updateRotation(id, rotation);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
