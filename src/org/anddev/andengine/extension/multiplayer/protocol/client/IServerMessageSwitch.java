package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BaseConnector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageSwitch;

/**
 * @author Nicolas Gramlich
 * @since 21:01:19 - 19.09.2009
 *
 * You might consider using {@link BaseServerMessageSwitch}.
 *
 * @see BaseServerMessageSwitch
 */
public interface IServerMessageSwitch extends IMessageSwitch<BaseServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void doSwitch(final BaseServerMessage pServerMessage) throws IOException;

	public void setConnector(final BaseConnector<BaseServerMessage> pServerConnector);
}