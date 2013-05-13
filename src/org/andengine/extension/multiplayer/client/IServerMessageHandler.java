package org.andengine.extension.multiplayer.client;

import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.shared.Connection;
import org.andengine.extension.multiplayer.shared.IMessageHandler;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:01:19 - 19.09.2009
 */
public interface IServerMessageHandler<C extends Connection> extends IMessageHandler<C, ServerConnector<C>, IServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void onHandleMessage(final ServerConnector<C> pServerConnector, final IServerMessage pServerMessage) throws IOException;
}