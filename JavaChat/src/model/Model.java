package model;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Model {
	private final ArrayList<SocketThread> sockets;
	private final Hashtable<String, String> userInput = new Hashtable<String, String>();
	
	public Model() {
		sockets = new ArrayList<SocketThread>();
	}
	
	public void addSocket(SocketThread socket) {
		sockets.add(socket);
	}
	
	public SocketThread getSocket(int id) {
		return sockets.get(id);
	}
	
	public DocumentListener getDocumentListener(String action) {
		return new UserInputListener(action);
	}
	
	private void setUserInput(String key, String value) {
		userInput.put(key, value);
	}
	
	public String getUserInput(String key) {
		return userInput.get(key);
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
}
