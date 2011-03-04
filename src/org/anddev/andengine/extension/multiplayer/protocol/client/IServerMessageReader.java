package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.DataInputStream;
import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPingServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.MessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ServerMessageFlags;

/**
 * @author Nicolas Gramlich
 * @since 13:11:07 - 02.03.2011
 */
public interface IServerMessageReader<C extends Connection> extends IMessageReader<C, ServerConnector<C>, IServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass);

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass, final IMessageHandler<C, ServerConnector<C>, IServerMessage> pServerMessageHandler);

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, ServerConnector<C>, IServerMessage> pServerMessageHandler);

	@Override
	public IServerMessage readMessage(final DataInputStream pDataInputStream) throws IOException;

	@Override
	public void handleMessage(final ServerConnector<C> pServerConnector, final IServerMessage pServerMessage) throws IOException;

	@Override
	public void recycleMessage(final IServerMessage pServerMessage);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class ServerMessageReader<C extends Connection> extends MessageReader<C, ServerConnector<C>, IServerMessage> implements ServerMessageFlags, IServerMessageReader<C> {
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

		public static class DefaultServerMessageReader<C extends Connection> extends ServerMessageReader<C> implements IServerMessageHandler<C> {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			public DefaultServerMessageReader() {
				this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED, ConnectionAcceptedServerMessage.class, this);
				this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REFUSED, ConnectionRefusedServerMessage.class, this);
				this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, this);
				this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PING, ConnectionPingServerMessage.class, this);
				this.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class, this);
			}

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			// ===========================================================
			// Methods for/from SuperClass/Interfaces
			// ===========================================================

			@Override
			public void onHandleMessage(final ServerConnector<C> pServerConnector, final IServerMessage pServerMessage) throws IOException {
				switch(pServerMessage.getFlag()) {
					case FLAG_MESSAGE_SERVER_CONNECTION_ACCEPTED:
						this.onHandleConnectionAcceptedServerMessage(pServerConnector, (ConnectionAcceptedServerMessage) pServerMessage);
						break;
					case FLAG_MESSAGE_SERVER_CONNECTION_REFUSED:
						this.onHandleConnectionRefusedServerMessage(pServerConnector, (ConnectionRefusedServerMessage) pServerMessage);
						break;
					case FLAG_MESSAGE_SERVER_CONNECTION_CLOSE:
						this.onHandleConnectionCloseServerMessage(pServerConnector, (ConnectionCloseServerMessage) pServerMessage);
						break;
					case FLAG_MESSAGE_SERVER_CONNECTION_PING:
						this.onHandleConnectionPingServerMessage(pServerConnector, (ConnectionPingServerMessage) pServerMessage);
						break;
					case FLAG_MESSAGE_SERVER_CONNECTION_PONG:
						this.onHandleConnectionPongServerMessage(pServerConnector, (ConnectionPongServerMessage) pServerMessage);
						break;
				}
			}

			// ===========================================================
			// Methods
			// ===========================================================

			protected void onHandleConnectionRefusedServerMessage(final ServerConnector<C> pServerConnector, final ConnectionRefusedServerMessage pConnectionRefusedServerMessage) {

			}

			protected void onHandleConnectionAcceptedServerMessage(final ServerConnector<C> pServerConnector, final ConnectionAcceptedServerMessage pConnectionAcceptedServerMessage) {

			}

			protected void onHandleConnectionPingServerMessage(final ServerConnector<C> pServerConnector, final ConnectionPingServerMessage pConnectionPingServerMessage) throws IOException {
				pServerConnector.sendClientMessage(new ConnectionPongClientMessage(pConnectionPingServerMessage)); // TODO Eventually add Pooling here
			}

			protected void onHandleConnectionPongServerMessage(final ServerConnector<C> pServerConnector, final ConnectionPongServerMessage pConnectionPongServerMessage) {

			}

			protected void onHandleConnectionCloseServerMessage(final ServerConnector<C> pServerConnector, final ConnectionCloseServerMessage pConnectionCloseServerMessage) {
				if(pServerConnector.hasConnectorListener()){
					pServerConnector.getConnectorListener().onDisconnected(pServerConnector);
				}
			}

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}
	}
}
