package blackout.network.server;

import blackout.network.Client;
import blackout.network.Connection;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayOut;
import blackout.network.PacketUtils;

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
