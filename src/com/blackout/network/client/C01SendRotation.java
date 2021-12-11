package com.blackout.network.client;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayOut;
import com.blackout.network.PacketUtils;
import com.blackoutburst.bogel.maths.Vector3f;

public class C01SendRotation extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f rotation;
	
	public C01SendRotation(int id, Vector3f rotation) {
		this.ID = 0x01;
		this.id = id;
		this.rotation = rotation;
	}
	
	@Override
	public C01SendRotation writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(id);
		buffer.writeFloat(rotation.x);
		buffer.writeFloat(rotation.y);
		buffer.writeFloat(rotation.z);
		return (this);
	}

	@Override
	public void sendPacket() {
		send(this.buffer);
	}

}
