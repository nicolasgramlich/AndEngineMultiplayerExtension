package org.anddev.andengine.extension.multiplayer.protocol.adt.message.server;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.BaseMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 18:15:42 - 18.09.2009
 */
public abstract class BaseServerMessage extends BaseMessage implements ServerMessageFlags {
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

		final BaseServerMessage other = (BaseServerMessage) obj;

		return this.getFlag() == other.getFlag();
	}


	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
