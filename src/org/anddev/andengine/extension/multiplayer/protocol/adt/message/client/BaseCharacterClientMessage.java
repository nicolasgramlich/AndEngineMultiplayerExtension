package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author Nicolas Gramlich
 * @since 13:38:26 - 19.09.2009
 */
public abstract class BaseCharacterClientMessage extends BaseClientMessage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected char mCharacter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseCharacterClientMessage(final char pCharacter) {
		this.mCharacter = pCharacter;
	}

	public BaseCharacterClientMessage(final DataInputStream pDataInputStream) throws IOException {
		this.read(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public char getCharacter() {
		return this.mCharacter;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.mCharacter = pDataInputStream.readChar();
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getCharacter()=").append('\'').append(this.getCharacter()).append('\'');
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeChar(this.getCharacter());
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

		final BaseCharacterClientMessage other = (BaseCharacterClientMessage) obj;

		return this.getFlag() == other.getFlag()
		&& this.getCharacter() == other.getCharacter();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
