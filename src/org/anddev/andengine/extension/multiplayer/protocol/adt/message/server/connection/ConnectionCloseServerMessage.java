package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 12:19:01 - 02.10.2009
 */
public class ConnectionCloseServerMessage extends BaseServerMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionCloseServerMessage() {
		/* Nothing to store. */
	}

	public ConnectionCloseServerMessage(final DataInputStream pInputStream) throws IOException {
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
		return FLAG_SERVERMESSAGE_CONNECTION_CLOSE;
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
