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

/**
 * Manages one connection. Sends and receives messages.
 * Parses XML with StAx.
 */
public class SocketThread {

    // registered listeners
	private ArrayList<SocketListener> _listeners = new ArrayList<SocketListener>();

    // the socket
	private Socket socket;
	private String name = null;

    // separate threads for i/o
	private final Thread reader;
	private final Thread writer;

    // queue for messages to be sent
	private volatile BlockingQueue<Message> msgQueue = new LinkedBlockingQueue<Message>();

	private volatile boolean isRunning = true;

	private volatile XMLReader input;
	private volatile XMLWriter output;
	
	private volatile BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();

	public SocketThread(Socket skt) {

        // contains a socket
		this.socket = skt;

        // contains a thread that accepts input streams
		this.reader = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

                    // xml wrapped stream
					input = new XMLReader(new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")));

				} catch (XMLStreamException | IOException e) {
                    // if something went wrong
					e.printStackTrace();
					return;
				}
				
				while (isRunning) {
					try {
                        // fires an event when a message comes in
                        // which object has a readMessage() method?
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

        // contains a thread which sends output streams
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

    /**
     * Register listener.
     * Custom event mechanism for incoming messages.
     */
	public synchronized void addEventListener(SocketListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(SocketListener listener) {
		_listeners.remove(listener);
	}

    /**
     * Creates a SocketEvent from this object.
     * Calls the handleSocketEvent() method in registered listeners.
     *
     * Event is empty, it only notifies that there is an incoming message.
     * Notified entities handle everything.
     */
	private synchronized void fireEvent() {
		SocketEvent event = new SocketEvent(this);
		Iterator<SocketListener> i = _listeners.iterator();

        // passes event to
		while (i.hasNext()) {
			(i.next()).handleSocketEvent(event);
		}
	}

    /**
     * Start reader and writer threads.
     */
	public void start() {
		if ( !reader.isAlive()) {
			reader.start();
			writer.start();
		}
	}

    /**
     * Interrupts and closes everything.
     */
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		if ( name != null ) {
			return name;
		} else {
			return socket.getInetAddress().getCanonicalHostName();
		}
	}
}
