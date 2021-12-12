package com.blackoutburst.network.server;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.network.Client;
import com.blackoutburst.network.Connection;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class S02UpdateEntityRotation extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f rotation;
	
	public S02UpdateEntityRotation(int id, Vector3f rotation) {
		this.ID = 0x02;
		this.id = id;
		this.rotation = rotation;
	}
	
	@Override
	public S02UpdateEntityRotation writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
		buffer.writeFloat(this.rotation.x);
		buffer.writeFloat(this.rotation.y);
		buffer.writeFloat(this.rotation.z);
		return (this);
	}
	
	@Override
	public void sendPacketToAll() {
		for (Client c : Connection.clients) {
			send(this.buffer, c);
		}
	}

	@Override
	public void sendPacket(Client c) {
		send(this.buffer, c);
	}
}
