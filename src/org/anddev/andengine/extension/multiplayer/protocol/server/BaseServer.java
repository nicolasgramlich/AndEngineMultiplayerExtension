package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class BaseServer<CC extends ClientConnection> extends Thread implements ProtocolConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mServerPort;
	private ServerSocket mServerSocket;

	protected final ArrayList<CC> mClientConnections = new ArrayList<CC>();
	private final BaseClientConnectionListener mClientConnectionListener;
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
		this(pPort, new BaseClientConnectionListener.DefaultClientConnectionListener());
	}

	public BaseServer(final BaseClientConnectionListener pClientConnectionListener) {
		this(SERVER_DEFAULT_PORT, pClientConnectionListener);
	}

	public BaseServer(final IServerStateListener pServerStateListener) {
		this(SERVER_DEFAULT_PORT, pServerStateListener);
	}

	public BaseServer(final int pPort, final BaseClientConnectionListener pClientConnectionListener) {
		this(pPort, pClientConnectionListener, new IServerStateListener.DefaultServerStateListener());
	}

	public BaseServer(final int pPort, final IServerStateListener pServerStateListener) {
		this(pPort, new BaseClientConnectionListener.DefaultClientConnectionListener(), pServerStateListener);
	}

	public BaseServer(final BaseClientConnectionListener pClientConnectionListener, final IServerStateListener pServerStateListener) {
		this(SERVER_DEFAULT_PORT, pClientConnectionListener, pServerStateListener);
	}

	public BaseServer(final int pPort, final BaseClientConnectionListener pClientConnectionListener, final IServerStateListener pServerStateListener) {
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

	protected abstract CC newClientConnection(final Socket pClientSocket, final BaseClientConnectionListener pClientConnectionListener) throws Exception;

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
					final CC clientConnection = this.newClientConnection(clientSocket, this.mClientConnectionListener);
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
			for(final ClientConnection cc : this.mClientConnections){
				cc.interrupt();
			}

			this.mClientConnections.clear();

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
			for(final ClientConnection cc : this.mClientConnections) {
				try{
					cc.sendServerMessage(pServerMessage);
				}catch(IOException e) {
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
