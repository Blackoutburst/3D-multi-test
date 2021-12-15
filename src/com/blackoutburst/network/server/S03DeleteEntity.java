package com.blackoutburst.network.server;

import com.blackoutburst.game.EntityManager;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S03DeleteEntity extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			final int id = buffer.readInt();
			
			new EntityManager().deleteEntity(id);
		} catch(Exception e) {
			malformedError(e.toString());
		}
	}
}
