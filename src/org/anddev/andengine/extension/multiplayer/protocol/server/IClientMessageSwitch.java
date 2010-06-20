package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageSwitch;

/**
 * @author Nicolas Gramlich
 * @since 21:02:16 - 19.09.2009
 *
 * You might consider using {@link BaseClientMessageSwitch}.
 *
 * @see BaseClientMessageSwitch
 */
public interface IClientMessageSwitch extends IMessageSwitch<BaseClientMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void doSwitch(final BaseClientMessage pClientMessage) throws IOException;
}