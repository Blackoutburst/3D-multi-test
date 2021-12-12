package com.blackoutburst.network.server;

import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.EntityManager;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S01MoveEntity extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			Vector3f position = new Vector3f();

			PacketBuffer buffer = new PacketBuffer(data);

			int id = buffer.readInt();
			position.x = buffer.readFloat();
			position.y = buffer.readFloat();
			position.z = buffer.readFloat();

			new EntityManager().moveEntity(id, position);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
