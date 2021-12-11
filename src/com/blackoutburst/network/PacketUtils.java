package com.blackout.network;


import com.blackout.network.server.*;
import com.blackoutburst.game.Main;

public interface PacketUtils {

	public static final char SEPARATOR = 0x1F;
	public static final char END = 0x0A;
	
	default void malformatedError(String e) {
		System.out.println("Malformated packet unable to read it: " + e);
	}
	
	default void send(PacketBuffer buffer) {
		Main.connection.write(buffer.getData());
	}
	
	default void sort(char type, String data) {
		switch (type) {
			case 0x00: new S00AddEntity().readPacketData(data); break;
			case 0x01: new S01MoveEntity().readPacketData(data); break;
			case 0x02: new S02UpdateEntityRotation().readPacketData(data); break;
			case 0x03: new S03DeleteEntity().readPacketData(data); break;
			case 0x04: new S04AddBlock().readPacketData(data); break;
			case 0x05: new S05RemoveBlock().readPacketData(data); break;
		}
	}
	
}
