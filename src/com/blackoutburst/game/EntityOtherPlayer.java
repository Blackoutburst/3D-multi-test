package com.blackoutburst.game;

import com.blackoutburst.bogel.graphics.Color;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;

public class EntityOtherPlayer extends Entity {


	public EntityOtherPlayer(int id, EntityTypes type, Vector3f position, Vector3f scale, Vector3f rotation) {
		super(id, type, position, scale, rotation);
	}

	@Override
	protected void update() {}

	@Override
	protected void render() {
		new Cube(Textures.MUSH, position, scale, rotation, Color.WHITE, new Vector2f(), false).draw();
	}
}
