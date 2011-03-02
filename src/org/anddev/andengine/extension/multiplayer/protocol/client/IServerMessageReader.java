package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;

/**
 * @author Nicolas Gramlich
 * @since 13:11:07 - 02.03.2011
 */
public interface IServerMessageReader {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass);
	public IServerMessage readMessage(final DataInputStream pDataInputStream) throws IOException;
	public void recycleMessage(final IServerMessage pIServerMessage) throws IOException;
}
