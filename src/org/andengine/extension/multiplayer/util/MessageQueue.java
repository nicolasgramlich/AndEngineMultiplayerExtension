package org.andengine.extension.multiplayer.util;

import org.andengine.extension.multiplayer.adt.message.IMessage;
import org.andengine.util.adt.queue.concurrent.PriorityBlockingAggregatorQueue;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:55:23 - 08.05.2013
 */
public class MessageQueue<M extends IMessage> extends PriorityBlockingAggregatorQueue<M> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MessageQueue(final boolean pFair) {
		super(pFair);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
