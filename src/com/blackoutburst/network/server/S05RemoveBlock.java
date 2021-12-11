package com.blackout.network.server;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayIn;
import com.blackout.network.PacketUtils;
import com.blackoutburst.game.Main;

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
