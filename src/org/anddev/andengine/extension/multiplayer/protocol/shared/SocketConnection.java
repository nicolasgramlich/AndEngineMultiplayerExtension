package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class SocketConnection extends Connection {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Socket mSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketConnection(final Socket pSocket) throws IOException {
		super(new DataInputStream(pSocket.getInputStream()), new DataOutputStream(pSocket.getOutputStream()));

		this.mSocket = pSocket;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Socket getSocket() {
		return this.mSocket;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onClosed() {
		/* Ensure Socket is really closed. */
		SocketUtils.closeSocket(this.mSocket);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
