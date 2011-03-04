package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.MessagePool;

import android.util.SparseArray;

/**
 * @author Nicolas Gramlich
 * @since 11:05:58 - 21.09.2009
 */
public abstract class MessageReader<C extends Connection, M extends IMessage> implements IMessageReader<C, M> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final MessagePool<M> mMessagePool = new MessagePool<M>();
	private final SparseArray<IMessageHandler<C, M>> mMessageHandlers = new SparseArray<IMessageHandler<C, M>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass) {
		this.mMessagePool.registerMessage(pFlag, pMessageClass);
	}

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, M> pMessageHandler) {
		this.mMessageHandlers.put(pFlag, pMessageHandler);
	}

	@Override
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass, final IMessageHandler<C, M> pMessageHandler) {
		this.registerMessage(pFlag, pMessageClass);
		this.registerMessageHandler(pFlag, pMessageHandler);
	}

	@Override
	public M readMessage(final DataInputStream pDataInputStream) throws IOException {
		final short flag = pDataInputStream.readShort();
		return this.mMessagePool.obtainMessage(flag, pDataInputStream);
	}

	@Override
	public void handleMessage(final Connector<C> pConnector, final M pMessage) {
		final IMessageHandler<C, M> messageHandler = this.mMessageHandlers.get(pMessage.getFlag());
		if(messageHandler != null) {
			messageHandler.onHandleMessage(pConnector, pMessage);
		}
	}

	@Override
	public void recycleMessage(final M pMessage) {
		this.mMessagePool.recycleMessage(pMessage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
