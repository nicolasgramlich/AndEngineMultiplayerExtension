package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnection.IClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnection.IClientConnectionListener.DefaultClientConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class BaseServer<T extends ClientConnection> extends Thread implements ProtocolConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mServerPort;
	private ServerSocket mServerSocket;

	protected final ArrayList<T> mClientConnections = new ArrayList<T>();
	private final IClientConnectionListener mClientConnectionListener;
	private final IServerStateListener mServerStateListener;
	private boolean mRunning = false;
	private boolean mTerminated = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses {@link ProtocolConstants#SERVER_DEFAULT_PORT}
	 */
	public BaseServer() {
		this(SERVER_DEFAULT_PORT);
	}

	public BaseServer(final int pPort) {
		this(pPort, new DefaultClientConnectionListener());
	}

	public BaseServer(final IClientConnectionListener pClientConnectionListener) {
		this(SERVER_DEFAULT_PORT, pClientConnectionListener);
	}

	public BaseServer(final IServerStateListener pServerStateListener) {
		this(SERVER_DEFAULT_PORT, pServerStateListener);
	}

	public BaseServer(final int pPort, final IClientConnectionListener pClientConnectionListener) {
		this(pPort, pClientConnectionListener, new IServerStateListener.DefaultServerStateListener());
	}

	public BaseServer(final int pPort, final IServerStateListener pServerStateListener) {
		this(pPort, new DefaultClientConnectionListener(), pServerStateListener);
	}

	public BaseServer(final IClientConnectionListener pClientConnectionListener, final IServerStateListener pServerStateListener) {
		this(SERVER_DEFAULT_PORT, pClientConnectionListener, pServerStateListener);
	}

	public BaseServer(final int pPort, final IClientConnectionListener pClientConnectionListener, final IServerStateListener pServerStateListener) {
		this.mServerStateListener = pServerStateListener;

		if (pPort < 0) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal port '< 0'.");
			this.mServerStateListener.onException(illegalArgumentException);
			throw illegalArgumentException;
		}else{
			this.mServerPort = pPort;
		}

		this.mClientConnectionListener = pClientConnectionListener;

		this.initName();
	}

	private void initName() {
		this.setName(this.getClass().getName());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRunning() {
		return this.mRunning;
	}

	public boolean isTerminated() {
		return this.mTerminated ;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T newClientConnection(final Socket pClientSocket, final IClientConnectionListener pClientConnectionListener) throws Exception;

	@Override
	public void run() {
		this.mRunning = true;
		this.mTerminated = false;
		this.mServerStateListener.onStarted(this.mServerPort);
		try {
			/* The Thread accepting the Sockets may run at minor Priority. */
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			this.mServerSocket = ServerSocketFactory.getDefault().createServerSocket(this.mServerPort);

			/* Endless waiting for incoming clients. */
			while (!Thread.interrupted()) {
				try {
					/* Wait for an incoming connection. */
					final Socket clientSocket = this.mServerSocket.accept();

					/* Spawn a new ClientConnection, which send and receive data to and from the client. */
					final T clientConnection = this.newClientConnection(clientSocket, this.mClientConnectionListener);
					this.mClientConnections.add(clientConnection);

					/* Start the ClientConnection(-Thread) so it starts receiving commands. */
					clientConnection.start();
				}catch (final SocketException se){
					if(!se.getMessage().equals(SocketUtils.SOCKETEXCEPTION_MESSAGE_SOCKET_CLOSED) && !se.getMessage().equals(SocketUtils.SOCKETEXCEPTION_MESSAGE_SOCKET_IS_CLOSED)) {
						this.mServerStateListener.onException(se);
					}

					break;
				} catch (final Throwable pThrowable) {
					this.mServerStateListener.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.mServerStateListener.onException(pThrowable);
		} finally {
			this.mRunning = false;
			this.mTerminated = true;
			SocketUtils.closeSocket(this.mServerSocket);
			this.mServerStateListener.onTerminated(this.mServerPort);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.interrupt();
		super.finalize();
	}

	@Override
	public void interrupt() {
		try {
			this.mTerminated = true;

			super.interrupt();

			/* First interrupt all Clients. */
			final ArrayList<T> clientConnections = this.mClientConnections;
			for(int i = 0; i < clientConnections.size(); i++){
				clientConnections.get(i).interrupt();
			}

			clientConnections.clear();

			Thread.sleep(1000);

			SocketUtils.closeSocket(this.mServerSocket);
			this.mRunning = false;
		} catch (final Exception e) {
			this.mServerStateListener.onException(e);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void sendBroadcastServerMessage(final BaseServerMessage pServerMessage) throws IOException {
		if(this.mRunning == true && this.mTerminated == false) {
			final ArrayList<T> clientConnections = this.mClientConnections;
			for(int i = 0; i < clientConnections.size(); i++) {
				try {
					clientConnections.get(i).sendServerMessage(pServerMessage);
				} catch (IOException e) {
					this.mServerStateListener.onException(e);
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IServerStateListener {
		public void onStarted(final int pPort);
		public void onTerminated(final int pPort);
		public void onException(final Throwable pThrowable);

		public static class DefaultServerStateListener implements IServerStateListener {
			public void onStarted(final int pPort) {
				Debug.d("Server listening on Port: " + pPort);
			}
			public void onTerminated(final int pPort) {
				Debug.d("Server terminated on Port: " + pPort);
			}
			public void onException(final Throwable pThrowable) {
				Debug.e(pThrowable);
			}
		}
	}
}
