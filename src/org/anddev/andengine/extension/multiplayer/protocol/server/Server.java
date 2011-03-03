package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 14:36:54 - 18.09.2009
 */
public abstract class Server<K extends Connection, T extends ClientConnector<K>> extends Thread implements ProtocolConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IServerStateListener mServerStateListener;

	private boolean mRunning = false;
	private boolean mClosed = true;

	protected final ArrayList<T> mClientConnectors = new ArrayList<T>();
	protected final IClientConnectorListener<K> mClientConnectorListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Server(final IClientConnectorListener<K> pClientConnectorListener, final IServerStateListener pServerStateListener) {
		this.mServerStateListener = pServerStateListener;
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onInit() throws IOException;
	protected abstract void onClosed();
	protected abstract T acceptClientConnector() throws IOException;

	@Override
	public void run() {
		this.mRunning = true;
		this.mClosed = false;
		this.mServerStateListener.onStarted();
		try {
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY); // TODO What ThreadPriority makes sense here?
			this.onInit();

			/* Endless waiting for incoming clients. */
			while (!Thread.interrupted()) {
				try {
					final T clientConnector = this.acceptClientConnector();
					clientConnector.setClientConnectorListener(this.mClientConnectorListener);
					this.mClientConnectors.add(clientConnector);

					/* Start the ClientConnector(-Thread) so it starts receiving commands. */
					clientConnector.getConnection().start();
				} catch (final Throwable pThrowable) {
					this.mServerStateListener.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.mServerStateListener.onException(pThrowable);
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
				final ArrayList<T> clientConnectors = this.mClientConnectors;
				for(int i = 0; i < clientConnectors.size(); i++) {
					clientConnectors.get(i).getConnection().interrupt();
				}
				clientConnectors.clear();
				
                Thread.sleep(1000);

				this.mServerStateListener.onTerminated();
			} catch (final Exception e) {
				this.mServerStateListener.onException(e);
			}
			
			this.onClosed();
		}
	}

	public void sendBroadcastServerMessage(final IServerMessage pServerMessage) throws IOException {
		if(this.mRunning && !this.mClosed) {
			final ArrayList<T> clientConnectors = this.mClientConnectors;
			for(int i = 0; i < clientConnectors.size(); i++) {
				try {
					clientConnectors.get(i).sendServerMessage(pServerMessage);
				} catch (final IOException e) {
					this.mServerStateListener.onException(e);
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IServerStateListener {
		public void onStarted();
		public void onTerminated();
		public void onException(final Throwable pThrowable);

		public static class DefaultServerStateListener implements IServerStateListener {
			@Override
			public void onStarted() {
				Debug.d("Server started.");
			}
			@Override
			public void onTerminated() {
				Debug.d("Server terminated.");
			}
			@Override
			public void onException(final Throwable pThrowable) {
				Debug.e(pThrowable);
			}
		}
	}
}
