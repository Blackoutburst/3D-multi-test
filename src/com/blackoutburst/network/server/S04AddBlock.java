package com.blackoutburst.network.server;

import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.graphics.Texture;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.Cube;
import com.blackoutburst.game.Textures;
import com.blackoutburst.game.World;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S04AddBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			final Vector3f position = new Vector3f();
			final Vector3f scale = new Vector3f();
			final Vector3f rotation = new Vector3f();
			final Color color = new Color();

			final PacketBuffer buffer = new PacketBuffer(data);

			final String texture = buffer.readString();
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

			final Texture text;
			final Vector2f offset = new Vector2f();
			final boolean transparent;

			switch (texture) {
				case "GRASS": text = Textures.GRASS; offset.set(5, 0); transparent = false; break;
				case "STONEBRICKS": text = Textures.STONEBRICKS; offset.set(0, 0); transparent = false; break;
				case "BRICKS": text = Textures.BRICKS; offset.set(1, 1); transparent = false; break;
				case "COBBLESTONE": text = Textures.COBBLESTONE; offset.set(0, 1); transparent = false; break;
				case "DIAMOND_BLOCK": text = Textures.DIAMOND_BLOCK; offset.set(7, 0); transparent = false; break;
				case "GLASS": text = Textures.GLASS; offset.set(6, 0); transparent = true; break;
				case "LOG_OAK": text = Textures.LOG_OAK; offset.set(4, 0); transparent = false; break;
				case "NOTEBLOCK": text = Textures.NOTEBLOCK; offset.set(2, 0); transparent = false; break;
				case "PLANKS_OAK": text = Textures.PLANKS_OAK; offset.set(1, 0); transparent = false; break;
				default: return;
			}

			World.cubes.add(new Cube(text, position, scale, rotation, color, offset, transparent));
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
