package model;

/**
 * Interface for classes which accept socket events.
 * handleSocketEvent() method is called directly by firing entity.
 */
public interface SocketListener {
	public void handleSocketEvent(SocketEvent e);
}
