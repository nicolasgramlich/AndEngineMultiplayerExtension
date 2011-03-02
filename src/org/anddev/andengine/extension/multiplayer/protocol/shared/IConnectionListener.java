package org.anddev.andengine.extension.multiplayer.protocol.shared;


/**
 * @author Nicolas Gramlich
 * @since 12:43:13 - 02.03.2011
 */
public interface IConnectionListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onConnected(final Connection pConnection); 
	public void onDisconnected(final Connection pConnection);
}
