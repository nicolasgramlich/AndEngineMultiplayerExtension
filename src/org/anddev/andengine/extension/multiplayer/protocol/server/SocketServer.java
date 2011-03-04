package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener.DefaultClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:55:09 - 03.03.2011
 */
public abstract class SocketServer extends Server<SocketConnection, ClientConnector<SocketConnection>> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mServerPort;
	private ServerSocket mServerSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServer(final int pPort) {
		this(pPort, new DefaultClientConnectorListener<SocketConnection>());
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener) {
		this(pPort, pClientConnectorListener, new IServerStateListener.DefaultServerStateListener());
	}

	public SocketServer(final int pPort, final IServerStateListener pServerStateListener) {
		this(pPort, new DefaultClientConnectorListener<SocketConnection>(), pServerStateListener);
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener, final IServerStateListener pServerStateListener) {
		super(pClientConnectorListener, pServerStateListener);

		if (pPort < 0) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal port '< 0'.");
			this.mServerStateListener.onException(illegalArgumentException);
			throw illegalArgumentException;
		}else{
			this.mServerPort = pPort;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract ClientConnector<SocketConnection> newClientConnector(final SocketConnection pSocketConnection) throws IOException;

	@Override
	protected void onInit() throws IOException {
		this.mServerSocket = ServerSocketFactory.getDefault().createServerSocket(this.mServerPort);
	}

	@Override
	protected ClientConnector<SocketConnection> acceptClientConnector() throws IOException {
		/* Wait for an incoming connection. */
		final Socket clientSocket = this.mServerSocket.accept();

		/* Spawn a new ClientConnector, which send and receive data to and from the client. */
		return this.newClientConnector(new SocketConnection(clientSocket));
	}

	@Override
	public void onClosed() {
		SocketUtils.closeSocket(this.mServerSocket);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
