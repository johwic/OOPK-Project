package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Conversation implements ActionListener, ListSelectionListener, SocketListener {
	private final ParticipantList<SocketThread> participants = new ParticipantList<SocketThread>();
	private final Hashtable<String, String> userInput = new Hashtable<String, String>();
	
	private final HTMLDocument doc;
	private final HTMLEditorKit kit;
	private List<SocketThread> selectedParticipants = null;
	
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
			String s;
			if ( m.isDisconnect() ) {
				s = "You disonnected.";
			} else {
				s = m.getText().replaceAll("<fetstil>", "<b>");
			}
			s.replaceAll("</fetstil>", "</b>");
			s.replaceAll("<kursiv>", "<i>");
			s.replaceAll("</kursiv>", "</i>");
			kit.insertHTML(doc, doc.getLength(),"<b>" + m.getSender() + ": </b><font color=\"" + m.getColor() + "\">" + s + "</font>", 0, 0, null);
			//System.out.println(m.getText());
		} catch (BadLocationException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	public void setUserInput(String key, String value) {
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
		if ( !userInput.containsKey("user_name") ) {
			JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if ( userInput.get("user_name").length() < 1) {
			JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		Message m = new Message();
		
		switch(arg0.getActionCommand()) {
			case "submit":
				if ( !userInput.containsKey("message_text") ) {
					JOptionPane.showMessageDialog(null, "Please enter a valid message.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if ( userInput.get("message_text").length() < 1) {
					JOptionPane.showMessageDialog(null, "Please enter a valid message.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				m.setSender(userInput.get("user_name"));
				m.setText(userInput.get("message_text"));
				if ( userInput.containsKey("text_color") ) {
					m.setColor(userInput.get("text_color") );
				} else {
					m.setColor("#000000");
				}
				
				send(m);
				break;
			case "disconnect":
				m.setDisconnect(true);
				m.setSender(userInput.get("user_name"));
				
				for ( SocketThread skt : selectedParticipants ) {
					try {
						skt.send(m);
						skt.terminate();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				participants.removeAll(selectedParticipants);
				break;
		}
	}
	
	@Override
	public String toString() {
		if ( participants.isEmpty() ) {
			return "Empty conversation";
		}
		
		return "Conversation 1";
	}

	@Override
	public void handleSocketEvent(SocketEvent e) {
		SocketThread source = (SocketThread) e.getSource();
		Message m = source.getMessage();
		
		if ( m.isDisconnect() ) {
			try {
				remove(source);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Message message = new Message();
			message.setSender("Server");
			message.setText(m.getSender() + " disconnected.");
			message.setColor("#000000");
			send(message);
		} else if ( m.getRequestReply() != null ) {
			try {
				remove(source);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Message message = new Message();
			message.setSender("Server");
			message.setText(m.getSender() + " declined connection. Message: " + m.getRequestMessage());
			message.setColor("#000000");
			insert(message);
		} else {
			relay(source, m);
			insert(m);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		JList<SocketThread> src = (JList<SocketThread>) arg0.getSource();
		
		selectedParticipants  = src.getSelectedValuesList();
	}
	
	public void diconnectAll() {
		Message m = new Message();
		m.setDisconnect(true);
		m.setSender(userInput.get("user_name"));
		
		for ( SocketThread skt : participants ) {
			try {
				skt.send(m);
				skt.terminate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//participants.removeAll(participants);
	}
}
