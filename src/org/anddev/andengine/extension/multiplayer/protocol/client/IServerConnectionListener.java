package org.anddev.andengine.extension.multiplayer.protocol.client;

import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IConnectionListener;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 13:47:15 - 02.03.2011
 */
public interface IServerConnectionListener extends IConnectionListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class DefaultServerConnectionListener implements IServerConnectionListener {
		@Override
		public void onConnected(Connection pConnection) {
			Debug.d("Accepted ServerConnection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}

		@Override
		public void onDisconnected(Connection pConnection) {
			Debug.d("Closed ServerConnection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}
	}
}
