package org.anddev.andengine.extension.multiplayer.protocol.client;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageReader;

/**
 * @author Nicolas Gramlich
 * @since 13:11:07 - 02.03.2011
 */
public interface IServerMessageReader<C extends Connection> extends IMessageReader<C, IServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
}
