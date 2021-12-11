package com.blackout.network.client;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayOut;
import com.blackout.network.PacketUtils;
import com.blackoutburst.bogel.maths.Vector3f;

public class C00SendMovement extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f position;

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
