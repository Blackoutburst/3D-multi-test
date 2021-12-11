package blackout.entity;

public enum EntityTypes {
	PLAYER(0x00);

	private final int i;
	
	EntityTypes(int i) {
		this.i = i;
	}
	
	public final int getType() {
		return (i);
	}
}
