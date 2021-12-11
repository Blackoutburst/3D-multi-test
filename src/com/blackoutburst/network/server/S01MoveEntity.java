package com.blackout.network.server;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayIn;
import com.blackout.network.PacketUtils;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.EntityManager;

public class S01MoveEntity extends PacketPlayIn implements PacketUtils {

	private int id;
	private Vector3f position;

	@Override
	public void readPacketData(String data) {
		try {
			position = new Vector3f();

			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			position.x = buffer.readFloat();
			position.y = buffer.readFloat();
			position.z = buffer.readFloat();

			new EntityManager().moveEntity(id, position);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
