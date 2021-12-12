package com.blackoutburst.network;

import com.blackoutburst.core.Core;
import com.blackoutburst.network.client.C00SendMovement;
import com.blackoutburst.network.client.C01SendRotation;
import com.blackoutburst.network.client.C02BreakBlock;
import com.blackoutburst.network.client.C03PlaceBlock;

public interface PacketUtils {

	public static final char SEPARATOR = 0x1F;
	public static final char END = 0x0A;
	
	default void malformatedError(String e) {
		System.out.println("Malformated packet unable to read it: " + e);
	}
	
	default void send(PacketBuffer buffer, Client c) {
		Core.connection.write(buffer.getData(), c);
	}
	
	default void sort(char type, String data) {
		switch (type) {
			case 0x00: new C00SendMovement().readPacketData(data); break;
			case 0x01: new C01SendRotation().readPacketData(data); break;
			case 0x02: new C02BreakBlock().readPacketData(data); break;
			case 0x03: new C03PlaceBlock().readPacketData(data); break;
			default: return;
		}
	}
	
}
