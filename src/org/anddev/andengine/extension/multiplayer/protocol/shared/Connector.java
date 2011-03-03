package org.anddev.andengine.extension.multiplayer.protocol.shared;

/**
 * @author Nicolas Gramlich
 * @since 13:51:22 - 03.03.2011
 */
public class Connector<T extends Connection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final T mConnection;
	protected final IConnectorListener<Connector<T>> mConnectorListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connector(final T pConnection, final IConnectorListener<Connector<T>> pConnectorListener) {
		this.mConnection = pConnection;
		this.mConnectorListener = pConnectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public T getConnection() {
		return this.mConnection;
	}

	public boolean hasConnectorListener() {
		return this.mConnectorListener != null;
	}

	public IConnectorListener<Connector<T>> getConnectorListener() {
		return this.mConnectorListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void start() {
		this.mConnection.start();
	}
	
	public void interrupt() {
		this.mConnection.interrupt();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IConnectorListener<T extends Connector<? extends Connection>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onConnected(final T pConnector);
		public void onDisconnected(final T pConnector);
	}
}
