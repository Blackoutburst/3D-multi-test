package com.blackoutburst.network.server;

import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.*;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

import java.util.ArrayList;
import java.util.List;

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

			final Vector2f offset = new Vector2f();
			final boolean transparent;

			switch (texture) {
				case "GRASS": offset.set(5, 0); transparent = false; break;
				case "STONEBRICKS": offset.set(0, 0); transparent = false; break;
				case "BRICKS": offset.set(1, 1); transparent = false; break;
				case "COBBLESTONE": offset.set(0, 1); transparent = false; break;
				case "DIAMOND_BLOCK": offset.set(7, 0); transparent = false; break;
				case "GLASS": offset.set(6, 0); transparent = true; break;
				case "LOG_OAK": offset.set(4, 0); transparent = false; break;
				case "NOTEBLOCK": offset.set(2, 0); transparent = false; break;
				case "PLANKS_OAK": offset.set(1, 0); transparent = false; break;
				default: offset.set(7, 7); transparent = false; break;
			}

			World.cubes.add(new Cube(null, position, scale, rotation, color, offset, transparent));
			World.drawCubes.put(Main.toIntVector(position), true);

			for (Cube c : World.cubes) {

				Vector3f top = new Vector3f(c.getPosition().x, c.getPosition().y + 1, c.getPosition().z);
				Vector3f bottom = new Vector3f(c.getPosition().x, c.getPosition().y - 1, c.getPosition().z);

				Vector3f back = new Vector3f(c.getPosition().x, c.getPosition().y, c.getPosition().z + 1);
				Vector3f front = new Vector3f(c.getPosition().x, c.getPosition().y, c.getPosition().z - 1);

				Vector3f right = new Vector3f(c.getPosition().x + 1, c.getPosition().y, c.getPosition().z);
				Vector3f left = new Vector3f(c.getPosition().x - 1, c.getPosition().y, c.getPosition().z);

				boolean draw = World.drawCubes.get(Main.toIntVector(top)) == null || World.drawCubes.get(Main.toIntVector(bottom)) == null ||
						World.drawCubes.get(Main.toIntVector(back)) == null || World.drawCubes.get(Main.toIntVector(front)) == null ||
						World.drawCubes.get(Main.toIntVector(right)) == null || World.drawCubes.get(Main.toIntVector(left)) == null;

				World.drawCubes.put(Main.toIntVector(c.getPosition()), draw);
			}

			List<Cube> glass = new ArrayList<>();
			List<Cube> tmp = new ArrayList<>();

			int j = World.cubes.size();
			for (int i = 0; i < j; i++) {
				Cube c = World.cubes.get(i);
				if (c.isTransparent()) {
					c.setDistance(Math.pow((Camera.position.x - c.getPosition().x), 2) +
							Math.pow((Camera.position.y - c.getPosition().y), 2) +
							Math.pow((Camera.position.z - c.getPosition().z), 2));
					glass.add(c);
					continue;
				}

				if (World.drawCubes.get(Main.toIntVector(c.getPosition())) == null || !World.drawCubes.get(Main.toIntVector(c.getPosition()))) {
					continue;
				}
				tmp.add(c);
			}
			glass.sort(new DistanceComparator());
			tmp.addAll(glass);
			glass.clear();

			World.toDraw = new ArrayList<>(tmp);
		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
