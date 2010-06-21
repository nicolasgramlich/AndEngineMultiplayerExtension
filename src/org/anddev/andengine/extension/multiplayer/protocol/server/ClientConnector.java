package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnector;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ClientConnector extends BaseConnector<BaseClientMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClientConnector(final Socket pSocket, final BaseClientConnectionListener pConnectionListener, final ClientMessageExtractor pClientMessageExtractor, final BaseClientMessageSwitch pClientMessageSwitch) throws IOException {
		super(pSocket, pConnectionListener, pClientMessageExtractor, pClientMessageSwitch);
		
		pClientMessageSwitch.setClientConnector(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public IClientMessageSwitch getMessageSwitch() {
		return (IClientMessageSwitch)super.getMessageSwitch();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSendConnectionClose() {
		try {
			this.sendServerMessage(new ConnectionCloseServerMessage());
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void sendServerMessage(final BaseServerMessage pServerMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.getDataOutputStream();
		pServerMessage.transmit(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
