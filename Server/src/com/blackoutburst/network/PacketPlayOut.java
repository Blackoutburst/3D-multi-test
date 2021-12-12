package com.blackoutburst.network;

public abstract class PacketPlayOut {

	protected char ID;
	protected PacketBuffer buffer;
	
	public abstract PacketPlayOut writePacketData();
	public abstract void sendPacket(Client c);
	public abstract void sendPacketToAll();
}
