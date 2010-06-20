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

	private BaseClientConnector mClientConnector;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * The {@link BaseClientMessageSwitch} requires a
	 * {@link BaseClientConnector} to be set, so the
	 * {@link BaseClientMessageSwitch} can directly respond to
	 * {@link BaseClientMessage} from the client.
	 *
	 * You will need to call: {@link BaseClientMessageSwitch#setClientConnector(BaseClientConnector)} !
	 *
	 * @see BaseClientMessageSwitch#setClientConnector(BaseClientConnector)
	 *
	 */
	public BaseClientMessageSwitch() {
		this(null);
	}

	/**
	 * The {@link BaseClientMessageSwitch} requires a
	 * {@link BaseClientConnector} to be set, so the
	 * {@link BaseClientMessageSwitch} can directly respond to
	 * {@link BaseClientMessage} from the client.
	 *
	 * @param pClientConnector
	 */
	public BaseClientMessageSwitch(final BaseClientConnector pClientConnector) {
		this.mClientConnector = pClientConnector;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BaseClientConnector getClientConnector() {
		return this.mClientConnector;
	}

	/**
	 * The {@link BaseClientMessageSwitch} requires a
	 * {@link BaseClientConnector} to be set, so the
	 * {@link BaseClientMessageSwitch} can directly respond to
	 * {@link BaseClientMessage} from the client.
	 *
	 * @param pClientConnector
	 */
	public void setClientConnector(final BaseClientConnector pClientConnector) {
		this.mClientConnector = pClientConnector;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/* Connection-Handlers */
	protected void onHandleConnectionEstablishClientMessage(final ConnectionEstablishClientMessage pClientMessage) throws IOException {
		if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION){
			this.mClientConnector.sendServerMessage(new ConnectionAcceptedServerMessage());
		}else{
			this.mClientConnector.sendServerMessage(new ConnectionRefusedServerMessage());
		}
	}

	private void onHandleConnectionPingClientMessage(final ConnectionPingClientMessage pClientMessage) throws IOException {
		this.mClientConnector.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
	}

	protected void onHandleConnectionPongClientMessage(final ConnectionPongClientMessage pClientMessage) {

	}

	private void onHandleConnectionCloseClientMessage(final ConnectionCloseClientMessage pClientMessage) throws IOException {
		if(this.mClientConnector.hasConnectionListener()){
			this.mClientConnector.getConnectionListener().onDisconnect(this.mClientConnector);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andremote.protocol.IClientMessageSwitch#doSwitch(org.anddev.andremote.protocol.adt.cmd.AbstractClientMessage)
	 */
	public void doSwitch(final BaseClientMessage pClientMessage) throws IOException {
		/* Choose the correct handle method for pClientMessage. */
		switch(pClientMessage.getFlag()){
			case FLAG_CLIENTMESSAGE_CONNECTION_ESTABLISH:
				this.onHandleConnectionEstablishClientMessage((ConnectionEstablishClientMessage)pClientMessage);
				break;
			case FLAG_CLIENTMESSAGE_CONNECTION_CLOSE:
				this.onHandleConnectionCloseClientMessage((ConnectionCloseClientMessage)pClientMessage);
				break;
			case FLAG_CLIENTMESSAGE_CONNECTION_PING:
				this.onHandleConnectionPingClientMessage((ConnectionPingClientMessage)pClientMessage);
				break;
			case FLAG_CLIENTMESSAGE_CONNECTION_PONG:
				this.onHandleConnectionPongClientMessage((ConnectionPongClientMessage)pClientMessage);
				break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
