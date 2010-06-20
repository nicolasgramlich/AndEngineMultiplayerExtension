package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * @author Nicolas Gramlich
 * @since 11:05:58 - 21.09.2009
 */
public abstract class BaseMessageExtractor<T extends IMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public abstract T readMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException;

	// ===========================================================
	// Methods
	// ===========================================================

	public short readMessageFlag(final DataInputStream pDataInputStream) throws IOException {
		return pDataInputStream.readShort();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
