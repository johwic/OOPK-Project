package model;

import java.util.ArrayList;

public class Conversation {
	private final ArrayList<SocketThread> participants = new ArrayList<SocketThread>();
	
	public Conversation() {
		
	}
	
	public void add(SocketThread skt) {
		participants.add(skt);
	}
}
