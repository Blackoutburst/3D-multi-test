package com.blackoutburst.network;

import com.blackoutburst.game.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements PacketUtils {

	private final String IP = "localhost";
	private final short PORT = 25565;
	
	public Socket socket = null;
	public BufferedReader in = null;
	public OutputStream out = null;
	
	public void connect() {
		try {
			socket = new Socket(IP, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			out = socket.getOutputStream();
			EntityManager.init(Integer.valueOf(in.readLine()));
			System.out.println("Connected");
		} catch (Exception e) {
			System.err.println("Connection error :"+e.toString());
		}
	}
	
	public void write(String data) {
		if (out == null) return;
		
		Thread connectionThread = new Thread(new Runnable(){
			public synchronized void run(){
				try {
					out.write(data.getBytes("UTF-8"));
				} catch (Exception e) {
					System.err.println("Error while writing data :"+e.toString());
				}	
			}
		});
		connectionThread.setDaemon(true);
		connectionThread.setName("Connection output thread");
		connectionThread.start();
	}
	
	public void read() {
		if (in == null) return;
		
		try {
			String str = in.readLine();
			if (str == null) {
				close();
				return;
			}
			
			sort(str.charAt(0), str);
		} catch (Exception e) {
			close();
			System.err.println("Error while reading data :"+e.toString());
		}			
	}
	
	public void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Error while closing the connection :"+e.toString());
		}
	}
	
}
