package com.blackoutburst.network.server;

import com.blackoutburst.game.Main;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S05RemoveBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			PacketBuffer buffer = new PacketBuffer(data);

			int id = buffer.readInt();
			Main.cubes.remove(id);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
