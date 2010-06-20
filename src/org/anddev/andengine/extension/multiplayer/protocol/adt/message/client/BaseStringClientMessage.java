package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author Nicolas Gramlich
 * @since 13:49:25 - 21.09.2009
 */
public abstract class BaseStringClientMessage extends BaseClientMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected String mString;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseStringClientMessage(final String pString) {
		this.mString = pString;
	}

	public BaseStringClientMessage(final DataInputStream pDataInputStream) throws IOException {
		this.mString = pDataInputStream.readUTF();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getString() {
		return this.mString;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getString()=").append('\"').append(this.getString()).append('\"');
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeUTF(this.getString());
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

		final BaseStringClientMessage other = (BaseStringClientMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getString() == other.getString();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
