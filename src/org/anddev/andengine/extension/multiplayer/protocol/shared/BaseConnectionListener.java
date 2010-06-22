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

	protected abstract void onConnectInner(final C pConnector);

	protected abstract void onDisconnectInner(final C pConnector);

	// ===========================================================
	// Methods
	// ===========================================================

	public void onConnect(final C pConnector){
		this.onConnectInner(pConnector);
	}

	public void onDisconnect(final C pConnector){
		if(this.mDisconnectCalled == true) {
			return;
		}

		this.mDisconnectCalled = true;

		this.onDisconnectInner(pConnector);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
