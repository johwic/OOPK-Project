package model;

import java.util.ArrayList;

public class Model {
	private final ArrayList<SocketThread> sockets;
	private StringBuilder sb = new StringBuilder();
	
	public Model() {
		sockets = new ArrayList<SocketThread>();
	}
	
	public void addSocket(SocketThread socket) {
		sockets.add(socket);
	}
	
	public void updateText(String s) {
		sb.append(s);
	}
	
	public String getText() {
		return sb.toString();
	}
}
