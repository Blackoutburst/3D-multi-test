package com.blackoutburst.network.server;

import com.blackoutburst.network.Client;
import com.blackoutburst.network.Connection;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class S05RemoveBlock extends PacketPlayOut implements PacketUtils {

	private int id;
	
	public S05RemoveBlock(int id) {
		this.ID = 0x05;
		this.id = id;
	}
	
	@Override
	public S05RemoveBlock writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
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
