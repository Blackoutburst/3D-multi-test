package com.blackout.network.server;

import com.blackout.network.PacketBuffer;
import com.blackout.network.PacketPlayIn;
import com.blackout.network.PacketUtils;
import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.Cube;
import com.blackoutburst.game.Main;
import com.blackoutburst.game.Textures;

public class S04AddBlock extends PacketPlayIn implements PacketUtils {

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

			Texture text = null;
			
			switch (texture) {
				case "GRASS": text = Textures.GRASS; break;
				case "BRICKS": text = Textures.BRICKS; break;
			}
			Main.cubes.add(new Cube(text, position, scale, rotation, color));
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
