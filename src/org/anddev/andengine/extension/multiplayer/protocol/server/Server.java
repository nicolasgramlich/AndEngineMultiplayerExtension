package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.server.connector.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class Server<C extends Connection, CC extends ClientConnector<C>> extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected IServerListener<? extends Server<C, CC>> mServerListener;

	private boolean mRunning = false;
	private boolean mClosed = true;

	protected final ArrayList<CC> mClientConnectors = new ArrayList<CC>();
	protected final IClientConnectorListener<C> mClientConnectorListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Server(final IClientConnectorListener<C> pClientConnectorListener, final IServerListener<? extends Server<C, CC>> pServerListener) {
		this.mServerListener = pServerListener;
		this.mClientConnectorListener = pClientConnectorListener;

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
		return this.mClosed ;
	}
	
	public IServerListener<? extends Server<C, CC>> getServerListener() {
		return this.mServerListener;
	}
	
	protected void setServerListener(final IServerListener<? extends Server<C, CC>> pServerListener) {
		this.mServerListener = pServerListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onStart() throws IOException;
	protected abstract CC acceptClientConnector() throws IOException;
	protected abstract void onTerminate();
	protected abstract void onException(Throwable pPThrowable);

	@Override
	public void run() {
		this.mRunning = true;
		this.mClosed = false;
		try {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY); // TODO What ThreadPriority makes sense here?
			this.onStart();

			/* Endless waiting for incoming clients. */
			while (!Thread.interrupted()) {
				try {
					final CC clientConnector = this.acceptClientConnector();
					clientConnector.setClientConnectorListener(this.mClientConnectorListener);
					this.mClientConnectors.add(clientConnector);

					/* Start the ClientConnector(-Thread) so it starts receiving commands. */
					clientConnector.getConnection().start();
				} catch (final Throwable pThrowable) {
					this.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.onException(pThrowable);
		} finally {
			this.close();
		}
	}

	@Override
	public void interrupt() {
		this.close();

		super.interrupt();
	}

	@Override
	protected void finalize() throws Throwable {
		this.interrupt();
		super.finalize();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void close() {
		if(!this.mClosed) {
			this.mClosed = true;
			this.mRunning = false;

			try {
				/* First interrupt all Clients. */
				final ArrayList<CC> clientConnectors = this.mClientConnectors;
				for(int i = 0; i < clientConnectors.size(); i++) {
					clientConnectors.get(i).getConnection().interrupt();
				}
				clientConnectors.clear();
			} catch (final Exception e) {
				this.onException(e);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Debug.e(e);
			}
			this.onTerminate();
		}
	}

	public void sendBroadcastServerMessage(final IServerMessage pServerMessage) throws IOException {
		if(this.mRunning && !this.mClosed) {
			final ArrayList<CC> clientConnectors = this.mClientConnectors;
			for(int i = 0; i < clientConnectors.size(); i++) {
				try {
					clientConnectors.get(i).sendServerMessage(pServerMessage);
				} catch (final IOException e) {
					this.onException(e);
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IServerListener<S extends Server<?, ?>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		
		public void onStarted(final S pServer);
		public void onTerminated(final S pServer);
		public void onException(final S pServer, final Throwable pThrowable);
	}
}
