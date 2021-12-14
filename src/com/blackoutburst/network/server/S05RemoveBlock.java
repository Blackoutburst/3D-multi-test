package com.blackoutburst.network.server;

import com.blackoutburst.bogel.maths.Vector3f;
import com.blackoutburst.game.Cube;
import com.blackoutburst.game.Main;
import com.blackoutburst.game.World;
import com.blackoutburst.network.PacketBuffer;
import com.blackoutburst.network.PacketPlayIn;
import com.blackoutburst.network.PacketUtils;

public class S05RemoveBlock extends PacketPlayIn implements PacketUtils {

	@Override
	public void readPacketData(String data) {
		try {
			final PacketBuffer buffer = new PacketBuffer(data);

			final int id = buffer.readInt();

			World.drawCubes.remove(Main.toIntVector(World.cubes.get(id).getPosition()));
			World.cubes.remove(id);

			for (Cube c : World.cubes) {

				Vector3f top = new Vector3f(c.getPosition().x, c.getPosition().y + 1, c.getPosition().z);
				Vector3f bottom = new Vector3f(c.getPosition().x, c.getPosition().y - 1, c.getPosition().z);

				Vector3f back = new Vector3f(c.getPosition().x, c.getPosition().y, c.getPosition().z + 1);
				Vector3f front = new Vector3f(c.getPosition().x, c.getPosition().y, c.getPosition().z - 1);

				Vector3f right = new Vector3f(c.getPosition().x + 1, c.getPosition().y, c.getPosition().z);
				Vector3f left = new Vector3f(c.getPosition().x - 1, c.getPosition().y, c.getPosition().z);

				boolean draw = true;

				if (World.drawCubes.get(Main.toIntVector(top)) != null && World.drawCubes.get(Main.toIntVector(bottom)) != null &&
						World.drawCubes.get(Main.toIntVector(back)) != null && World.drawCubes.get(Main.toIntVector(front)) != null &&
						World.drawCubes.get(Main.toIntVector(right)) != null && World.drawCubes.get(Main.toIntVector(left)) != null) {
					draw = false;
				}
				World.drawCubes.put(Main.toIntVector(c.getPosition()), draw);
			}

		} catch(Exception e) {
			malformatedError(e.toString());
		}
	}
}
