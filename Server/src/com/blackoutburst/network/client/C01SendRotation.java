package blackout.network.client;

import blackout.core.Vector3f;
import blackout.entity.EntityManager;
import blackout.network.PacketBuffer;
import blackout.network.PacketPlayIn;
import blackout.network.PacketUtils;

public class C01SendRotation extends PacketPlayIn implements PacketUtils {

	private int id;
	private Vector3f rotation;
	
	@Override
	public void readPacketData(String data) {
		try {
			rotation = new Vector3f();
			
			PacketBuffer buffer = new PacketBuffer(data);

			id = buffer.readInt();
			rotation.x = buffer.readFloat();
			rotation.y = buffer.readFloat();
			rotation.z = buffer.readFloat();
			
			new EntityManager().updateRotation(id, rotation);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
