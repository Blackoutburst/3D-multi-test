package com.blackoutburst.game;

public enum EntityTypes {
	PLAYER(0x00);

	private final int I;
	
	EntityTypes(int i) {
		this.I = i;
	}
	
	public int getType() {
		return (I);
	}
}
