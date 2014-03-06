package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

public class SocketThread {
	
	private ArrayList<SocketListener> _listeners = new ArrayList<SocketListener>();	
	private Socket socket;
	private final Thread reader;
	private final Thread writer;
	private volatile BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<Message>();
	private volatile boolean isRunning = true;
	private volatile XMLReader input;
	private volatile XMLWriter output;
	
	private volatile BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public SocketThread(Socket skt) {
		this.socket = skt;

		this.reader = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					input = new XMLReader(new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")));
				} catch (XMLStreamException | IOException e) {
					e.printStackTrace();
					return;
				}
				
				while (isRunning) {
					try {
						messages.put(input.readMessage());
						fireEvent();
					} catch (InterruptedException e) {
						System.out.println("Reader closed.");
						return;
					} catch (XMLStreamException e) {
						e.printStackTrace();
						return;
					}
				}
				
				return;
			}
		});
		
		this.writer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					output = new XMLWriter(new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")));
				} catch (XMLStreamException | IOException e) {
					e.printStackTrace();
					return;
				}
				
				while (true) {
					try {
						Message m = msgQueue.take();
						output.writeMessage(m);
					} catch (InterruptedException e) {
						System.out.println("Writer closed.");
						return;
					} catch (XMLStreamException e) {
						e.printStackTrace();
					}
				}
			}
		});
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
	
	public void start() {
		if ( !reader.isAlive()) {
			reader.start();
			writer.start();
		}
	}

	public synchronized void terminate() throws IOException {
		isRunning = false;
		reader.interrupt();
		writer.interrupt();
		input.close();
		output.close();
		socket.close();
		
		System.out.println("Socket " + socket.getInetAddress().toString() + " and streams closed.");
	}
	
	public synchronized void send(Message m) {
		this.msgQueue.add(m);
	}
	
	public synchronized Message getMessage() {
		try {
			return messages.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized Message takeMessageTimeout(int timeout) {
		try {
			return messages.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	@Override
	public String toString() {
		return socket.getInetAddress().getCanonicalHostName();
	}
}
