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
public class MessagePool<M extends IMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private MultiPool<M> mMessageMultiPool = new MultiPool<M>(); 

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass) {
		this.mMessageMultiPool.registerPool(pFlag, 
			new GenericPool<M>() {
				@Override
				protected M onAllocatePoolItem() {
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

	public M obtainMessage(final short pFlag) {
		return this.mMessageMultiPool.obtainPoolItem(pFlag);
	}
	
	public M obtainMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
		final M message = this.mMessageMultiPool.obtainPoolItem(pFlag);
		message.read(pDataInputStream);
		return message;
	}

	public void recycleMessage(final M pMessage) {
		this.mMessageMultiPool.recyclePoolItem(pMessage.getFlag(), pMessage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
