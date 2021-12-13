package com.blackoutburst.network;

public class PacketBuffer implements PacketUtils {
	
	protected String data;
	
	public PacketBuffer() {
		this.data = "";
	}
	
	public PacketBuffer(String data) {
		this.data = data;
		subHead();
	}
	
	public void writeInt(int val) {
		data += val + String.valueOf(SEPARATOR);
	}
	
	public void writeFloat(float val) {
		data += val + String.valueOf(SEPARATOR);
	}
	
	public void writeChar(char val) {
		data += val + String.valueOf(SEPARATOR);
	}
	
	public void writeString(String val) {
		data += val + SEPARATOR;
	}
	
	public String getData() {
		return(this.data + END);
	}
	
	public int readInt() {
		String[] val = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (-1);
		
		try {
			return (Integer.parseInt(val[0]));
		} catch (Exception e) {
			return (-1);
		}
	}
	
	public float readFloat() {
		String[] val = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (-1);
		
		try {
			return (Float.parseFloat(val[0]));
		} catch (Exception e) {
			return (-1);
		}
	}
	
	public char readChar() {
		String[] val = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return (0);
		
		try {
			return (val[0].charAt(0));
		} catch (Exception e) {
			return (0);
		}
	}
	
	public String readString() {
		String[] val = this.data.split(String.valueOf(SEPARATOR));
		subHead();
		
		if (val.length == 0) return ("");
		
		try {
			return (String.valueOf(val[0]));
		} catch (Exception e) {
			return ("");
		}
	}
	
	private void subHead() {
		String[] str = data.split("((?<="+SEPARATOR+")|(?="+SEPARATOR+"))");
		StringBuilder newData = new StringBuilder();
		
		try {
			for(int i = 2; i < str.length; i++) {
				newData.append(str[i]);
			}
		} catch (Exception ignored) {}
		
		this.data = newData.toString();
	}
	
}
