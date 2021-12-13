package com.blackoutburst.network.client;

import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class C00SendMovement extends PacketPlayOut implements PacketUtils {

	private final int id;
	private final Vector3f position;

	public C00SendMovement(int id, Vector3f position) {
		this.ID = 0x00;
		this.id = id;
		this.position = position;
	}
	
	@Override
	public C00SendMovement writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(id);
		buffer.writeFloat(position.x);
		buffer.writeFloat(position.y);
		buffer.writeFloat(position.z);
		return (this);
	}

	@Override
	public void sendPacket() {
		send(this.buffer);
	}

}
