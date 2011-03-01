package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPingServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 18:15:50 - 18.09.2009
 */
public class ServerMessageReader extends BaseMessageReader<BaseServerMessage> implements ServerMessageFlags {
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
	public BaseServerMessage readMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
		switch(pFlag) {
			case FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED:
				return new ConnectionAcceptedServerMessage(pDataInputStream);
			case FLAG_MESSAGE_SERVER_CONNECTION_REFUSED:
				return new ConnectionRefusedServerMessage(pDataInputStream);
			case FLAG_MESSAGE_SERVER_CONNECTION_CLOSE:
				return new ConnectionCloseServerMessage(pDataInputStream);
			case FLAG_MESSAGE_SERVER_CONNECTION_PING:
				return new ConnectionPingServerMessage(pDataInputStream);
			case FLAG_MESSAGE_SERVER_CONNECTION_PONG:
				return new ConnectionPongServerMessage(pDataInputStream);
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
