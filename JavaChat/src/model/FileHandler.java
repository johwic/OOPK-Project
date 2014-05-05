package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.ProgressMonitorInputStream;

import view.FileWindow;

public class FileHandler {
	
	private volatile BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<Message>();
	
	public void sendFile(File file, String ip) {
		Thread t = new Thread(new Sender(file, ip));
		t.start();
	}
	
	public synchronized void addResponse(Message m) {
		responseQueue.add(m);
	}
	
	public void addRequest(Message m, SocketThread skt) {
		FileWindow.createFileResponseWindow(this, m);
		Thread t = new Thread(new Receiver(m, skt));
		t.start();
	}
	
	private class Sender implements Runnable {

		private final File file;
		private final String ip;

		public Sender(File file, String ip) {
			this.file = file;
			this.ip = ip;
		}
		
		@Override
		public void run() {
			Message response = null;
			try {
				response = responseQueue.poll(1, TimeUnit.MINUTES);
				System.out.println("Here");
			} catch (InterruptedException e) {
				return;
			}
			
			if ( response != null ) {
				if ( response.getFileResponse().equals("yes") ) {
					Socket skt;
					BufferedOutputStream output;
					BufferedInputStream input;
					
					try {
						skt = new Socket(ip, response.getFilePort());
						output = new BufferedOutputStream(skt.getOutputStream());
						input = new BufferedInputStream(new FileInputStream(this.file));
	
						int data;
						while ((data = input.read()) != -1) {
							output.write(data);
						}
						
						output.flush();
						output.close();
						input.close();
						skt.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	private class Receiver implements Runnable {

		private final Message message;
		private final SocketThread socket;

		public Receiver(Message m, SocketThread skt) {
			this.message = m;
			this.socket = skt;
		}
		
		@Override
		public void run() {
			//Open window
			
			//get user input
			
			Message m = new Message();
			m.setSender("Johan");
			m.setFileResponse("yes");
			m.setFileResponseMessage("d2");
			m.setFilePort(10101);
			
			// send message
			socket.send(m);
			// if yes
			if ( m.getFileResponse().equals("yes") ) {
				try {
					ServerSocket serverSkt = new ServerSocket(10101);
					Socket skt = serverSkt.accept();
					
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(message.getFileName()));
					BufferedInputStream input = new BufferedInputStream(new ProgressMonitorInputStream(null, "Transfering file", skt.getInputStream()));
					
					int data;
					while ( (data = input.read()) != -1) {
						output.write(data);
					}
					
					output.flush();
					output.close();
					input.close();
					skt.close();
					serverSkt.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
