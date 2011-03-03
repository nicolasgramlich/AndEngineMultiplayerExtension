package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPingServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 21:01:19 - 19.09.2009
 */
public interface IServerMessageHandler<T extends Connection> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final ServerConnector<T> pServerConnector, final IServerMessage pServerMessage) throws IOException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static abstract class DefaultServerMessageHandler<T extends Connection> implements ServerMessageFlags, IServerMessageHandler<T> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		/* Connection-Handlers */
		protected abstract void onHandleConnectionRefusedServerMessage(final ServerConnector<T> pServerConnector, final ConnectionRefusedServerMessage pConnectionRefusedServerMessage);

		protected abstract void onHandleConnectionAcceptedServerMessage(final ServerConnector<T> pServerConnector, final ConnectionAcceptedServerMessage pConnectionAcceptedServerMessage);

		protected void onHandleConnectionPingServerMessage(final ServerConnector<T> pServerConnector, final ConnectionPingServerMessage pConnectionPingServerMessage) throws IOException {
			pServerConnector.sendClientMessage(new ConnectionPongClientMessage(pConnectionPingServerMessage));
		}

		protected void onHandleConnectionPongServerMessage(final ServerConnector<T> pServerConnector, final ConnectionPongServerMessage pConnectionPongServerMessage) {

		}

		protected void onHandleConnectionCloseServerMessage(final ServerConnector<T> pServerConnector, final ConnectionCloseServerMessage pConnectionCloseServerMessage) {
			if(pServerConnector.hasConnectorListener()){
				pServerConnector.getConnectorListener().onDisconnected(pServerConnector);
			}
		}

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onHandleMessage(final ServerConnector<T> pServerConnector, final IServerMessage pServerMessage) throws IOException {
			switch(pServerMessage.getFlag()){
				case FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED:
					this.onHandleConnectionAcceptedServerMessage(pServerConnector, (ConnectionAcceptedServerMessage)pServerMessage);
					break;
				case FLAG_MESSAGE_SERVER_CONNECTION_REFUSED:
					this.onHandleConnectionRefusedServerMessage(pServerConnector, (ConnectionRefusedServerMessage)pServerMessage);
					break;
				case FLAG_MESSAGE_SERVER_CONNECTION_CLOSE:
					this.onHandleConnectionCloseServerMessage(pServerConnector, (ConnectionCloseServerMessage)pServerMessage);
					break;
				case FLAG_MESSAGE_SERVER_CONNECTION_PING:
					this.onHandleConnectionPingServerMessage(pServerConnector, (ConnectionPingServerMessage)pServerMessage);
					break;
				case FLAG_MESSAGE_SERVER_CONNECTION_PONG:
					this.onHandleConnectionPongServerMessage(pServerConnector, (ConnectionPongServerMessage)pServerMessage);
					break;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}