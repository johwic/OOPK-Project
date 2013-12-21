package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class SocketThread implements Runnable {
	
	private ArrayList<SocketListener> _listeners = new ArrayList<SocketListener>();	
	private Socket socket;
	private volatile boolean isRunning = true;
	
	public String clientCommand;

	public SocketThread(Socket socket) {
		this.socket = socket;
	}
	
	public synchronized void addEventListener(SocketListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(SocketListener listener) {
		_listeners.remove(listener);
	}

	private synchronized void fireEvent() {
		SocketEvent event = new SocketEvent(this);
		Iterator<SocketListener> i = _listeners.iterator();
		while (i.hasNext()) {
			(i.next()).handleSocketEvent(event);
		}
	}	

	public void terminate() {
		isRunning = false;
	}

	@Override
	public void run() {
		// Obtain the input stream and the output stream for the socket
		// A good practice is to encapsulate them with a BufferedReader
		// and a PrintWriter as shown below.
		BufferedReader in = null;
		PrintWriter out = null;

		// Print out details of this connection
		System.out.println("Accepted Client Address - "
				+ socket.getInetAddress().getHostName());

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			// At this point, we can read for input and reply with appropriate
			// output.

			// Run in a loop until m_bRunThread is set to false
			while (isRunning) {
				// read incoming stream
				clientCommand = in.readLine();
				
				fireEvent();
				
				System.out.println("Client Says :" + clientCommand);

				out.println("Server Says : " + clientCommand);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
				System.out.println("Socket closed.");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
