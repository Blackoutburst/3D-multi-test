package com.blackoutburst.network.server;

import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.Cube;
import com.blackoutburst.game.Main;
import com.blackoutburst.game.Textures;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S04AddBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			Vector3f position = new Vector3f();
			Vector3f scale = new Vector3f();
			Vector3f rotation = new Vector3f();
			Color color = new Color();

			PacketBuffer buffer = new PacketBuffer(data);

			String texture = buffer.readString();
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

			Texture text;

			switch (texture) {
				case "GRASS": text = Textures.GRASS; break;
				case "STONEBRICKS": text = Textures.STONEBRICKS; break;
				case "BRICKS": text = Textures.BRICKS; break;
				case "COBBLESTONE": text = Textures.COBBLESTONE; break;
				case "DIAMOND_BLOCK": text = Textures.DIAMOND_BLOCK; break;
				case "GLASS": text = Textures.GLASS; break;
				case "LOG_OAK": text = Textures.LOG_OAK; break;
				case "NOTEBLOCK": text = Textures.NOTEBLOCK; break;
				case "PLANKS_OAK": text = Textures.PLANKS_OAK; break;
				default: return;
			}

			Main.cubes.add(new Cube(text, position, scale, rotation, color));
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
