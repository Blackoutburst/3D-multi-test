package com.blackoutburst.network.server;

import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.EntityManager;
import com.blackoutburst.game.EntityTypes;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S00AddEntity extends PacketPlayIn implements PacketUtils {

	private int id;
	private int type;
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	
	@Override
	public void readPacketData(String data) {
		try {

			position = new Vector3f();
			scale = new Vector3f();
			rotation = new Vector3f();

			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			type = buffer.readInt();
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
