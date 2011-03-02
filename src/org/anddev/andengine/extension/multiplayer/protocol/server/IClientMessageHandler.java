package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:02:16 - 19.09.2009
 */
public interface IClientMessageHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final ClientConnection pClientConnection, final BaseClientMessage pClientMessage) throws IOException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static class DefaultClientMessageHandler implements ClientMessageFlags, IClientMessageHandler {
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
		protected void onHandleConnectionEstablishClientMessage(final ClientConnection pClientConnection, final ConnectionEstablishClientMessage pClientMessage) throws IOException {
			if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION){
				pClientConnection.sendServerMessage(new ConnectionAcceptedServerMessage());
			}else{
				pClientConnection.sendServerMessage(new ConnectionRefusedServerMessage());
			}
		}

		protected void onHandleConnectionPingClientMessage(final ClientConnection pClientConnection, final ConnectionPingClientMessage pClientMessage) throws IOException {
			pClientConnection.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
		}

		protected void onHandleConnectionPongClientMessage(final ClientConnection pClientConnection, final ConnectionPongClientMessage pClientMessage) {

		}

		protected void onHandleConnectionCloseClientMessage(final ClientConnection pClientConnection, final ConnectionCloseClientMessage pClientMessage) throws IOException {
			pClientConnection.closeConnection();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onHandleMessage(final ClientConnection pClientConnection, final BaseClientMessage pClientMessage) throws IOException {
			switch(pClientMessage.getFlag()){
				case FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH:
					this.onHandleConnectionEstablishClientMessage(pClientConnection, (ConnectionEstablishClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE:
					this.onHandleConnectionCloseClientMessage(pClientConnection, (ConnectionCloseClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_PING:
					this.onHandleConnectionPingClientMessage(pClientConnection, (ConnectionPingClientMessage)pClientMessage);
					break;
				case FLAG_MESSAGE_CLIENT_CONNECTION_PONG:
					this.onHandleConnectionPongClientMessage(pClientConnection, (ConnectionPongClientMessage)pClientMessage);
					break;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}