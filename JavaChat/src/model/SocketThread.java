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
	
	public Message message;

	public SocketThread(Socket skt) throws XMLStreamException, IOException {
		this.socket = skt;

		this.reader = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					input = new XMLReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
				} catch (XMLStreamException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while (isRunning) {
					message = input.readMessage();
					fireEvent();
				}
				
				return;
			}
		});
		
		this.writer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					output = new XMLWriter(new PrintWriter(new OutputStreamWriter(socket.getOutputStream())));
				} catch (XMLStreamException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (isRunning) {
					try {
						Message m = msgQueue.take();
						output.writeMessage(m);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				return;
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
		reader.start();
		writer.start();
	}

	public void terminate() throws IOException {
		isRunning = false;
		input.close();
		output.close();
		socket.close();
	}
	
	public synchronized void send(Message m) {
		this.msgQueue.add(m);
	}
}
