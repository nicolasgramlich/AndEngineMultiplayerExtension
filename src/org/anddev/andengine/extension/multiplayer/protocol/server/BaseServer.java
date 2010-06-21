package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class BaseServer extends Thread implements ProtocolConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mServerPort;
	private ServerSocket mServerSocket;

	private final List<ClientConnector> mClientConnectors = new ArrayList<ClientConnector>();
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
			this.mServerStateListener.onException(new IllegalArgumentException("Illegal port '< 0'."));
			throw new IllegalArgumentException();
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

	protected abstract ClientConnector newClientConnector(final Socket pClientSocket, final BaseClientConnectionListener pClientConnectionListener) throws Exception;

	@Override
	public void run() {
		this.mServerStateListener.onStarted(this.mServerPort);
		this.mRunning = true;
		try {
			/* The Thread accepting the Sockets may run at minor Priority. */
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			this.mServerSocket = ServerSocketFactory.getDefault().createServerSocket(this.mServerPort);

			/* Endless waiting for incoming clients. */
			while (!Thread.interrupted()) {
				try {
					/* Wait for an incoming connection. */
					final Socket clientSocket = this.mServerSocket.accept();

					/* Spawn a new ClientConnector, which send and receive data to and from the client. */
					final ClientConnector cc = this.newClientConnector(clientSocket, this.mClientConnectionListener);
					this.mClientConnectors.add(cc);

					/* Start the ClientConnector(-Thread) so it starts receiving commands. */
					cc.start();
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
			for(final ClientConnector cc : this.mClientConnectors){
				cc.interrupt();
			}

			this.mClientConnectors.clear();

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
			for(final ClientConnector cc : this.mClientConnectors) {
				cc.sendServerMessage(pServerMessage);
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
				Debug.d("AndRemote-Server is listening on Port: " + pPort);
			}
			public void onTerminated(final int pPort) {
				Debug.d("AndRemote-Server terminated on Port: " + pPort);
			}
			public void onException(final Throwable pThrowable) {
				Debug.e(pThrowable);
			}
		}
	}
}
