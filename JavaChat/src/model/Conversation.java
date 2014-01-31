package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Conversation implements ActionListener, SocketListener {
	private final ParticipantList<SocketThread> participants = new ParticipantList<SocketThread>();
	private final Hashtable<String, String> userInput = new Hashtable<String, String>();
	
	private final HTMLDocument doc;
	private final HTMLEditorKit kit;
	
	public Conversation() {
		this.doc = new HTMLDocument();
		this.kit = new HTMLEditorKit();
	}
	
	public void add(SocketThread skt) {
		skt.addEventListener(this);
		skt.start();
		participants.add(skt);
	}
	
	public void remove(SocketThread skt) throws IOException {
		skt.terminate();
		participants.remove(skt);
	}
	
	public ParticipantList<SocketThread> getParticipants() {
		return participants;
	}
	
	public boolean isNew() {
		return participants.isEmpty();
	}
	
	private void relay(SocketThread sender, Message m) {
		for ( SocketThread skt : participants ) {
			if ( skt == sender ) {
				continue;
			} else {
				skt.send(m);
			}
		}
	}
	
	public void send(Message m) {
		for ( SocketThread skt : participants ) {
			skt.send(m);
		}
		
		insert(m);
	}
	
	public StyledDocument getDocument() {
		return doc;
	}
	
	public EditorKit getEditorKit() {
		return kit;
	}
	
	private void insert(Message m) {
		try {
			kit.insertHTML(doc, doc.getLength(), m.getText(), 0, 0, null);
			System.out.println(m.getText());
		} catch (BadLocationException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	private void setUserInput(String key, String value) {
		userInput.put(key, value);
	}
	
	public DocumentListener getDocumentListener(String action) {
		return new UserInputListener(action);
	}	

	private class UserInputListener implements DocumentListener {
		
		private final String actionCommand;
		
		public UserInputListener(String actionCommand) {
			this.actionCommand = actionCommand;
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			return;
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			Document document = (Document) arg0.getDocument();
			
			try {
				setUserInput(actionCommand, document.getText(0, document.getLength()));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			Document document = (Document) arg0.getDocument();
			
			try {
				setUserInput(actionCommand, document.getText(0, document.getLength()));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Message m = new Message();
		m.setSender("Johan");
		m.setText("Hej! Lorem ipsum dolor sit amet.");
		
		send(m);
	}
	
	@Override
	public String toString() {
		if ( participants.isEmpty() ) {
			return "New conversation";
		}
		
		return "Conversation 1";
	}

	@Override
	public void handleSocketEvent(SocketEvent e) {
		SocketThread source = (SocketThread) e.getSource();
		Message m = source.getMessage();
		
		relay(source, m);
		insert(m);
	}
}
