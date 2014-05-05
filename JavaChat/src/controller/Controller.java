package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.*;
import view.*;

public class Controller implements ServerSocketListener, ActionListener, WindowListener {
	
	private final View view;
	private final Model model;
	
	private ServerSocketThread server;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		view.addListeners(this);
	}

	@Override
	public void handleServerSocketEvent(ServerSocketEvent e) {

        // receive connection
		Socket clientSocket = ((ServerSocketThread) e.getSource()).getSocket();

        // make a message string
		SocketThread socket = new SocketThread(clientSocket);
		socket.start();
		Message requestMessage = socket.takeMessageTimeout(2000);
		String message = null;
		if (requestMessage != null) {
			message = "Connection recieved from " + clientSocket.toString() + ".\n Sender: " + requestMessage.getSender() + "\n Message: " + requestMessage.getRequestMessage() + "\n Please choose a conversation to join, or leave blank to close it:";
			socket.setName(requestMessage.getSender());
		} else {
			message = "Connection recieved from " + clientSocket.toString() + ".\n The client has not implemented B1.\n Please choose a conversation to join, or leave blank to close it:";
		}

        // content for dialog
        ArrayList<Conversation> selectionValues = new ArrayList<Conversation>();
		selectionValues.add(0, null);
		selectionValues.add(1, new Conversation());
		selectionValues.addAll(model.getConversations());
		Conversation conversation = (Conversation) JOptionPane.showInputDialog(view, message, "New socket", JOptionPane.QUESTION_MESSAGE, null, selectionValues.toArray(), null);

		if ( conversation != null ) {

            // make a new thread for conversation

            // pass socket thread to conversation
			
            // make a new tab (what is a "new" conversation?)
			if (conversation.isNew()) {
				conversation.add(socket);
				view.createTabUI(conversation, ((ServerSocketThread) e.getSource()).getName());
				model.addConversation(conversation);
			} else {
				conversation.add(socket);
			}
			
		} else {
			Message reply = new Message();
			// Check if client implemented B1
			if ( requestMessage != null ) {
				reply.setRequestReply("no");
				reply.setRequestMessage("bas");
			} else {
				reply.setDisconnect(true);
			}
			
			reply.setSender(((ServerSocketThread) e.getSource()).getName());
			
			socket.send(reply);
			try {
				socket.terminate();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch ( e.getActionCommand()) {
			case "server_socket_controller":
				String state = ((JButton) e.getSource()).getText();
				if (state.equals("Listen")) {
					int port;
					try {
						port = Integer.parseInt(model.getUserInput("server_socket_port"));
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(view, "Please enter a valid port number", "Invalid port number", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if ( model.getUserInput("server_socket_name") == null ) {
						JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else if ( model.getUserInput("server_socket_name").length() < 1) {
						JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
						return;
					}					
					
					server = new ServerSocketThread(port);
					server.addEventListener(this);
					server.setName(model.getUserInput("server_socket_name"));
					
					model.startServer(server);
					
					((JButton) e.getSource()).setText("Stop server");
					System.out.println("Listening to incoming connections on port " + port);
				} else {
					model.stopServer();
	
					((JButton) e.getSource()).setText("Listen");
					System.out.println("Server stopped");
				}
				break;
			case "socket_controller":
				int sport;
				try {
					sport = Integer.parseInt(model.getUserInput("socket_port"));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(view, "Please enter a valid port number", "Invalid port number", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if ( model.getUserInput("socket_ip") == null ) {
					JOptionPane.showMessageDialog(null, "Please enter a valid IP number.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if ( model.getUserInput("socket_ip").length() < 1) {
					JOptionPane.showMessageDialog(null, "Please enter a valid IP number.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				if ( model.getUserInput("request_name") == null ) {
					JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if ( model.getUserInput("request_name").length() < 1) {
					JOptionPane.showMessageDialog(null, "Please enter a valid name.", "Invalid input", JOptionPane.INFORMATION_MESSAGE);
					return;
				}				
				
				try {
					Conversation conversation = new Conversation();
					SocketThread socket = new SocketThread(new Socket(model.getUserInput("socket_ip"), sport));
					
					conversation.add(socket);
					Message m = new Message();
					if ( model.getUserInput("request_message") != null ) {
						m.setRequestMessage(model.getUserInput("request_message"));
					} else {
						m.setRequestMessage("");
					}
					m.setSender(model.getUserInput("request_name"));
					// send request message
					socket.send(m);
					
					view.createTabUI(conversation, model.getUserInput("request_name"));
					model.addConversation(conversation);
				} catch ( IllegalArgumentException | IOException e1) {
					JOptionPane.showMessageDialog(null, "Connection failed. Please check that you have entered correct IP and port number.", "Connection failed", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
		}	
	}

	@Override
	public void windowActivated(WindowEvent e) {
		return;
	}

	@Override
	public void windowClosed(WindowEvent e) {
		return;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		ArrayList<Conversation> conv = model.getConversations();
		for ( Conversation c : conv ) {
			c.disconnectAll();
		}
		if ( server != null ) {
			server.terminate();
		}
		return;
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		return;
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		return;
	}

	@Override
	public void windowIconified(WindowEvent e) {
		return;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		return;
	}
}
