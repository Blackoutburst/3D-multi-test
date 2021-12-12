package com.blackoutburst.network.client;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.entity.EntityManager;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class C01SendRotation extends PacketPlayIn implements PacketUtils {

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
