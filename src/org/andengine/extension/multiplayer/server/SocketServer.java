package org.andengine.extension.multiplayer.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.andengine.extension.multiplayer.shared.SocketConnection;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.server.connector.ClientConnector.IClientConnectorListener;
import org.andengine.extension.multiplayer.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.andengine.extension.multiplayer.server.connector.SocketConnectionClientConnector.DefaultSocketConnectionClientConnectorListener;
import org.andengine.util.SocketUtils;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:55:09 - 03.03.2011
 */
public abstract class SocketServer<CC extends ClientConnector<SocketConnection>> extends Server<SocketConnection, CC> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int BIND_RETRY_COUNT = 25;
	private static final int BIND_RETRY_DELAY = 100;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mPort;
	private ServerSocket mServerSocket;

	private boolean mReuseAddress;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServer(final int pPort) {
		this(pPort, new DefaultSocketConnectionClientConnectorListener());
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener) {
		this(pPort, pClientConnectorListener, new DefaultSocketServerListener<CC>());
	}

	public SocketServer(final int pPort, final ISocketServerListener<CC> pSocketServerListener) {
		this(pPort, new DefaultSocketConnectionClientConnectorListener(), pSocketServerListener);
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener, final ISocketServerListener<CC> pSocketServerListener) {
		super(pClientConnectorListener, pSocketServerListener);

		if (pPort < 0) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal port '< 0'.");
			this.onException(illegalArgumentException);
			throw illegalArgumentException;
		} else {
			this.mPort = pPort;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getPort() {
		return this.mPort;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISocketServerListener<CC> getServerListener() {
		return (ISocketServerListener<CC>)super.getServerListener();
	}

	public void setSocketServerListener(final ISocketServerListener<CC> pSocketServerListener) {
		super.setServerListener(pSocketServerListener);
	}

	public void setReuseAddress(final boolean pReuseAddress) {
		this.mReuseAddress = pReuseAddress;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract CC newClientConnector(final SocketConnection pSocketConnection) throws IOException;

	@Override
	protected void onStart() throws IOException {
		/* Repeatedly try to bind (since when quickly terminating the starting a new server in sequence, blocks the TCP socket for a while): */
		for (int i = 0; i <= BIND_RETRY_COUNT; i++) {
			this.mServerSocket = new ServerSocket();

			try {
				if (this.mServerSocket.getReuseAddress() != this.mReuseAddress) {
					this.mServerSocket.setReuseAddress(this.mReuseAddress);
				}
			} catch (final SocketException e) {
				Debug.w(e);
			}

			try {
				this.mServerSocket.bind(new InetSocketAddress(this.mPort));
				break;
			} catch (final SocketException e) {
				if (i < BIND_RETRY_COUNT) {
					Debug.w(e);
				} else {
					throw e;
				}

				try {
					Thread.sleep(BIND_RETRY_DELAY);
				} catch (final InterruptedException ie) {
					Debug.w(ie);
				}
			}
		}

		this.getServerListener().onStarted(this);
	}

	@Override
	protected CC acceptClientConnector() throws IOException {
		/* Wait for an incoming connection. */
		final Socket clientSocket = this.mServerSocket.accept();

		/* Spawn a new ClientConnector, which send and receive data to and from the client. */
		return this.newClientConnector(new SocketConnection(clientSocket));
	}

	@Override
	protected void onTerminate() {
		SocketUtils.closeSocket(this.mServerSocket);

		this.getServerListener().onTerminated(this);
	}

	@Override
	protected void onException(final Throwable pThrowable) {
		this.getServerListener().onException(this, pThrowable);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISocketServerListener<CC extends ClientConnector<SocketConnection>> extends Server.IServerListener<SocketServer<CC>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onStarted(final SocketServer<CC> pSocketServer);

		@Override
		public void onTerminated(final SocketServer<CC> pSocketServer);

		@Override
		public void onException(final SocketServer<CC> pSocketServer, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultSocketServerListener<CC extends ClientConnector<SocketConnection>> implements ISocketServerListener<CC> {
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
			public void onStarted(final SocketServer<CC> pSocketServer) {
				Debug.d("SocketServer started on port: " + pSocketServer.getPort());
			}

			@Override
			public void onTerminated(final SocketServer<CC> pSocketServer) {
				Debug.d("SocketServer terminated on port: " + pSocketServer.getPort());
			}

			@Override
			public void onException(final SocketServer<CC> pServer, final Throwable pThrowable) {
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
