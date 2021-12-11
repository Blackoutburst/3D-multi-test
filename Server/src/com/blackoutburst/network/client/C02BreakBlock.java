package blackout.network.client;

import blackout.core.Core;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayIn;
import blackout.network.PacketUtils;
import blackout.network.server.S05RemoveBlock;

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
