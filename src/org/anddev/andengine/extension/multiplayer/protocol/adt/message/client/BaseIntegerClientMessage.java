package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author Nicolas Gramlich
 * @since 13:38:26 - 19.09.2009
 */
public abstract class BaseIntegerClientMessage extends BaseClientMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mInteger;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseIntegerClientMessage(final int pInteger) {
		this.mInteger = pInteger;
	}

	public BaseIntegerClientMessage(final DataInputStream pDataInputStream) throws IOException {
		this.read(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getInteger() {
		return this.mInteger;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.mInteger = pDataInputStream.readInt();
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getInteger()=").append(this.getInteger());
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.getInteger());
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

		final BaseIntegerClientMessage other = (BaseIntegerClientMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getInteger() == other.getInteger();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
