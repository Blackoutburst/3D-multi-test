package com.blackoutburst.network.client;


import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class C02BreakBlock extends PacketPlayOut implements PacketUtils {

	private final int id;

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
