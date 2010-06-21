package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * @author Nicolas Gramlich
 * @since 21:36:23 - 19.09.2009
 */
public interface IMessageSwitch<T extends IMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void doSwitch(final T pMessage) throws IOException;

	public void setConnector(final BaseConnector<T> pConnector);
}
