package com.blackoutburst.network.client;

import com.blackoutburst.core.Core;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;
import com.blackoutburst.network.server.S05RemoveBlock;

public class C02BreakBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			PacketBuffer buffer = new PacketBuffer(data);

			int id = buffer.readInt();
			
			Core.cubes.remove(id);
			
			new S05RemoveBlock(id).writePacketData().sendPacketToAll();
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
