package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ServerConnection extends BaseConnection<BaseServerMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ServerConnection(final Socket pSocket, final BaseServerConnectionListener pServerConnectionListener, final ServerMessageReader pServerMessageReader, final IServerMessageSwitch pServerMessageSwitch) throws IOException {
		super(pSocket, pServerConnectionListener, pServerMessageReader, pServerMessageSwitch);

		/* Initiate communication with the server,
		 * by sending a ConnectionEstablishClientMessage
		 * which contains the Protocol version. */
		this.sendClientMessage(new ConnectionEstablishClientMessage());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public IServerMessageSwitch getMessageSwitch() {
		return (IServerMessageSwitch)super.getMessageSwitch();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void handleMessage(final BaseServerMessage pMessage) throws IOException {
		this.getMessageSwitch().switchMessage(this, pMessage);
	}

	@Override
	protected void onSendConnectionClose() {
		try {
			this.sendClientMessage(new ConnectionCloseClientMessage());
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void sendClientMessage(final BaseClientMessage pClientMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.getDataOutputStream();
		pClientMessage.transmit(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
