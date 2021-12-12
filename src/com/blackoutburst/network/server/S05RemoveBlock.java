package com.blackoutburst.network.server;

import com.blackoutburst.game.Main;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S05RemoveBlock extends PacketPlayIn implements PacketUtils {

	private int id;

	@Override
	public void readPacketData(String data) {
		try {
			PacketBuffer buffer = new PacketBuffer(data);

			this.id = buffer.readInt();
			Main.cubes.remove(this.id);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
