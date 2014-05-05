package model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import crypto.Crypto;

/**
 * StAx implementation for XML parsing of messages.
 * Cursor model.
 */
public class XMLReader implements Closeable {
	private static final String MESSAGE = "message";
	private static final String TEXT = "text";
	private static final String SENDER = "sender";
	private static final String DISCONNECT = "disconnect";
	private static final String COLOR = "color";
	private static final String REQUEST = "request";
	private static final String REPLY = "reply";
	
	private final BufferedReader in;
	private final XMLInputFactory inputFactory;
	private final Crypto crypto;
	
	private XMLEventReader reader;
	private Message message;
	
	public XMLReader(BufferedReader in) throws XMLStreamException {
		this.in = in;
		this.crypto = new Crypto();
		
		inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
	}

	private void addAttribute(String tag, String attribute, String value, boolean decrypt, String type, String key) {
		if (decrypt) {
			value = crypto.decrypt(value, key, type);
		}
		
		switch (tag) {
			case MESSAGE:
				switch (attribute) {
				case SENDER:
					message.setSender(value);
					break;
				}
				break;
	
			case TEXT:
				switch (attribute) {
				case COLOR:
					message.setColor(value);
					break;
				}
				break;
				
			case REQUEST:
				switch (attribute) {
				case REPLY:
					message.setRequestReply(value);
					break;
				}
				break;
				
			case "filerequest":
				switch (attribute) {
				case "size":
					message.setFileSize(Integer.parseInt(value));
					break;
				case "name":
					message.setFileName(value);
					break;
				}
				break;
			
			case "fileresponse":
				switch (attribute) {
				case "port":
					message.setFilePort(Integer.parseInt(value));
					break;
				case "reply":
					message.setFileResponse(value);
					break;
				}
				break;
				
			case "keyrequest":
				switch (attribute) {
				case "type":
					message.setKeyRequestType(value);
					break;
				}
				break;				
		}
	}

	private void addTag(String tag, String value, boolean decrypt, String type, String key) {
		if (decrypt) {
			value = crypto.decrypt(value, key, type);
		}
		
		switch (tag) {
			case MESSAGE:
				break;

			case TEXT:
				message.setText(value);
				break;

			case DISCONNECT:
				message.setDisconnect(true);
				break;
				
			case REQUEST:
				message.setRequestMessage(value);
				break;
				
			case "filerequest":
				message.setFileMessage(value);
				break;
				
			case "fileresponse":
				message.setFileResponseMessage(value);
				break;
				
			case "keyrequest":
				message.setKeyRequest(value);
				break;
		}
	}

	public Message readMessage() throws XMLStreamException {
		// Create new reader every time, otherwise can't read multiple documents.
		reader = inputFactory.createXMLEventReader(this.in);
		message = new Message();
		
		boolean decrypt = false;
		String type = null;
		String key = null;
		String tagContent = null;
		
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			System.out.println(event.toString());
			
			switch (event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					StartElement elem = event.asStartElement();
					if (elem.getName().getLocalPart().equals(MESSAGE)) {
						message = new Message();
						addAttribute(MESSAGE, "sender", elem.getAttributeByName(new QName("sender")).getValue(), false, null, null);
					} else if (elem.getName().getLocalPart().equals("encrypted")) {
						decrypt = true;
						type = elem.getAttributeByName(new QName("type")).getValue();
						key = elem.getAttributeByName(new QName("key")).getValue();
						message.setEncryptionAlgo(type);
						message.setEncryptionKey(key);
					} else {
						Iterator<?> attributes = elem.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							addAttribute(elem.getName().getLocalPart(), attribute.getName().getLocalPart(), attribute.getValue(), decrypt, type, key);
						}
//						if (elem.getName().getLocalPart().equals(TEXT)) {
//							event = reader.nextEvent();
//							System.out.println(event.toString());
//							addTag(TEXT, event.asCharacters().getData(), decrypt, type, key);
//						}
//						if (elem.getName().getLocalPart().equals(REQUEST)) {
//							event = reader.nextEvent();
//							System.out.println(event.toString());
//							addTag(REQUEST, event.asCharacters().getData(), decrypt, type, key);
//						}
//						if (elem.getName().getLocalPart().equals("filerequest")) {
//							event = reader.nextEvent();
//							System.out.println(event.toString());
//							addTag("filerequest", event.asCharacters().getData(), decrypt, type, key);
//						}
//						if (elem.getName().getLocalPart().equals("fileresponse")) {
//							event = reader.nextEvent();
//							System.out.println(event.toString());
//							addTag("fileresponse", event.asCharacters().getData(), decrypt, type, key);
//						}
//						if (elem.getName().getLocalPart().equals("keyrequest")) {
//							event = reader.nextEvent();
//							System.out.println(event.toString());
//							addTag("keyrequest", event.asCharacters().getData(), decrypt, type, key);
//						}						
					}
					break;
	
				case XMLStreamConstants.CHARACTERS:
					tagContent = event.asCharacters().getData();
					break;
	
				case XMLStreamConstants.END_ELEMENT:
					EndElement elem1 = event.asEndElement();
					if ( elem1.getName().getLocalPart().equals(MESSAGE) ) {
						reader.close();
						return message;
					} else if ( elem1.getName().getLocalPart().equals(DISCONNECT) ) {
						message.setDisconnect(true);
					} else if ( elem1.getName().getLocalPart().equals("encrypted")) {
						decrypt = false;
					} else {
						addTag(elem1.getName().getLocalPart(), tagContent, decrypt, type, key);
						tagContent = null;
					}
					break;
			}
		}
		
		reader.close();
		return message;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			try {
				reader.close();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
		in.close();
	}
}
