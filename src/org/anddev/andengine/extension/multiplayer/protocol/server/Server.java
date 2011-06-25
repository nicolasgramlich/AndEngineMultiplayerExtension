package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.server.connector.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class Server<C extends Connection, CC extends ClientConnector<C>> extends Thread implements IClientConnectorListener<C> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected IServerListener<? extends Server<C, CC>> mServerListener;

	private final AtomicBoolean mRunning = new AtomicBoolean(false);
	private final AtomicBoolean mTerminated = new AtomicBoolean(false);

	protected final ArrayList<CC> mClientConnectors = new ArrayList<CC>();
	protected IClientConnectorListener<C> mClientConnectorListener;

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
		return this.mRunning.get();
	}

	public boolean isTerminated() {
		return this.mTerminated.get();
	}

	public IClientConnectorListener<C> getClientConnectorListener() {
		return this.mClientConnectorListener;
	}

	public void setClientConnectorListener(final IClientConnectorListener<C> pClientConnectorListener) {
		this.mClientConnectorListener = pClientConnectorListener;
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
	protected abstract void onException(Throwable pThrowable);

	@Override
	public void run() {
		try {
			this.onStart();

			this.mRunning.set(true);

			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);  // TODO What ThreadPriority makes sense here?

			/* Endless waiting for incoming clients. */
			while (!Thread.interrupted() && this.mRunning.get() && !this.mTerminated.get()) {
				try {
					final CC clientConnector = this.acceptClientConnector();
					clientConnector.addClientConnectorListener(this);
					clientConnector.addClientConnectorListener(this.mClientConnectorListener);

					/* Start the ClientConnector(-Thread) so it starts receiving messages. */
					clientConnector.start();
				} catch (final Throwable pThrowable) {
					this.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.onException(pThrowable);
		} finally {
			this.terminate();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.terminate();
		super.finalize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onConnected(final ClientConnector<C> pClientConnector) {
		this.mClientConnectors.add((CC) pClientConnector);
	}

	@Override
	public void onDisconnected(final ClientConnector<C> pClientConnector) {
		this.mClientConnectors.remove(pClientConnector);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void terminate() {
		if(!this.mTerminated.getAndSet(true)) {
			this.mRunning.set(false);

			try {
				/* First interrupt all Clients. */
				final ArrayList<CC> clientConnectors = this.mClientConnectors;
				for(int i = 0; i < clientConnectors.size(); i++) {
					clientConnectors.get(i).terminate();
				}
				clientConnectors.clear();
			} catch (final Exception e) {
				this.onException(e);
			}

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				Debug.e(e);
			}
			this.interrupt();

			this.onTerminate();
		}
	}

	public void sendBroadcastServerMessage(final IServerMessage pServerMessage) throws IOException {
		if(this.mRunning.get()) {
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
