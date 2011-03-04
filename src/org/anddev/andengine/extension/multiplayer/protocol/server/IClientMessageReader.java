package org.anddev.andengine.extension.multiplayer.protocol.server;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageReader;

/**
 * @author Nicolas Gramlich
 * @since 13:39:29 - 02.03.2011
 */
public interface IClientMessageReader<C extends Connection> extends IMessageReader<C, IClientMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
}
