package com.blackoutburst.network.client;

import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayOut;
import com.blackoutburst.network.PacketUtils;

public class C03PlaceBlock extends PacketPlayOut implements PacketUtils {

	private String texture;
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	private Color color;

	public C03PlaceBlock(String texture, Vector3f position, Vector3f scale, Vector3f rotation, Color color) {
		this.ID = 0x03;
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.color = color;
	}
	
	@Override
	public C03PlaceBlock writePacketData() {
		buffer = new PacketBuffer();
		buffer.writeChar(this.ID);
		buffer.writeString(this.texture);
		buffer.writeFloat(this.position.x);
		buffer.writeFloat(this.position.y);
		buffer.writeFloat(this.position.z);
		buffer.writeFloat(this.scale.x);
		buffer.writeFloat(this.scale.y);
		buffer.writeFloat(this.scale.z);
		buffer.writeFloat(this.rotation.x);
		buffer.writeFloat(this.rotation.y);
		buffer.writeFloat(this.rotation.z);
		buffer.writeFloat(this.color.r);
		buffer.writeFloat(this.color.g);
		buffer.writeFloat(this.color.b);
		return (this);
	}

	@Override
	public void sendPacket() {
		send(this.buffer);
	}

}
