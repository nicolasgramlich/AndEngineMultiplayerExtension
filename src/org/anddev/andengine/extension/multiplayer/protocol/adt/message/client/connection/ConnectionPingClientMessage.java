package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;

/**
 * @author Nicolas Gramlich
 * @since 17:51:32 - 21.09.2009
 */
public class ConnectionPingClientMessage extends BaseClientMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final long mTimestamp;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionPingClientMessage() {
		this(System.currentTimeMillis());
	}

	public ConnectionPingClientMessage(final long pTimestamp) {
		this.mTimestamp = pTimestamp;
	}

	public ConnectionPingClientMessage(final DataInputStream pInputStream) throws IOException {
		this.mTimestamp = pInputStream.readLong();
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
		return FLAG_CLIENTMESSAGE_CONNECTION_PING;
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
