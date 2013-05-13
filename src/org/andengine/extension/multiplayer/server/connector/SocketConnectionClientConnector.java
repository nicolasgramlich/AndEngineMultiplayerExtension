package org.andengine.extension.multiplayer.server.connector;

import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.server.IClientMessageReader;
import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:44:42 - 04.03.2011
 */
public class SocketConnectionClientConnector extends ClientConnector<SocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketConnectionClientConnector(final SocketConnection pSocketConnection) throws IOException {
		super(pSocketConnection);
	}

	public SocketConnectionClientConnector(final SocketConnection pSocketConnection, final IClientMessageReader<SocketConnection> pClientMessageReader) throws IOException {
		super(pSocketConnection, pClientMessageReader);
	}

	public SocketConnectionClientConnector(final SocketConnection pSocketConnection, final MessagePool<IServerMessage> pServerMessagePool) throws IOException {
		super(pSocketConnection, pServerMessagePool);
	}

	public SocketConnectionClientConnector(final SocketConnection pSocketConnection, final IClientMessageReader<SocketConnection> pClientMessageReader, final MessagePool<IServerMessage> pServerMessagePool) throws IOException {
		super(pSocketConnection, pClientMessageReader, pServerMessagePool);
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

	public static interface ISocketConnectionClientConnectorListener extends IClientConnectorListener<SocketConnection> {

	}

	public static class DefaultSocketConnectionClientConnectorListener implements ISocketConnectionClientConnectorListener {
		@Override
		public void onStarted(ClientConnector<SocketConnection> pClientConnector) {
			Debug.d("Accepted Client-Connection from: '" + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onTerminated(ClientConnector<SocketConnection> pClientConnector) {
			Debug.d("Closed Client-Connection from: '" + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}
}
