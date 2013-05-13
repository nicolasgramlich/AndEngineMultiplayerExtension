package org.andengine.extension.multiplayer.server;

import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.shared.Connection;
import org.andengine.extension.multiplayer.shared.IMessageHandler;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:02:16 - 19.09.2009
 */
public interface IClientMessageHandler<C extends Connection> extends IMessageHandler<C, ClientConnector<C>, IClientMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void onHandleMessage(final ClientConnector<C> pClientConnector, final IClientMessage pClientMessage) throws IOException;
}