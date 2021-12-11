package com.blackout.network;

public abstract class PacketPlayOut {

	protected char ID;
	protected PacketBuffer buffer;
	
	public abstract PacketPlayOut writePacketData();
	public abstract void sendPacket();
}
