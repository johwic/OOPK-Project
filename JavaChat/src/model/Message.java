package model;

import java.util.ArrayList;

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
	
	private String requestMessage;
	private String requestReply;
	
	private String fileName;
	private int fileSize;
	private String fileMessage;
	
	private String fileResponse;
	private int filePort;
	private String fileResponseMessage;
	
	private String encryptionAlgo;
	private String encryptionKey;
	
	private String keyRequest;
	private String keyRequestType;
	
	private ArrayList<String> algorithms;
	private ArrayList<String> keys;
	private ArrayList<String> encryptedTags;

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
	
	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getRequestReply() {
		return requestReply;
	}

	public void setRequestReply(String requestReply) {
		this.requestReply = requestReply;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMessage() {
		return fileMessage;
	}

	public void setFileMessage(String fileMessage) {
		this.fileMessage = fileMessage;
	}

	public String getFileResponse() {
		return fileResponse;
	}

	public void setFileResponse(String fileResponse) {
		this.fileResponse = fileResponse;
	}

	public int getFilePort() {
		return filePort;
	}

	public void setFilePort(int filePort) {
		this.filePort = filePort;
	}

	public String getFileResponseMessage() {
		return fileResponseMessage;
	}

	public void setFileResponseMessage(String fileResponseMessage) {
		this.fileResponseMessage = fileResponseMessage;
	}

	public String getEncryptionAlgo() {
		return encryptionAlgo;
	}

	public void setEncryptionAlgo(String encryptionAlgo) {
		this.encryptionAlgo = encryptionAlgo;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public String getKeyRequest() {
		return keyRequest;
	}

	public void setKeyRequest(String keyRequest) {
		this.keyRequest = keyRequest;
	}

	public String getKeyRequestType() {
		return keyRequestType;
	}

	public void setKeyRequestType(String keyRequestType) {
		this.keyRequestType = keyRequestType;
	}

	@Override
	public String toString() {
		return text;
	}

	public void addEncryptionInfo(String tag, String algorithm, String key) {
		algorithms.add(algorithm);
		keys.add(key);
		encryptedTags.add(tag);
	}
}
