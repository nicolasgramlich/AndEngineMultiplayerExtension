package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 19:04:33 - 19.09.2009
 */
public class ConnectionAcceptedServerMessage extends BaseServerMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionAcceptedServerMessage() {
		/* Nothing to store. */
	}

	public ConnectionAcceptedServerMessage(final DataInputStream pInputStream) throws IOException {
		/* Nothing to read. */
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_SERVERMESSAGE_CONNECTION_ACCEPTED;
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		/* Nothing to write. */
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		/* Nothing to append. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
