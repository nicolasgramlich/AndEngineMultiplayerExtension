package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Nicolas Gramlich
 * @since 22:26:21 - 22.06.2010
 */
public abstract class EmptyServerMessage extends BaseServerMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EmptyServerMessage() {
		/* Nothing to store. */
	}

	public EmptyServerMessage(final DataInputStream pInputStream) throws IOException {
		/* Nothing to read. */
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

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
