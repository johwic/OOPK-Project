package model;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

public class XMLWriter implements Closeable {
	
	private final PrintWriter out;
	private final XMLEventWriter writer;
	
	public XMLWriter(PrintWriter out) throws XMLStreamException {
		this.out = out;
		
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		writer = outputFactory.createXMLEventWriter(this.out);
	}
	
	public void writeMessage(Message m) {
		XMLEventFactory factory = XMLEventFactory.newInstance();
		
		try {
			writer.add(factory.createStartDocument("UTF-8", "1.0"));
			ArrayList<Attribute> attr = new ArrayList<Attribute>();
			attr.add(factory.createAttribute("sender", m.getSender()));
			writer.add(factory.createStartElement(new QName("message"), attr.iterator(), null));
			writer.add(factory.createStartElement(new QName("text"), null, null));
			writer.add(factory.createCharacters(m.getText()));
			writer.add(factory.createEndElement(new QName("text"), null));					
			writer.add(factory.createEndElement(new QName("message"), null));
			writer.add(factory.createEndDocument());
			writer.flush();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
