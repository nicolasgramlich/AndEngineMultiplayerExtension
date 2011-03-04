package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * @author Nicolas Gramlich
 * @since 11:50:53 - 04.03.2011
 */
public interface IMessageReader<C extends Connection, M extends IMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass);
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, M> pMessageHandler);
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass, final IMessageHandler<C, M> pMessageHandler);
	
	public M readMessage(final DataInputStream pDataInputStream) throws IOException;
	
	public void handleMessage(final Connector<C> pConnector, final M pMessage);
	
	public void recycleMessage(final M pMessage);
}
