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

/**
 * Manages list of participants (threads).
 * Forwards messages to everyone.
 * Tracks colors and styles for each participant.
 */
public class Conversation implements ActionListener, ListSelectionListener, SocketListener {

    // list of active socket threads (why final? who might mess it up?)
	private final ParticipantList<SocketThread> participants = new ParticipantList<SocketThread>();

    // maps user to style
	private final Hashtable<String, String> userInput = new Hashtable<String, String>();
	
	private final HTMLDocument doc;
	private final HTMLEditorKit kit;
	private List<SocketThread> selectedParticipants = null;
	
	public Conversation() {
		this.doc = new HTMLDocument();
		this.kit = new HTMLEditorKit();
	}

    /**
     * Adds and starts a socket thread to participant list.
     *
     */
	public void add(SocketThread skt) {

        // socket thread listens to events from this
		skt.addEventListener(this);

        // start the thread
		skt.start();

        // add socket thread to list of active socket threads
		participants.add(skt);
	}

    /**
     * Kills and removes the passed socket thread from participant list.
     */
	public void remove(SocketThread skt) throws IOException {

        // kill thread and remove it from list of active socket threads
		skt.terminate();
		participants.remove(skt);
	}
	
	public ParticipantList<SocketThread> getParticipants() {
		return participants;
	}
	
	public boolean isNew() {
		return participants.isEmpty();
	}

    /**
     * Forward message to everyone in conversation.
     */
	private void relay(SocketThread sender, Message m) {
		for ( SocketThread skt : participants ) {
			if ( skt == sender ) {
				continue;
			} else {
				skt.send(m);
			}
		}
	}

    /**
     * Sends to everyone in conversation.
     */
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

    /**
     * Inserts message into editor kit.
     */
	private void insert(Message m) {
		try {
			String s;
			if ( m.isDisconnect() ) {
				s = "You disconnected.";
			} else {
				s = m.getText();
			}
			s.replaceAll("<", "&lt;");
			s.replaceAll(">", "&gt;");
			s.replaceAll("&lt;fetstil&gt;", "<b>");
			s.replaceAll("&lt;/fetstil&gt;", "</b>");
			s.replaceAll("&lt;kursiv&gt;", "<i>");
			s.replaceAll("&lt;/kursiv&gt;", "</i>");
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

    /**
     * Nested listener class for local user input.
     *
     */
	private class UserInputListener implements DocumentListener {
		
		private final String actionCommand;
		
		public UserInputListener(String actionCommand) {
			this.actionCommand = actionCommand;
		}

        /**
         * Not used.
         * @param arg0
         */
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			return;
		}

        /**
         * Called by document listener.
         * @param arg0
         */
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

    /**
     * Handles incoming action events:
     *
     *  submit
     *  A message is submitted from local user. New Message object is created, colored and sent to all participants.
     *
     *  disconnect
     *  Local user disconnected. A disconnect message is sent to everyone. All participant threads are killed.
     */
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

    /**
     * Handles incoming events (messages) from participants.
     *
     * Disconnect messages remove participant.
     * Other messages are forwarded to everyone and displayed locally.
     */
	@Override
	public void handleSocketEvent(SocketEvent e) {

        // get incoming message from one of the participants
		SocketThread source = (SocketThread) e.getSource();
		Message m = source.getMessage();

        // if a disconnect message, remove participoant
		if ( m.isDisconnect() ) {
			try {
				remove(source);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            // display message that participant disconnected
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
            // if not a disconnect message, forward to everyone and display message locally
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
