package blackout.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import blackout.core.Core;
import blackout.core.Cube;
import blackout.entity.Entity;
import blackout.entity.EntityManager;
import blackout.entity.EntityTypes;
import blackout.network.server.S00AddEntity;
import blackout.network.server.S03DeleteEntity;
import blackout.network.server.S04AddBlock;

public class Connection implements PacketUtils {

	public static List<Client> clients = new ArrayList<Client>();
	
	private final int PORT = 25565;
	private ServerSocket server;
	
	public void startServer() {
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server running on port : "+PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void acceptClient() {
		try {
			Socket socket = server.accept();
	  		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
	  		OutputStream out = socket.getOutputStream();
	  		Client client = new Client(socket, in, out, EntityManager.entityId);
	  		
	  		
	  		String str = String.valueOf(EntityManager.entityId)+"\n";
	  		
	  		out.write(str.getBytes("UTF-8"));
	  		
			clients.add(client);
			System.out.println("New client "+socket.getInetAddress().toString().substring(1)+" entity id: " +EntityManager.entityId);
			
			new S00AddEntity().writePacketData().sendPacketToAll();
			
			for (Entity e : EntityManager.entities) {
				new S00AddEntity(e.getId(), EntityTypes.PLAYER, e.getPosition(), e.getScale(), e.getRotation()).writePacketData().sendPacket(client);
			}
			
			for (Cube c : Core.cubes) {
				new S04AddBlock(c.getTexture(), c.getPosition(), c.getScale(), c.getRotation(), c.getColor()).writePacketData().sendPacket(client);
			}
			
	  		Thread connectionThread = new Thread(new Runnable(){
				public synchronized void run(){
					try {
						while (!socket.isClosed()) {
							readClient(client);
						}
					} catch (Exception e) {
						System.err.println("Error while writing data :"+e.toString());
					}	
				}
			});
			connectionThread.setDaemon(true);
			connectionThread.setName("Connection output thread");
			connectionThread.start();
	  		
		} catch (Exception e) {
			System.err.println("Could not establish connection: " + e);
		}
	}
	
	public void write(String data, Client client) {
		if (client.out == null) return;
		
		try {
			client.out.write(data.getBytes("UTF-8"));
		} catch (Exception e) {
			removeClient(client);
			System.err.println("Error while writing data :"+e.toString());
		}
	}
	
	private void readClient(Client client) {
		if (client.in == null) return;
		try {
			String str = client.in.readLine();
			if (str == null) {
				removeClient(client);
				return;
			}
			
			sort(str.charAt(0), str);
		} catch (Exception e) {
			removeClient(client);
			System.err.println("Error while reading data :"+e.toString());
		}
	}
	
	public void removeClient(Client client) {
		try {
			client.in.close();
			client.out.close();
			client.socket.close();
			clients.remove(client);
			new S03DeleteEntity(client.entityId).writePacketData().sendPacketToAll();
			System.out.println("Removed client entity " + client.entityId);
		} catch (Exception e) {
			System.err.println("Error while closing the connection :"+e.toString());
		}
	}
}
