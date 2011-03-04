package org.anddev.andengine.extension.multiplayer.protocol.shared;

import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection.IConnectionListener;

/**
 * @author Nicolas Gramlich
 * @since 13:51:22 - 03.03.2011
 */
public abstract class Connector<C extends Connection> implements IConnectionListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final C mConnection;
	protected IConnectorListener<? extends Connector<C>> mConnectorListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connector(final C pConnection) {
		this.mConnection = pConnection;
		this.mConnection.setConnectionListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public C getConnection() {
		return this.mConnection;
	}

	public boolean hasConnectorListener() {
		return this.mConnectorListener != null;
	}

	public IConnectorListener<? extends Connector<C>> getConnectorListener() {
		return this.mConnectorListener;
	}

	protected void setConnectorListener(final IConnectorListener<? extends Connector<C>> pConnectorListener) {
		this.mConnectorListener = pConnectorListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IConnectorListener<C extends Connector<?>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onConnected(final C pConnector);
		public void onDisconnected(final C pConnector);
	}
}
