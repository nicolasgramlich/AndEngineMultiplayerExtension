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
 * @since 21:05:06 - 18.09.2009
 */
public abstract class BaseClientMessageSwitch implements ClientMessageFlags, IClientMessageSwitch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseClientMessageSwitch() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/* Connection-Handlers */
	protected void onHandleConnectionEstablishClientMessage(final ClientConnector pClientConnector, final ConnectionEstablishClientMessage pClientMessage) throws IOException {
		if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION){
			pClientConnector.sendServerMessage(new ConnectionAcceptedServerMessage());
		}else{
			pClientConnector.sendServerMessage(new ConnectionRefusedServerMessage());
		}
	}

	protected void onHandleConnectionPingClientMessage(final ClientConnector pClientConnector, final ConnectionPingClientMessage pClientMessage) throws IOException {
		pClientConnector.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
	}

	protected void onHandleConnectionPongClientMessage(final ClientConnector pClientConnector, final ConnectionPongClientMessage pClientMessage) {

	}

	protected void onHandleConnectionCloseClientMessage(final ClientConnector pClientConnector, final ConnectionCloseClientMessage pClientMessage) throws IOException {
		if(pClientConnector.hasConnectionListener()){
			pClientConnector.getConnectionListener().onDisconnect(pClientConnector);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andengine.extension.multiplayer.protocol.server.IClientMessageSwitch#doSwitch(org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector, org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage)
	 */
	@Override
	public void doSwitch(final ClientConnector pClientConnector, final BaseClientMessage pClientMessage) throws IOException {
		/* Choose the correct handle method for pClientMessage. */
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
