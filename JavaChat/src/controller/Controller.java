package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.xml.stream.XMLStreamException;

import model.*;
import view.*;

public class Controller implements ServerSocketListener, SocketListener, ActionListener {
	
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
		Socket clientSocket = ((ServerSocketThread) e.getSource()).clientSocket;
		String message = "Connection recieved from " + clientSocket.toString();
		int choice = JOptionPane.showConfirmDialog(view, message, "New socket", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if ( choice == JOptionPane.YES_OPTION ) {
			SocketThread socket = null;
			try {
				socket = new SocketThread(clientSocket);
			} catch (XMLStreamException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			socket.addEventListener(this);
			socket.start();
			
			view.createTabUI(this);
			model.addSocket(socket);
		} else {
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Socket denied");
		}
	}

	@Override
	public void handleSocketEvent(SocketEvent e) {
		SocketThread source = (SocketThread) e.getSource();
		String m = source.message.getText();
		String sender = source.message.getSender();

		try {
			view.getTabInterface(0).append("<b>" + sender + ":</b> " + m);
		} catch (BadLocationException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch ( e.getActionCommand()) {
		case "chat_submit_0":
			Message m = new Message();
			m.setSender("Johan");
			m.setText("Hej da, vi ses kanske någon gang");
			model.getSocket(0).send(m);
			break;
		case "server_socket_controller":
			int port;
			try {
				port = Integer.parseInt(model.getUserInput("server_socket_port"));
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(view, "Please enter a valid port number", "Invalid port number", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			server = new ServerSocketThread(port);
			server.addEventListener(this);
			
			Thread t = new Thread(server);
			t.start();
			
			System.out.println("Listening to incoming connections on port " + port);
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
				SocketThread socket = new SocketThread(new Socket(model.getUserInput("socket_ip"), sport));
				socket.addEventListener(this);
				socket.start();
				
				view.createTabUI(this);
				model.addSocket(socket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (XMLStreamException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}	
	}
}
