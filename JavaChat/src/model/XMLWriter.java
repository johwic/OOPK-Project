package model;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import crypto.Crypto;

public class XMLWriter implements Closeable {
	
	private final PrintWriter out;
	private final XMLStreamWriter writer;
	private final Crypto crypto;
	
	public XMLWriter(PrintWriter out) throws XMLStreamException {
		this.out = out;
		
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		writer = outputFactory.createXMLStreamWriter(this.out);
		this.crypto = new Crypto();
	}
	
	public void writeMessage(Message m) throws XMLStreamException {

		writer.writeStartDocument("UTF-8", "1.0");
		writer.writeStartElement("message");
		writer.writeAttribute("sender", m.getSender());
		
		if ( m.getKeyRequest() != null ) {
			writer.writeStartElement("keyrequest");
			writer.writeAttribute("type", m.getKeyRequestType());
			writer.writeCharacters(m.getKeyRequest());
			writer.writeEndElement();			
		} else if ( m.getEncryptionAlgo() != null ) {
			
			String key;
			if ( m.getEncryptionKey() != null ) {
				key = m.getEncryptionKey();
			} else {
				key = crypto.getKey(m.getEncryptionAlgo());
			}
			
			writer.writeStartElement("encrypted");
			writer.writeAttribute("type", m.getEncryptionAlgo());
			writer.writeAttribute("key", key);
			
			if ( m.isDisconnect() ) {
				writer.writeEmptyElement("disconnect");
			} else if ( m.getRequestMessage() != null ) {
				writer.writeStartElement("request");
				if ( m.getRequestReply() != null) {
					writer.writeAttribute("reply", crypto.encrypt(m.getRequestReply(), key, m.getEncryptionAlgo()));
				}
				writer.writeCharacters(crypto.encrypt(m.getRequestMessage(), key, m.getEncryptionAlgo()));
				writer.writeEndElement();
			} else if ( m.getFileName() != null ) {
				writer.writeStartElement("filerequest");
				writer.writeAttribute("name", crypto.encrypt(m.getFileName(), key, m.getEncryptionAlgo()));
				writer.writeAttribute("size", crypto.encrypt(Integer.toString(m.getFileSize()), key, m.getEncryptionAlgo()));
				writer.writeCharacters(crypto.encrypt(m.getFileMessage(), key, m.getEncryptionAlgo()));
				writer.writeEndElement();
			} else if ( m.getFileResponse() != null) {
				writer.writeStartElement("fileresponse");
				writer.writeAttribute("reply", crypto.encrypt(m.getFileResponse(), key, m.getEncryptionAlgo()));
				writer.writeAttribute("port", crypto.encrypt(Integer.toString(m.getFilePort()), key, m.getEncryptionAlgo()));
				writer.writeCharacters(crypto.encrypt(m.getFileResponseMessage(), key, m.getEncryptionAlgo()));
				writer.writeEndElement();
			} else if ( m.getText() != null ) {
				writer.writeStartElement("text");
				if ( m.getColor() != null ) {
					writer.writeAttribute("color", crypto.encrypt(m.getColor(), key, m.getEncryptionAlgo()));
				}
				writer.writeCharacters(crypto.encrypt(m.getText(), key, m.getEncryptionAlgo()));
				writer.writeEndElement();
			}
			
			writer.writeEndElement();
		} else {
			if ( m.isDisconnect() ) {
				writer.writeEmptyElement("disconnect");
			} else if ( m.getRequestMessage() != null ) {
				writer.writeStartElement("request");
				if ( m.getRequestReply() != null) {
					writer.writeAttribute("reply", m.getRequestReply());
				}
				writer.writeCharacters(m.getRequestMessage());
				writer.writeEndElement();
			} else if ( m.getFileName() != null ) {
				writer.writeStartElement("filerequest");
				writer.writeAttribute("name", m.getFileName());
				writer.writeAttribute("size", Integer.toString(m.getFileSize()));
				writer.writeCharacters(m.getFileMessage());
				writer.writeEndElement();
			} else if ( m.getFileResponse() != null) {
				writer.writeStartElement("fileresponse");
				writer.writeAttribute("reply", m.getFileResponse());
				writer.writeAttribute("port", Integer.toString(m.getFilePort()));
				writer.writeCharacters(m.getFileResponseMessage());
				writer.writeEndElement();
			} else if ( m.getText() != null ) {
				writer.writeStartElement("text");
				if ( m.getColor() != null ) {
					writer.writeAttribute("color", m.getColor());
				}
				writer.writeCharacters(m.getText());
				writer.writeEndElement();
			}
		}

		writer.writeEndElement();
		writer.writeEndDocument();

		writer.flush();
	}

	@Override
	public void close() throws IOException {
		try {
			writer.close();
			out.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
