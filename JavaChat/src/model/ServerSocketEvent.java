package model;

import java.util.EventObject;

public class ServerSocketEvent extends EventObject {
	/**
	 * Custom event fired by server socket.
	 */
	private static final long serialVersionUID = -7945730370782623418L;

	public ServerSocketEvent(Object source) {
		super(source);
	}
}
