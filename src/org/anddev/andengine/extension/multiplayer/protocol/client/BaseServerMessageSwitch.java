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
	protected abstract void onHandleConnectionRefusedServerMessage(final ServerConnector pServerConnector, final ConnectionRefusedServerMessage pServerMessage);

	protected abstract void onHandleConnectionAcceptedServerMessage(final ServerConnector pServerConnector, final ConnectionAcceptedServerMessage pServerMessage);

	protected void onHandleConnectionPingServerMessage(final ServerConnector pServerConnector, final ConnectionPingServerMessage pServerMessage) throws IOException {
		pServerConnector.sendClientMessage(new ConnectionPongClientMessage(pServerMessage));
	}

	protected void onHandleConnectionPongServerMessage(final ServerConnector pServerConnector, final ConnectionPongServerMessage pServerMessage) {

	}

	protected void onHandleConnectionCloseServerMessage(final ServerConnector pServerConnector, final ConnectionCloseServerMessage pServerMessage) {
		if(pServerConnector.hasConnectionListener()){
			pServerConnector.getConnectionListener().onDisconnect(pServerConnector);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andremote.protocol.IServerMessageSwitch#doSwitch(org.anddev.andremote.protocol.adt.message.AbstractServerMessage)
	 */
	public void doSwitch(final ServerConnector pServerConnector, final BaseServerMessage pServerMessage) throws IOException {
		/* Choose the correct handle method for pServerMessage. */
		switch(pServerMessage.getFlag()){
			case FLAG_SERVERMESSAGE_CONNECTION_ACCEPTED:
				this.onHandleConnectionAcceptedServerMessage(pServerConnector, (ConnectionAcceptedServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_REFUSED:
				this.onHandleConnectionRefusedServerMessage(pServerConnector, (ConnectionRefusedServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_CLOSE:
				this.onHandleConnectionCloseServerMessage(pServerConnector, (ConnectionCloseServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_PING:
				this.onHandleConnectionPingServerMessage(pServerConnector, (ConnectionPingServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_PONG:
				this.onHandleConnectionPongServerMessage(pServerConnector, (ConnectionPongServerMessage)pServerMessage);
				break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
