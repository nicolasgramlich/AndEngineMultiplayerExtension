package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageHandler;

/**
 * @author Nicolas Gramlich
 * @since 21:01:19 - 19.09.2009
 */
public interface IServerMessageHandler<C extends Connection> extends IMessageHandler<C, IServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final ServerConnector<C> pServerConnector, final IServerMessage pServerMessage) throws IOException;
}