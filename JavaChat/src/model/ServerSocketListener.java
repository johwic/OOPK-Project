package model;

/**
 * Interface for a listener class which accepts server socket events.
 * The handleServerSocketEvent() method is called directly by firing entity.
 */
public interface ServerSocketListener {
	public void handleServerSocketEvent(ServerSocketEvent e);
}
