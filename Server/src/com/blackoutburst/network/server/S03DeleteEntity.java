package blackout.network.server;

import blackout.entity.EntityManager;
import blackout.network.Client;
import blackout.network.Connection;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayOut;
import blackout.network.PacketUtils;

public class S03DeleteEntity extends PacketPlayOut implements PacketUtils {

	private int id;
	
	public S03DeleteEntity(int id) {
		this.ID = 0x03;
		this.id = id;
	}
	
	@Override
	public S03DeleteEntity writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(id);
		
		new EntityManager().deleteEntity(id);
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
