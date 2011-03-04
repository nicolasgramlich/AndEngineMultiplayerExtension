package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * @author Nicolas Gramlich
 * @since 11:57:21 - 04.03.2011
 */
public interface IMessageHandler<C extends Connection, CC extends Connector<C>, M extends IMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final CC pConnector, final M pMessage) throws IOException;
}
