package org.anddev.andengine.extension.multiplayer.protocol.util;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.pool.GenericPool;
import org.anddev.andengine.util.pool.MultiPool;

/**
 * @author Nicolas Gramlich
 * @since 11:33:23 - 02.03.2011
 */
public class MessagePool<T extends IMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private MultiPool<T> mMessageMultiPool = new MultiPool<T>(); 

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public void registerMessage(final short pFlag, final Class<? extends T> pMessageClass) {
		this.mMessageMultiPool.registerPool(pFlag, 
			new GenericPool<T>() {
				@Override
				protected T onAllocatePoolItem() {
					try {
						return pMessageClass.newInstance();
					} catch (Throwable t) {
						Debug.e(t);
						return null;
					}
				}
			}
		);
	}

	public T obtainMessage(final short pFlag) {
		return this.mMessageMultiPool.obtainPoolItem(pFlag);
	}
	
	public T obtainMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
		final T message = this.mMessageMultiPool.obtainPoolItem(pFlag);
		message.read(pDataInputStream);
		return message;
	}

	public void recycleMessage(final T pMessage) {
		this.mMessageMultiPool.recyclePoolItem(pMessage.getFlag(), pMessage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
