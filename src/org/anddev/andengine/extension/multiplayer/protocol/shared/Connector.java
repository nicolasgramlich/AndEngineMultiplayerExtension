package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.util.ArrayList;

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
	protected ArrayList<IConnectorListener<? extends Connector<C>>> mConnectorListeners = new ArrayList<IConnectorListener<? extends Connector<C>>>();

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
		return this.mConnectorListeners != null;
	}

	public ArrayList<? extends IConnectorListener<? extends Connector<C>>> getConnectorListeners() {
		return this.mConnectorListeners;
	}

	protected void addConnectorListener(final IConnectorListener<? extends Connector<C>> pConnectorListener) {
		if(pConnectorListener != null) {
			this.mConnectorListeners.add(pConnectorListener);
		}
	}

	protected boolean removeConnectorListener(final IConnectorListener<? extends Connector<C>> pConnectorListener) {
		if(pConnectorListener == null) {
			return false;
		} else {
			return this.mConnectorListeners.remove(pConnectorListener);
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public void start() {
		this.getConnection().start();
	}
	
	public void terminate() {
		this.getConnection().interrupt();
	}

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
