package com.blackoutburst.network.server;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.network.Client;
import com.blackoutburst.network.Connection;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class S01MoveEntity extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f position;
	
	public S01MoveEntity(int id, Vector3f position) {
		this.ID = 0x01;
		this.id = id;
		this.position = position;
	}
	
	@Override
	public S01MoveEntity writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
		buffer.writeFloat(this.position.x);
		buffer.writeFloat(this.position.y);
		buffer.writeFloat(this.position.z);
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
