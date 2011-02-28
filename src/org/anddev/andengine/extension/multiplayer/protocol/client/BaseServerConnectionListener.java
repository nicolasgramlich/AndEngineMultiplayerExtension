package org.anddev.andengine.extension.multiplayer.protocol.client;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 01:00:11 - 20.09.2009
 */
public abstract class BaseServerConnectionListener extends BaseConnectionListener<BaseServerMessage, BaseConnection<BaseServerMessage>> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static class DefaultServerConnectionListener extends BaseServerConnectionListener {
		@Override
		protected void onConnected(final BaseConnection<BaseServerMessage> pConnection) {
			Debug.d("Accepted Server-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}

		@Override
		protected void onDisconnected(final BaseConnection<BaseServerMessage> pConnection) {
			Debug.d("Closed Server-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}
	}
}
