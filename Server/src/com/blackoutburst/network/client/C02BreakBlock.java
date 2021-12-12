package com.blackoutburst.network.client;

import com.blackoutburst.core.Core;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;
import com.blackoutburst.network.server.S05RemoveBlock;

public class C02BreakBlock extends PacketPlayIn implements PacketUtils {

	private int id;
	
	@Override
	public void readPacketData(String data) {
		try {
			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			
			Core.cubes.remove(this.id);
			
			new S05RemoveBlock(this.id).writePacketData().sendPacketToAll();
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}