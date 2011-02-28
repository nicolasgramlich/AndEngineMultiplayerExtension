package org.anddev.andengine.extension.multiplayer.protocol.server;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 01:00:11 - 20.09.2009
 */
public abstract class BaseClientConnectionListener extends BaseConnectionListener<BaseClientMessage, BaseConnection<BaseClientMessage>> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static class DefaultClientConnectionListener extends BaseClientConnectionListener {
		@Override
		protected void onConnected(final BaseConnection<BaseClientMessage> pConnection) {
			Debug.d("Accepted Client-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}

		@Override
		protected void onDisconnected(final BaseConnection<BaseClientMessage> pConnection) {
			Debug.d("Closed Client-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
		}
	}
}
