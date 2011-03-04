package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

/**
 * @author Nicolas Gramlich
 * @since 17:51:32 - 21.09.2009
 */
public class ConnectionPingClientMessage extends ClientMessage {
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

	@Deprecated
	public ConnectionPingClientMessage() {
		
	}

	public ConnectionPingClientMessage(final long pTimestamp) {
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
		return FLAG_MESSAGE_CLIENT_CONNECTION_PING;
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

		final ConnectionPingClientMessage other = (ConnectionPingClientMessage) obj;

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
