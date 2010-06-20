package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:26:16 - 19.09.2009
 */
public class ConnectionEstablishClientMessage extends BaseClientMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mProtocolVersion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConnectionEstablishClientMessage() {
		this(ProtocolConstants.PROTOCOL_VERSION);
	}

	public ConnectionEstablishClientMessage(final int pProtocolVersion) {
		this.mProtocolVersion = pProtocolVersion;
	}

	public ConnectionEstablishClientMessage(final DataInputStream pInputStream) throws IOException {
		this.mProtocolVersion = pInputStream.readInt();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getProtocolVersion() {
		return this.mProtocolVersion;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_CLIENTMESSAGE_CONNECTION_ESTABLISH;
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mProtocolVersion);
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getProtocolVersion()=").append(this.mProtocolVersion);
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

		final ConnectionEstablishClientMessage other = (ConnectionEstablishClientMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getProtocolVersion() == other.getProtocolVersion();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
