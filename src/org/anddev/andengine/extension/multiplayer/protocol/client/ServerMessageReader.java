package org.anddev.andengine.extension.multiplayer.protocol.client;

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
public class ServerMessageReader extends BaseMessageReader<BaseServerMessage> implements ServerMessageFlags, IServerMessageReader {
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

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static class DefaultServerMessageReader extends ServerMessageReader {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public DefaultServerMessageReader() {
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED, ConnectionAcceptedServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REFUSED, ConnectionRefusedServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PING, ConnectionPingServerMessage.class);
			this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
