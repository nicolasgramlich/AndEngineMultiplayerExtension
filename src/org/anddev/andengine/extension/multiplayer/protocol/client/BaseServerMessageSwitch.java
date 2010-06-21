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

	private ServerConnector mServerConnector;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * The {@link AbstractServerMessageSwitch} requires a
	 * {@link ServerConnector} to be set, so the
	 * {@link AbstractServerMessageSwitch} can directly respond to
	 * {@link BaseServerMessage} from the server.
	 *
	 * You will need to call: {@link AbstractServerMessageSwitch#setConnector(ServerConnector)} !
	 *
	 * @see AbstractServerMessageSwitch#setConnector(ServerConnector)
	 *
	 */
	public BaseServerMessageSwitch() {
		this(null);
	}

	/**
	 * The {@link AbstractServerMessageSwitch} requires a
	 * {@link ServerConnector} to be set, so the
	 * {@link AbstractServerMessageSwitch} can directly respond to
	 * {@link BaseServerMessage} from the server.
	 *
	 * @param pServerConnector
	 */
	public BaseServerMessageSwitch(final ServerConnector pServerConnector) {
		this.mServerConnector = pServerConnector;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ServerConnector getServerConnector() {
		return this.mServerConnector;
	}

	/**
	 * The {@link AbstractServerMessageSwitch} requires a
	 * {@link ServerConnector} to be set, so the
	 * {@link AbstractServerMessageSwitch} can directly respond to
	 * {@link BaseServerMessage} from the server.
	 *
	 * @param pServerConnector
	 */
	public void setServerConnector(final ServerConnector pServerConnector) {
		this.mServerConnector = pServerConnector;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/* Connection-Handlers */
	protected abstract void onHandleConnectionRefusedServerMessage(final ConnectionRefusedServerMessage pServerMessage);

	protected abstract void onHandleConnectionAcceptedServerMessage(final ConnectionAcceptedServerMessage pServerMessage);

	private void onHandleConnectionPingServerMessage(final ConnectionPingServerMessage pServerMessage) throws IOException {
		this.mServerConnector.sendClientMessage(new ConnectionPongClientMessage(pServerMessage));
	}

	protected void onHandleConnectionPongServerMessage(final ConnectionPongServerMessage pServerMessage) {

	}

	private void onHandleConnectionCloseServerMessage(final ConnectionCloseServerMessage pServerMessage) {
		if(this.mServerConnector.hasConnectionListener()){
			this.mServerConnector.getConnectionListener().onDisconnect(this.mServerConnector);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/* (non-Javadoc)
	 * @see org.anddev.andremote.protocol.IServerMessageSwitch#doSwitch(org.anddev.andremote.protocol.adt.message.AbstractServerMessage)
	 */
	public void doSwitch(final BaseServerMessage pServerMessage) throws IOException {
		/* Choose the correct handle method for pServerMessage. */
		switch(pServerMessage.getFlag()){
			case FLAG_SERVERMESSAGE_CONNECTION_ACCEPTED:
				this.onHandleConnectionAcceptedServerMessage((ConnectionAcceptedServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_REFUSED:
				this.onHandleConnectionRefusedServerMessage((ConnectionRefusedServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_CLOSE:
				this.onHandleConnectionCloseServerMessage((ConnectionCloseServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_PING:
				this.onHandleConnectionPingServerMessage((ConnectionPingServerMessage)pServerMessage);
				break;
			case FLAG_SERVERMESSAGE_CONNECTION_PONG:
				this.onHandleConnectionPongServerMessage((ConnectionPongServerMessage)pServerMessage);
				break;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
