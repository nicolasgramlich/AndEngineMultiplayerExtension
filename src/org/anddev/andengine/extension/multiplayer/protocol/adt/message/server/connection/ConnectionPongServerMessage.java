package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 17:56:49 - 21.09.2009
 */
public class ConnectionPongServerMessage extends BaseServerMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private long mOriginalPingTimestamp;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionPongServerMessage(final ConnectionPingClientMessage pClientMessage) {
		this(pClientMessage.getTimestamp());
	}

	public ConnectionPongServerMessage(final long pTimestamp) {
		this.mOriginalPingTimestamp = pTimestamp;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getOriginalPingTimestamp() {
		return this.mOriginalPingTimestamp;
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
		this.mOriginalPingTimestamp = pDataInputStream.readLong();
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeLong(this.mOriginalPingTimestamp);
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getOriginalPingTimestamp()=").append(this.mOriginalPingTimestamp);
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

		final ConnectionPongServerMessage other = (ConnectionPongServerMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getOriginalPingTimestamp() == other.getOriginalPingTimestamp();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
