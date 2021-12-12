package com.blackoutburst.network.client;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.entity.EntityManager;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class C00SendMovement extends PacketPlayIn implements PacketUtils {

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
