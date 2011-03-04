package org.anddev.andengine.extension.multiplayer.protocol.client.connector;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 15:45:57 - 04.03.2011
 */
public class SocketServerConnector extends ServerConnector<SocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private SocketServerConnector(final SocketConnection pConnection, final ISocketServerConnectionConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
		super(pConnection, pSocketConnectionServerConnectorListener);
	}

	private SocketServerConnector(final SocketConnection pConnection, final IServerMessageReader<SocketConnection> pServerMessageReader, final ISocketServerConnectionConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
		super(pConnection, pServerMessageReader, pSocketConnectionServerConnectorListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface ISocketServerConnectionConnectorListener extends IServerConnectorListener<SocketConnection> {
		
	}
	
	public static class DefaultSocketConnectionServerConnectorListener implements ISocketServerConnectionConnectorListener {
		@Override
		public void onConnected(final ServerConnector<SocketConnection> pServerConnector) {
			Debug.d("Accepted Server-Connection from: '" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onDisconnected(final ServerConnector<SocketConnection> pServerConnector) {
			Debug.d("Closed Server-Connection from: '" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}
}
