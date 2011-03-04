package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener.DefaultClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.anddev.andengine.util.Debug;
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

	private final int mPort;
	private ServerSocket mServerSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServer(final int pPort) {
		this(pPort, new DefaultClientConnectorListener<SocketConnection>());
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener) {
		this(pPort, pClientConnectorListener, new DefaultSocketServerListener());
	}

	public SocketServer(final int pPort, final ISocketServerListener pSocketServerListener) {
		this(pPort, new DefaultClientConnectorListener<SocketConnection>(), pSocketServerListener);
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener, final ISocketServerListener pSocketServerListener) {
		super(pClientConnectorListener, pSocketServerListener);

		if (pPort < 0) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal port '< 0'.");
			this.mServerListener.onException(this, illegalArgumentException);
			throw illegalArgumentException;
		}else{
			this.mPort = pPort;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getPort() {
		return this.mPort;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract ClientConnector<SocketConnection> newClientConnector(final SocketConnection pSocketConnection) throws IOException;

	@Override
	protected void onInit() throws IOException {
		this.mServerSocket = ServerSocketFactory.getDefault().createServerSocket(this.mPort);
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

	public static interface ISocketServerListener extends IServerListener<Server<SocketConnection, ClientConnector<SocketConnection>>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public static class DefaultSocketServerListener implements ISocketServerListener {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			// ===========================================================
			// Methods for/from SuperClass/Interfaces
			// ===========================================================

			@Override
			public void onStarted(final Server<SocketConnection, ClientConnector<SocketConnection>> pSocketServer) {
				Debug.d("Server started on port: " + ((SocketServer)pSocketServer).getPort());
			}
			@Override
			public void onTerminated(final Server<SocketConnection, ClientConnector<SocketConnection>> pSocketServer) {
				Debug.d("Server terminated on port: " + ((SocketServer)pSocketServer).getPort());
			}
			@Override
			public void onException(final Server<SocketConnection, ClientConnector<SocketConnection>> pSocketServer, final Throwable pThrowable) {
				Debug.e(pThrowable);
			}

			// ===========================================================
			// Methods
			// ===========================================================

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}
	}
}
