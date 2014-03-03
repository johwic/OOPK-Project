package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.*;
import view.*;

public class Controller implements ServerSocketListener, ActionListener {
	
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
		Socket clientSocket = ((ServerSocketThread) e.getSource()).getSocket();
		String message = "Connection recieved from " + clientSocket.toString() + ".\n Please choose a conversation to join, or leave blank to close it:";
		ArrayList<Conversation> selectionValues = new ArrayList<Conversation>();
		
		selectionValues.add(0, null);
		selectionValues.add(1, new Conversation());
		selectionValues.addAll(model.getConversations());
		Conversation conversation = (Conversation) JOptionPane.showInputDialog(view, message, "New socket", JOptionPane.QUESTION_MESSAGE, null, selectionValues.toArray(), null);
		
		if ( conversation != null ) {
			SocketThread socket = new SocketThread(clientSocket);
			
			if (conversation.isNew()) {
				view.createTabUI(conversation);
				model.addConversation(conversation);
			}
			conversation.add(socket);
		} else {
			conversation = new Conversation();
			SocketThread socket = null;
			socket = new SocketThread(clientSocket);
			//socket.addEventListener(this);
			//socket.start();
			
			if (conversation.isNew()) {
				view.createTabUI(conversation);
				model.addConversation(conversation);
			}
			conversation.add(socket);
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
					
					server = new ServerSocketThread(port);
					server.addEventListener(this);
					
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
					e1.printStackTrace();
					return;
				}
				try {
					Conversation conversation = new Conversation();
					SocketThread socket = new SocketThread(new Socket(model.getUserInput("socket_ip"), sport));
					//socket.addEventListener(this);
					//socket.start();
					conversation.add(socket);
					
					view.createTabUI(conversation);
					model.addConversation(conversation);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
		}	
	}
}
