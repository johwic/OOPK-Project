package model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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
	
	private XMLEventReader reader;
	private Message message;
	
	public XMLReader(BufferedReader in) throws XMLStreamException {
		this.in = in;
		
		inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
	}

	private void addAttribute(String tag, String attribute, String value) {
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
		}		
	}

	private void addTag(String tag, String value) {
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
		}	
	}

	public Message readMessage() throws XMLStreamException {
		// Create new reader every time, otherwise can't read multiple documents.
		reader = inputFactory.createXMLEventReader(this.in);
		message = new Message();
		
		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			
			switch (event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					StartElement elem = event.asStartElement();
					if (elem.getName().getLocalPart().equals(MESSAGE)) {
						message = new Message();
					}
	
					Iterator<?> attributes = elem.getAttributes();
					while (attributes.hasNext()) {
						Attribute attribute = (Attribute) attributes.next();
						addAttribute(elem.getName().getLocalPart(), attribute.getName().getLocalPart(), attribute.getValue());
					}
					if (elem.getName().getLocalPart().equals(TEXT)) {
						event = reader.nextEvent();
						addTag(TEXT, event.asCharacters().getData());
					}
					if (elem.getName().getLocalPart().equals(REQUEST)) {
						event = reader.nextEvent();
						addTag(REQUEST, event.asCharacters().getData());
					}					
					break;
	
					//case XMLStreamConstants.CHARACTERS:
						//	tagContent = event.asCharacters().getData().trim();
					//	break;
	
				case XMLStreamConstants.END_ELEMENT:
					EndElement elem1 = event.asEndElement();
					if ( elem1.getName().getLocalPart().equals(MESSAGE) ) {
						reader.close();
						return message;
					} else if ( elem1.getName().getLocalPart().equals(DISCONNECT) ) {
						message.setDisconnect(true);
					}
					//addTag(elem1.getName().getLocalPart(), tagContent);
					break;
			}
		}
		
		reader.close();
		return message;
	}

	@Override
	public void close() throws IOException {
		try {
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		in.close();
	}
}
