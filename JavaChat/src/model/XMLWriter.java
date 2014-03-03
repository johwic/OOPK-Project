package model;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLWriter implements Closeable {
	
	private final PrintWriter out;
	private final XMLStreamWriter writer;
	
	public XMLWriter(PrintWriter out) throws XMLStreamException {
		this.out = out;
		
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		writer = outputFactory.createXMLStreamWriter(this.out);
	}
	
	public void writeMessage(Message m) throws XMLStreamException {

		writer.writeStartDocument("UTF-8", "1.0");
		writer.writeStartElement("message");
		writer.writeAttribute("sender", m.getSender());
		
		if ( m.isDisconnect() ) {
			writer.writeEmptyElement("disconnect");
		} else {
			writer.writeStartElement("text");
			writer.writeAttribute("color", m.getColor());
			writer.writeCharacters(m.getText());
			writer.writeEndElement();
		}

		writer.writeEndElement();
		writer.writeEndDocument();

		writer.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
		try {
			writer.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
