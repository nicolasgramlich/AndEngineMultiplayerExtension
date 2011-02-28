package org.anddev.andengine.extension.multiplayer.protocol.shared;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;


/**
 * @author Nicolas Gramlich
 * @param <K>
 * @since 00:37:18 - 20.09.2009
 */
public abstract class BaseConnectionListener<M extends IMessage, C extends BaseConnector<M>> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mDisconnectCalled = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onConnected(final C pConnector);

	protected abstract void onDisconnected(final C pConnector);

	// ===========================================================
	// Methods
	// ===========================================================

	public void onConnect(final C pConnector){
		this.onConnected(pConnector);
	}

	public void onDisconnect(final C pConnector){
		if(this.mDisconnectCalled == true) {
			return;
		}

		this.mDisconnectCalled = true;

		this.onDisconnected(pConnector);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
