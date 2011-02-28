package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPingServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 19:40:24 - 19.09.2009
 */
public abstract class BaseServerMessageSwitch implements ServerMessageFlags, IServerMessageSwitch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseServerMessageSwitch() {

	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/* Connection-Handlers */
	protected abstract void onHandleConnectionRefusedServerMessage(final ServerConnection pServerConnection, final ConnectionRefusedServerMessage pConnectionRefusedServerMessage);

	protected abstract void onHandleConnectionAcceptedServerMessage(final ServerConnection pServerConnection, final ConnectionAcceptedServerMessage pConnectionAcceptedServerMessage);

	protected void onHandleConnectionPingServerMessage(final ServerConnection pServerConnection, final ConnectionPingServerMessage pConnectionPingServerMessage) throws IOException {
		pServerConnection.sendClientMessage(new ConnectionPongClientMessage(pConnectionPingServerMessage));
	}

	protected void onHandleConnectionPongServerMessage(final ServerConnection pServerConnection, final ConnectionPongServerMessage pConnectionPongServerMessage) {

	}

	protected void onHandleConnectionCloseServerMessage(final ServerConnection pServerConnection, final ConnectionCloseServerMessage pConnectionCloseServerMessage) {
		if(pServerConnection.hasConnectionListener()){
			pServerConnection.getConnectionListener().onDisconnect(pServerConnection);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageSwitch#doSwitch(org.anddev.andengine.extension.multiplayer.protocol.client.ServerConnection, org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage)
	 */
	@Override
	public void doSwitch(final ServerConnection pServerConnection, final BaseServerMessage pServerMessage) throws IOException {
		/* Choose the correct handle method for pServerMessage. */
		switch(pServerMessage.getFlag()){
			case FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED:
				this.onHandleConnectionAcceptedServerMessage(pServerConnection, (ConnectionAcceptedServerMessage)pServerMessage);
				break;
			case FLAG_MESSAGE_SERVER_CONNECTION_REFUSED:
				this.onHandleConnectionRefusedServerMessage(pServerConnection, (ConnectionRefusedServerMessage)pServerMessage);
				break;
			case FLAG_MESSAGE_SERVER_CONNECTION_CLOSE:
				this.onHandleConnectionCloseServerMessage(pServerConnection, (ConnectionCloseServerMessage)pServerMessage);
				break;
			case FLAG_MESSAGE_SERVER_CONNECTION_PING:
				this.onHandleConnectionPingServerMessage(pServerConnection, (ConnectionPingServerMessage)pServerMessage);
				break;
			case FLAG_MESSAGE_SERVER_CONNECTION_PONG:
				this.onHandleConnectionPongServerMessage(pServerConnection, (ConnectionPongServerMessage)pServerMessage);
				break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
