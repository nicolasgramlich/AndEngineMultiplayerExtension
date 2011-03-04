package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.MessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;

/**
 * @author Nicolas Gramlich
 * @since 13:39:29 - 02.03.2011
 */
public interface IClientMessageReader<C extends Connection> extends IMessageReader<C, ClientConnector<C>, IClientMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass);

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass, final IMessageHandler<C, ClientConnector<C>, IClientMessage> pClientMessageHandler);

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, ClientConnector<C>, IClientMessage> pClientMessageHandler);

	@Override
	public IClientMessage readMessage(final DataInputStream pDataInputStream) throws IOException;

	@Override
	public void handleMessage(final ClientConnector<C> pClientConnector, final IClientMessage pClientMessage) throws IOException;

	@Override
	public void recycleMessage(final IClientMessage pClientMessage);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class ClientMessageReader<C extends Connection> extends MessageReader<C, ClientConnector<C>, IClientMessage> implements ClientMessageFlags, IClientMessageReader<C> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultClientMessageReader<C extends Connection> extends ClientMessageReader<C> implements IClientMessageHandler<C> {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			public DefaultClientMessageReader() {
				this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class);
				this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class);
				this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class);
				this.registerMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PONG, ConnectionPongClientMessage.class);

				this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, this);
				this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, this);
				this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_PING, this);
				this.registerMessageHandler(FLAG_MESSAGE_CLIENT_CONNECTION_PONG, this);
			}

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			// ===========================================================
			// Methods for/from SuperClass/Interfaces
			// ===========================================================

			@Override
			public void onHandleMessage(final ClientConnector<C> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				switch(pClientMessage.getFlag()){
					case FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH:
						this.onHandleConnectionEstablishClientMessage(pClientConnector, (ConnectionEstablishClientMessage)pClientMessage);
						break;
					case FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE:
						this.onHandleConnectionCloseClientMessage(pClientConnector, (ConnectionCloseClientMessage)pClientMessage);
						break;
					case FLAG_MESSAGE_CLIENT_CONNECTION_PING:
						this.onHandleConnectionPingClientMessage(pClientConnector, (ConnectionPingClientMessage)pClientMessage);
						break;
					case FLAG_MESSAGE_CLIENT_CONNECTION_PONG:
						this.onHandleConnectionPongClientMessage(pClientConnector, (ConnectionPongClientMessage)pClientMessage);
						break;
				}
			}

			// ===========================================================
			// Methods
			// ===========================================================

			protected void onHandleConnectionEstablishClientMessage(final ClientConnector<C> pClientConnector, final ConnectionEstablishClientMessage pClientMessage) throws IOException {
				if(pClientMessage.getProtocolVersion() == ProtocolConstants.PROTOCOL_VERSION) {
					pClientConnector.sendServerMessage(new ConnectionAcceptedServerMessage());
				} else {
					pClientConnector.sendServerMessage(new ConnectionRefusedServerMessage());
				}
			}

			protected void onHandleConnectionPingClientMessage(final ClientConnector<C> pClientConnector, final ConnectionPingClientMessage pClientMessage) throws IOException {
				pClientConnector.sendServerMessage(new ConnectionPongServerMessage(pClientMessage));
			}

			protected void onHandleConnectionPongClientMessage(final ClientConnector<C> pClientConnector, final ConnectionPongClientMessage pClientMessage) {

			}

			protected void onHandleConnectionCloseClientMessage(final ClientConnector<C> pClientConnector, final ConnectionCloseClientMessage pClientMessage) throws IOException {
				pClientConnector.getConnection().close();
			}

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}
	}

}
