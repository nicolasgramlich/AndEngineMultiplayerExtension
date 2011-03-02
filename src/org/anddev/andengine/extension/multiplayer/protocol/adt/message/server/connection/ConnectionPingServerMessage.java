package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 17:56:49 - 21.09.2009
 */
public class ConnectionPingServerMessage extends BaseServerMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private long mTimestamp;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionPingServerMessage() {
		this(System.currentTimeMillis());
	}

	public ConnectionPingServerMessage(final long pTimestamp) {
		this.mTimestamp = pTimestamp;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getTimestamp() {
		return this.mTimestamp;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_CONNECTION_PONG;
	}

	@Override
	public void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mTimestamp = pDataInputStream.readLong();
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeLong(this.mTimestamp);
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getTimestamp()=").append(this.mTimestamp);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final ConnectionPingServerMessage other = (ConnectionPingServerMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getTimestamp() == other.getTimestamp();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
