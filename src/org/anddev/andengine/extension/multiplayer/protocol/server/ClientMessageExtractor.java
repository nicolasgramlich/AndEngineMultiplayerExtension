package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseMessageExtractor;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 15:26:29 - 18.09.2009
 */
public class ClientMessageExtractor extends BaseMessageExtractor<BaseClientMessage> implements ClientMessageFlags {
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

	@Override
	public BaseClientMessage readMessage(final DataInputStream pDataInputStream) throws IOException {
		final short pFlag = this.readFlag(pDataInputStream);
		switch(pFlag) {
			case FLAG_CLIENTMESSAGE_CONNECTION_ESTABLISH:
				return new ConnectionEstablishClientMessage(pDataInputStream);
			case FLAG_CLIENTMESSAGE_CONNECTION_CLOSE:
				return new ConnectionCloseClientMessage(pDataInputStream);
			case FLAG_CLIENTMESSAGE_CONNECTION_PING:
				return new ConnectionPingClientMessage(pDataInputStream);
			case FLAG_CLIENTMESSAGE_CONNECTION_PONG:
				return new ConnectionPongClientMessage(pDataInputStream);
			default:
				throw new IllegalArgumentException("Unknown flag: " + pFlag);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
