package com.blackoutburst.network;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	public Socket socket;
	public BufferedReader in;
	public OutputStream out;
	public int entityId;
	
	public Client(Socket socket, BufferedReader in, OutputStream out, int entityId) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.entityId = entityId;
	}
	
}
