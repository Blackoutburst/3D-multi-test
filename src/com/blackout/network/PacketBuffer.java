package com.blackout.network;

public class PacketBuffer implements PacketUtils {
	
	protected String data = "";
	
	public PacketBuffer() {
		this.data = "";
	}
	
	public PacketBuffer(String data) {
		this.data = data;
		subHead();
	}
	
	public PacketBuffer writeInt(int val) {
		data += String.valueOf(val) + String.valueOf(SEPARATOR);
		return (this);
	}
	
	public PacketBuffer writeFloat(float val) {
		data += String.valueOf(val) + String.valueOf(SEPARATOR);
		return (this);
	}
	
	public PacketBuffer writeChar(char val) {
		data += String.valueOf(val) + String.valueOf(SEPARATOR);
		return (this);
	}
	
	public PacketBuffer writeString(String val) {
		data += String.valueOf(val) + String.valueOf(SEPARATOR);
		return (this);
	}
	
	public String getData() {
		return(this.data + END);
	}
	
	public int readInt() {
		String val[] = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (-1);
		
		try {
			int ret = Integer.valueOf(val[0]);
			return (ret);
		} catch (Exception e) {
			return (-1);
		}
	}
	
	public float readFloat() {
		String val[] = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (-1);
		
		try {
			float ret = Float.valueOf(val[0]);
			return (ret);
		} catch (Exception e) {
			return (-1);
		}
	}
	
	public char readChar() {
		String val[] = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (0);
		
		try {
			char ret = val[0].charAt(0);
			return (ret);
		} catch (Exception e) {
			return (0);
		}
	}
	
	public String readString() {
		String val[] = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return ("");
		
		try {
			String ret = String.valueOf(val[0]);
			return (ret);
		} catch (Exception e) {
			return ("");
		}
	}
	
	private void subHead() {
		String str[] = data.split("((?<="+SEPARATOR+")|(?="+SEPARATOR+"))");
		String newData = "";
		
		try {
			for(int i = 2; i < str.length; i++) {
				newData += str[i];
			}
		} catch (Exception e) {}
		
		this.data = newData;
	}
	
}
