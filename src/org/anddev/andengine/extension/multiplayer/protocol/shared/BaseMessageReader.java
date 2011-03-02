package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.MessagePool;

/**
 * @author Nicolas Gramlich
 * @since 11:05:58 - 21.09.2009
 */
public abstract class BaseMessageReader<T extends IMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	final MessagePool<T> mMessagePool = new MessagePool<T>();

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

	public void registerMessage(final short pFlag, final Class<? extends T> pMessageClass) {
		this.mMessagePool.registerMessage(pFlag, pMessageClass);
	}

	public T readMessage(final DataInputStream pDataInputStream) throws IOException {
		final short flag = pDataInputStream.readShort();
		return this.mMessagePool.obtainMessage(flag, pDataInputStream);
	}

	public void recycleMessage(T pMessage) {
		this.mMessagePool.recycleMessage(pMessage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
