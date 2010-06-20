package org.anddev.andengine.extension.multiplayer.protocol.client;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnectionListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnector;

/**
 * @author Nicolas Gramlich
 * @since 01:00:11 - 20.09.2009
 */
public abstract class BaseServerConnectionListener extends BaseConnectionListener<BaseServerMessage, BaseConnector<BaseServerMessage>> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static class DefaultServerConnectionListener extends BaseServerConnectionListener {
		@Override
		protected void onConnectInner(final BaseConnector<BaseServerMessage> pConnector) {
			System.out.println("Accepted Server-Connection from: '" + pConnector.getSocket().getRemoteSocketAddress() + "'");
		}

		@Override
		protected void onDisconnectInner(final BaseConnector<BaseServerMessage> pConnector) {
			System.err.println("Closed Server-Connection from: '" + pConnector.getSocket().getRemoteSocketAddress() + "'");
		}
	}
}
