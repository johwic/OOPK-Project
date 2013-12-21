package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerSocketThread implements Runnable {

	private ArrayList<ServerSocketListener> _listeners = new ArrayList<ServerSocketListener>();
	private ServerSocket ssock;
	private final int port;
	private volatile boolean isRunning = true;

	public Socket clientSocket;

	public ServerSocketThread(int port) {
		this.port = port;
	}
	
	public synchronized void addEventListener(ServerSocketListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(ServerSocketListener listener) {
		_listeners.remove(listener);
	}

	private synchronized void fireEvent() {
		ServerSocketEvent event = new ServerSocketEvent(this);
		Iterator<ServerSocketListener> i = _listeners.iterator();
		while (i.hasNext()) {
			(i.next()).handleServerSocketEvent(event);
		}
	}

	public void terminate() {
		isRunning = false;
	}

	@Override
	public void run() {
		try {
			ssock = new ServerSocket(port);
		} catch (IOException ioe) {
			System.out
					.println("Could not create server socket on port " + port + ". Quitting.");
			System.exit(-1);
		}
		
		while (isRunning) {
			try {
				clientSocket = ssock.accept();
				fireEvent();
			} catch (IOException ioe) {
				System.out
						.println("Exception encountered on accept. Ignoring. Stack Trace :");
				ioe.printStackTrace();
			}
		}

		try {
			ssock.close();
			System.out.println("Server Stopped");
		} catch (Exception ioe) {
			System.out.println("Problem stopping server socket");
			System.exit(-1);
		}
	}
}
