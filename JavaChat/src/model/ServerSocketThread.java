package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerSocketThread implements Runnable {

	private ArrayList<ServerSocketListener> _listeners = new ArrayList<ServerSocketListener>();
	private volatile ServerSocket serverSocket;
	private final int port;

	private Socket socket;

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

	public synchronized void terminate() {
		try {
			serverSocket.close();
			System.out.println("ServerSocket listening on port " + port + " closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Socket getSocket() {
		return this.socket;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not create server socket on port " + port + ". Port already in use?");
			return;
		}
		
		while (true) {
			try {
				socket = serverSocket.accept();
				fireEvent();
			} catch (SocketException e) {
				return;
			} catch (IOException e) {
				System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
				e.printStackTrace();
			}
		}
	}
}
