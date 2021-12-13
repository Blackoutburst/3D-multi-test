package com.blackoutburst.network.server;

import com.blackoutburst.game.World;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S05RemoveBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			final int id = buffer.readInt();
			World.cubes.remove(id);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
