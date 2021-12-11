package blackout.network.server;

import blackout.core.Vector3f;
import blackout.network.Client;
import blackout.network.Connection;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayOut;
import blackout.network.PacketUtils;

public class S01MoveEntity extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f position;
	
	public S01MoveEntity(int id, Vector3f position) {
		this.ID = 0x01;
		this.id = id;
		this.position = position;
	}
	
	@Override
	public S01MoveEntity writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
		buffer.writeFloat(this.position.x);
		buffer.writeFloat(this.position.y);
		buffer.writeFloat(this.position.z);
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
