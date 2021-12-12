package com.blackoutburst.network.client;

import com.blackoutburst.core.Color;
import com.blackoutburst.core.Core;
import com.blackoutburst.core.Cube;
import com.blackoutburst.core.Vector3f;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;
import com.blackoutburst.network.server.S04AddBlock;

public class C03PlaceBlock extends PacketPlayIn implements PacketUtils {

	private String texture;
	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	private Color color;
	
	@Override
	public void readPacketData(String data) {
		try {
			position = new Vector3f();
			scale = new Vector3f();
			rotation = new Vector3f();
			color = new Color();
			
			PacketBuffer buffer = new PacketBuffer(data);

			texture = buffer.readString();
			position.x =  buffer.readFloat();
			position.y =  buffer.readFloat();
			position.z =  buffer.readFloat();
			scale.x =  buffer.readFloat();
			scale.y =  buffer.readFloat();
			scale.z =  buffer.readFloat();
			rotation.x =  buffer.readFloat();
			rotation.y =  buffer.readFloat();
			rotation.z =  buffer.readFloat();
			color.r = buffer.readFloat();
			color.g = buffer.readFloat();
			color.b = buffer.readFloat();
			
			Core.cubes.add(new Cube(texture, position, scale, rotation, color));
			
			new S04AddBlock(texture, position, scale, rotation, color).writePacketData().sendPacketToAll();
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
