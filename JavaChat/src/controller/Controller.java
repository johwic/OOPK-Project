package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import model.*;
import view.*;

public class Controller implements ServerSocketListener, SocketListener, ActionListener {
	
	private final View view;
	private final Model model;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		ServerSocketThread ssock = new ServerSocketThread(11111);
		ssock.addEventListener(this);
		
		Thread t = new Thread(ssock);
		t.start();
		
		System.out.println("Listening to incoming connections.");
		
		//ssock.terminate();
	}

	@Override
	public void handleServerSocketEvent(ServerSocketEvent e) {
		SocketThread socket = new SocketThread(((ServerSocketThread) e.getSource()).clientSocket);
		socket.addEventListener(this);
		Thread t = new Thread(socket);
		t.start();
	}

	@Override
	public void handleSocketEvent(SocketEvent e) {
		SocketThread source = (SocketThread) e.getSource();
		
		model.updateText(source.clientCommand);
		view.getTabInterface().getMessagePane().setText(model.getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
