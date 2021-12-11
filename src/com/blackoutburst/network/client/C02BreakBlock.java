package com.blackout.network.client;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayOut;
import com.blackout.network.PacketUtils;

public class C02BreakBlock extends PacketPlayOut implements PacketUtils {

	private int id;

	public C02BreakBlock(int id) {
		this.ID = 0x02;
		this.id = id;
	}
	
	@Override
	public C02BreakBlock writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(id);
		return (this);
	}

	@Override
	public void sendPacket() {
		send(this.buffer);
	}

}
