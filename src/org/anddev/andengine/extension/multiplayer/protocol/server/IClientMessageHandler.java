package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;

/**
 * @author Nicolas Gramlich
 * @since 21:02:16 - 19.09.2009
 */
public interface IClientMessageHandler<C extends Connection> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final ClientConnector<C> pClientConnector, final IClientMessage pClientMessage) throws IOException;
}