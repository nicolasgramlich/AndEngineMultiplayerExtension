package org.anddev.andengine.extension.multiplayer.protocol.server;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.shared.MessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 15:26:29 - 18.09.2009
 */
public class ClientMessageReader<C extends Connection> extends MessageReader<C, IClientMessage> implements ClientMessageFlags, IClientMessageReader<C> {
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

	public static class DefaultClientMessageReader<C extends Connection> extends ClientMessageReader<C> implements IMessageHandler<C, IClientMessage> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public DefaultClientMessageReader() {
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class);
			this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PONG, ConnectionPongClientMessage.class);

			this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, this);
			this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, this);
			this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_PING, this);
			this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_PONG, this);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onHandleMessage(Connector<C> pConnector, IClientMessage pClientMessage) {
//			switch(pClientMessage.getFlag()){
//				case FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH:
//					this.onHandleConnectionEstablishClientMessage(pConnector, (ConnectionEstablishClientMessage)pClientMessage);
//					break;
//				case FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE:
//					this.onHandleConnectionCloseClientMessage(pConnector, (ConnectionCloseClientMessage)pClientMessage);
//					break;
//				case FLAG_MESSAGE_CLIENT_CONNECTION_PING:
//					this.onHandleConnectionPingClientMessage(pConnector, (ConnectionPingClientMessage)pClientMessage);
//					break;
//				case FLAG_MESSAGE_CLIENT_CONNECTION_PONG:
//					this.onHandleConnectionPongClientMessage(pConnector, (ConnectionPongClientMessage)pClientMessage);
//					break;
//			}
		}
		
//		protected void onHandleConnectionEstablishClientMessage(final ClientConnector<T> pClientConnector, final ConnectionEstablishClientMessage pClientMessage) throws IOException {
//			if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION){
//				pClientConnector.sendServerMessage(new ConnectionAcceptedServerMessage());
//			}else{
//				pClientConnector.sendServerMessage(new ConnectionRefusedServerMessage());
//			}
//		}
//
//		protected void onHandleConnectionPingClientMessage(final ClientConnector<T> pClientConnector, final ConnectionPingClientMessage pClientMessage) throws IOException {
//			pClientConnector.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
//		}
//
//		protected void onHandleConnectionPongClientMessage(final ClientConnector<T> pClientConnector, final ConnectionPongClientMessage pClientMessage) {
//
//		}
//
//		protected void onHandleConnectionCloseClientMessage(final ClientConnector<T> pClientConnector, final ConnectionCloseClientMessage pClientMessage) throws IOException {
//			pClientConnector.getConnection().close();
//		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
