package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;

/**
 * @author Nicolas Gramlich
 * @since 13:39:29 - 02.03.2011
 */
public interface IClientMessageReader {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass);
	public IClientMessage readMessage(final DataInputStream pDataInputStream) throws IOException;
	public void recycleMessage(final IClientMessage pIClientMessage) throws IOException;
}
