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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

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
		//FileWindow.createFileResponseWindow(this, m);
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
					JDialog dialog = new JDialog((JFrame) null, "File transfer request", false);
					
					JPanel pane = new JPanel();
					
					
					final JProgressBar progressbar = new JProgressBar(0, (int) file.length());
					progressbar.setValue(0);
					progressbar.setStringPainted(true);
					
					pane.add(progressbar);
					
					dialog.add(pane);
					dialog.setVisible(true);
					
					
					
					Socket skt;
					BufferedOutputStream output;
					BufferedInputStream input;
					
					try {
						skt = new Socket(ip, response.getFilePort());
						output = new BufferedOutputStream(skt.getOutputStream());
						input = new BufferedInputStream(new FileInputStream(this.file));
	
						int data;
						int i = 0;
						while ((data = input.read()) != -1) {
							output.write(data);
							i++;
							final int p = i;
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									progressbar.setValue(p);
								}
							});
							output.flush();
						}
						
						
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
			String msg = message.getSender() + " wish to send you a file.\nFile name: " + message.getFileName() + "\nFile size: " + (double) message.getFileSize() / (double) 1024 + " KB\nMessage: " + message.getFileMessage() + "\nDo you wish to receive it?";
			int choice = JOptionPane.showOptionDialog(null, msg, "File request received", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			String fileMsg = JOptionPane.showInputDialog(null, "Please enter a message:", "Enter a message", JOptionPane.PLAIN_MESSAGE);
			
			if ( choice == JOptionPane.YES_OPTION ) {
				String portStr = JOptionPane.showInputDialog(null, "Please enter a port:", "Enter a port", JOptionPane.PLAIN_MESSAGE);
				int port = Integer.parseInt(portStr);
				
				JDialog dialog = new JDialog((JFrame) null, "File transfer request", false);
				
				JPanel pane = new JPanel();
				
				
				JLabel portLabel = new JLabel("Port: ");
				final JProgressBar progressbar = new JProgressBar(0, message.getFileSize());
				progressbar.setValue(0);
				progressbar.setStringPainted(true);
				
				pane.add(progressbar);
				
				dialog.add(pane);
				dialog.setVisible(true);
				
				Message m = new Message();
				m.setSender("Johan");
				m.setFileResponse("yes");
				m.setFileResponseMessage(fileMsg);
				m.setFilePort(port);
				
				// send message
				socket.send(m);
				try {
					ServerSocket serverSkt = new ServerSocket(port);
					Socket skt = serverSkt.accept();
					
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(message.getFileName()));
					BufferedInputStream input = new BufferedInputStream(skt.getInputStream());
					
					int data;
					int i = 0;
					while ( (data = input.read()) != -1) {
						output.write(data);
						i++;
						final int p = i;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								progressbar.setValue(p);
							}
						});
					}
					
					output.flush();
					output.close();
					input.close();
					skt.close();
					serverSkt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Message m = new Message();
				m.setSender("Johan");
				m.setFileResponse("no");
				m.setFileResponseMessage(fileMsg);			
			}
		}

	}
}
