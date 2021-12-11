package blackout.network.server;

import blackout.core.Vector3f;
import blackout.network.Client;
import blackout.network.Connection;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayOut;
import blackout.network.PacketUtils;

public class S02UpdateEntityRotation extends PacketPlayOut implements PacketUtils {

	private int id;
	private Vector3f rotation;
	
	public S02UpdateEntityRotation(int id, Vector3f rotation) {
		this.ID = 0x02;
		this.id = id;
		this.rotation = rotation;
	}
	
	@Override
	public S02UpdateEntityRotation writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
		buffer.writeFloat(this.rotation.x);
		buffer.writeFloat(this.rotation.y);
		buffer.writeFloat(this.rotation.z);
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
