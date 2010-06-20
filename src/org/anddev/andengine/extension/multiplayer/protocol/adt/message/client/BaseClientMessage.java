package org.anddev.andengine.extension.multiplayer.protocol.adt.message.client;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.BaseMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 18:30:28 - 19.09.2009
 */
public abstract class BaseClientMessage extends BaseMessage implements ClientMessageFlags {
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

		final BaseClientMessage other = (BaseClientMessage) obj;

		return this.getFlag() == other.getFlag();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
