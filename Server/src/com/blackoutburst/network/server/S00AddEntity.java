package com.blackoutburst.network.server;

import com.blackoutburst.core.Vector3f;
import com.blackoutburst.entity.EntityManager;
import com.blackoutburst.entity.EntityTypes;
import com.blackoutburst.network.Client;
import com.blackoutburst.network.Connection;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class S00AddEntity extends PacketPlayOut implements PacketUtils {

	private int id;
	private int type;
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	
	public S00AddEntity() {
		this.ID = 0x00;
		this.type = EntityTypes.PLAYER.getType();
		this.id = EntityManager.entityId++;
		this.position = new Vector3f(0, 2.80f, 0);
		this.scale = new Vector3f(1);
		this.rotation = new Vector3f(0);
	}
	
	public S00AddEntity(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		this.ID = 0x00;
		this.id = id;
		this.type = type.getType();
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	@Override
	public S00AddEntity writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeInt(this.id);
		buffer.writeInt(this.type);
		buffer.writeFloat(this.position.x);
		buffer.writeFloat(this.position.y);
		buffer.writeFloat(this.position.z);
		buffer.writeFloat(this.scale.x);
		buffer.writeFloat(this.scale.y);
		buffer.writeFloat(this.scale.z);
		buffer.writeFloat(this.rotation.x);
		buffer.writeFloat(this.rotation.y);
		buffer.writeFloat(this.rotation.z);
		
		EntityTypes tp = null;
		
		switch (type) {
			case 0x00: tp = EntityTypes.PLAYER; break;
		}
		
		new EntityManager().addEntity(id, tp, position, scale, rotation);
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
