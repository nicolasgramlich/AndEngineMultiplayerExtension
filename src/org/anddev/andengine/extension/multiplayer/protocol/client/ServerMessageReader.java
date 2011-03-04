package org.anddev.andengine.extension.multiplayer.protocol.client;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPingServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.shared.MessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 18:15:50 - 18.09.2009
 */
public class ServerMessageReader<C extends Connection> extends MessageReader<C, IServerMessage> implements ServerMessageFlags, IServerMessageReader<C> {
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

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static class DefaultServerMessageReader<C extends Connection> extends ServerMessageReader<C> implements IMessageHandler<C, IServerMessage> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public DefaultServerMessageReader() {
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED, ConnectionAcceptedServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REFUSED, ConnectionRefusedServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PING, ConnectionPingServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class);

			this.registerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED, this);
			this.registerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_REFUSED, this);
			this.registerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, this);
			this.registerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_PING, this);
			this.registerMessageHandler(FLAG_MESSAGE_SERVER_CONNECTION_PONG, this);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void onHandleMessage(final Connector<C> pConnector, final IServerMessage pServerMessage) {
//			switch(pServerMessage.getFlag()) {
//				case FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED:
//					this.onHandleConnectionAcceptedServerMessage(pConnector, (ConnectionAcceptedServerMessage) pServerMessage);
//					break;
//				case FLAG_MESSAGE_SERVER_CONNECTION_REFUSED:
//					this.onHandleConnectionRefusedServerMessage(pConnector, (ConnectionRefusedServerMessage) pServerMessage);
//					break;
//				case FLAG_MESSAGE_SERVER_CONNECTION_CLOSE:
//					this.onHandleConnectionCloseServerMessage(pConnector, (ConnectionCloseServerMessage) pServerMessage);
//					break;
//				case FLAG_MESSAGE_SERVER_CONNECTION_PING:
//					this.onHandleConnectionPingServerMessage(pConnector, (ConnectionPingServerMessage) pServerMessage);
//					break;
//				case FLAG_MESSAGE_SERVER_CONNECTION_PONG:
//					this.onHandleConnectionPongServerMessage(pConnector, (ConnectionPongServerMessage) pServerMessage);
//					break;
//			}
		}
		
//		protected abstract void onHandleConnectionRefusedServerMessage(final ServerConnector<T> pServerConnector, final ConnectionRefusedServerMessage pConnectionRefusedServerMessage);
//
//		protected abstract void onHandleConnectionAcceptedServerMessage(final ServerConnector<T> pServerConnector, final ConnectionAcceptedServerMessage pConnectionAcceptedServerMessage);
//
//		protected void onHandleConnectionPingServerMessage(final ServerConnector<T> pServerConnector, final ConnectionPingServerMessage pConnectionPingServerMessage) throws IOException {
//			pServerConnector.sendClientMessage(new ConnectionPongClientMessage(pConnectionPingServerMessage));
//		}
//
//		protected void onHandleConnectionPongServerMessage(final ServerConnector<T> pServerConnector, final ConnectionPongServerMessage pConnectionPongServerMessage) {
//
//		}
//
//		protected void onHandleConnectionCloseServerMessage(final ServerConnector<T> pServerConnector, final ConnectionCloseServerMessage pConnectionCloseServerMessage) {
//			if(pServerConnector.hasConnectorListener()){
//				pServerConnector.getConnectorListener().onDisconnected(pServerConnector);
//			}
//		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
