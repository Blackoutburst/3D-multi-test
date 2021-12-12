package com.blackoutburst.network.server;

import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.EntityManager;
import com.blackoutburst.game.EntityTypes;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S00AddEntity extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			Vector3f position = new Vector3f();
			Vector3f scale = new Vector3f();
			Vector3f rotation = new Vector3f();

			PacketBuffer buffer = new PacketBuffer(data);

			int id = buffer.readInt();
			int type = buffer.readInt();
			position.x =  buffer.readFloat();
			position.y =  buffer.readFloat();
			position.z =  buffer.readFloat();
			scale.x =  buffer.readFloat();
			scale.y =  buffer.readFloat();
			scale.z =  buffer.readFloat();
			rotation.x =  buffer.readFloat();
			rotation.y =  buffer.readFloat();
			rotation.z =  buffer.readFloat();

			EntityTypes tp = null;
			
			switch (type) {
				case 0x00: tp = EntityTypes.PLAYER; break;
			}
			
			new EntityManager().addEntity(id, tp, position, scale, rotation);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
