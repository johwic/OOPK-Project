package model;

//import java.util.ArrayList;
//import java.util.Hashtable;

/**
 * Contains message text, color, sender.
 * Disconnect flag makes it a disconnect message.
 */
public class Message {
	private String sender;
	private String text;
	private String color;

    // disconnect flag
	private boolean disconnect;
	
	/*public static Hashtable<String, ArrayList<String>> getHashtable() {
		Hashtable<String, ArrayList<String>> map = new Hashtable<String, ArrayList<String>>();
		
		ArrayList<String> msg_attr = new ArrayList<String>();
		msg_attr.add("sender");
		
		map.put("message", msg_attr);
		
		return map;
	}*/
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public boolean isDisconnect() {
		return disconnect;
	}
	
	public void setDisconnect(boolean disconnect) {
		this.disconnect = disconnect;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
