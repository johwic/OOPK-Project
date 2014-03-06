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

        // receive connection
		Socket clientSocket = ((ServerSocketThread) e.getSource()).getSocket();

        // make a message string
		SocketThread socket = new SocketThread(clientSocket);
		socket.start();
		Message requestMessage = socket.takeMessageTimeout(2000);
		String message = null;
		if (requestMessage != null) {
			message = "Connection recieved from " + clientSocket.toString() + ".\n Sender: " + requestMessage.getSender() + "\n Message: " + requestMessage.getRequestMessage() + "\n Please choose a conversation to join, or leave blank to close it:";
		} else {
			message = "Connection recieved from " + clientSocket.toString() + ".\n The client has not implemented B1.\n Please choose a conversation to join, or leave blank to close it:";
		}
		
		ArrayList<Conversation> selectionValues = new ArrayList<Conversation>();

        // content for dialog
        ArrayList<Conversation> selectionValues = new ArrayList<Conversation>();
		selectionValues.add(0, null);
		selectionValues.add(1, new Conversation());
		selectionValues.addAll(model.getConversations());
		Conversation conversation = (Conversation) JOptionPane.showInputDialog(view, message, "New socket", JOptionPane.QUESTION_MESSAGE, null, selectionValues.toArray(), null);


		if ( conversation != null ) {

            // make a new thread for conversation

            // make a new tab (what is a "new" conversation?)
			if (conversation.isNew()) {
				view.createTabUI(conversation);
				model.addConversation(conversation);
			}
			

            // pass socket thread to conversation
			conversation.add(socket);

		} else {
			Message reply = new Message();
			if ( requestMessage != null ) {
				reply.setRequestReply("no");
				reply.setRequestMessage("bas");
			} else {
				reply.setText("bas");
			}
			reply.setSender("Johan");
			
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
					
					conversation.add(socket);
					Message m = new Message();
					m.setRequestMessage("Hej");//model.getUserInput("request_message"));
					m.setSender(model.getUserInput("request_name"));
					// send request message
					socket.send(m);
					
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
