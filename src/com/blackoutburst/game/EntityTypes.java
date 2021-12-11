package com.blackoutburst.game;

public enum EntityTypes {
	PLAYER(0x00);

	private int i;
	
	EntityTypes(int i) {
		this.i = i;
	}
	
	public int getType() {
		return (i);
	}
}
