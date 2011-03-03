package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:02:16 - 19.09.2009
 */
public interface IClientMessageHandler<T extends Connection> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final ClientConnector<T> pClientConnector, final IClientMessage pClientMessage) throws IOException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static class DefaultClientMessageHandler<T extends Connection> implements ClientMessageFlags, IClientMessageHandler<T> {
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
		protected void onHandleConnectionEstablishClientMessage(final ClientConnector<T> pClientConnector, final ConnectionEstablishClientMessage pClientMessage) throws IOException {
			if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION){
				pClientConnector.sendServerMessage(new ConnectionAcceptedServerMessage());
			}else{
				pClientConnector.sendServerMessage(new ConnectionRefusedServerMessage());
			}
		}

		protected void onHandleConnectionPingClientMessage(final ClientConnector<T> pClientConnector, final ConnectionPingClientMessage pClientMessage) throws IOException {
			pClientConnector.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
		}

		protected void onHandleConnectionPongClientMessage(final ClientConnector<T> pClientConnector, final ConnectionPongClientMessage pClientMessage) {

		}

		protected void onHandleConnectionCloseClientMessage(final ClientConnector<T> pClientConnector, final ConnectionCloseClientMessage pClientMessage) throws IOException {
			pClientConnector.getConnection().close();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onHandleMessage(final ClientConnector<T> pClientConnector, final IClientMessage pClientMessage) throws IOException {
			switch(pClientMessage.getFlag()){
				case FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH:
					this.onHandleConnectionEstablishClientMessage(pClientConnector, (ConnectionEstablishClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE:
					this.onHandleConnectionCloseClientMessage(pClientConnector, (ConnectionCloseClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_PING:
					this.onHandleConnectionPingClientMessage(pClientConnector, (ConnectionPingClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_PONG:
					this.onHandleConnectionPongClientMessage(pClientConnector, (ConnectionPongClientMessage)pClientMessage);
					break;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}