package com.blackout.network.server;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayIn;
import com.blackout.network.PacketUtils;
import com.blackoutburst.game.EntityManager;

public class S03DeleteEntity extends PacketPlayIn implements PacketUtils {

	private int id;
	
	@Override
	public void readPacketData(String data) {
		try {
			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			
			new EntityManager().deleteEntity(id);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
