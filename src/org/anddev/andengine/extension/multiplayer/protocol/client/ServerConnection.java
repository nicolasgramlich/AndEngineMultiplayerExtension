package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler.DefaultServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.ServerMessageReader.DefaultServerMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ServerConnection extends Connection {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IServerMessageReader mServerMessageReader;
	private final IServerMessageHandler mServerMessageHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ServerConnection(final Socket pSocket, final IServerConnectionListener pServerConnectionListener, final DefaultServerMessageHandler pServerMessageHandler) throws IOException {
		this(pSocket, pServerConnectionListener, new DefaultServerMessageReader(), pServerMessageHandler);
	}

	public ServerConnection(final Socket pSocket, final IServerConnectionListener pServerConnectionListener, final IServerMessageReader pServerMessageReader, final IServerMessageHandler pServerMessageHandler) throws IOException {
		super(pSocket, pServerConnectionListener);
		this.mServerMessageReader = pServerMessageReader;
		this.mServerMessageHandler = pServerMessageHandler;

		/* Initiate communication with the server,
		 * by sending a ConnectionEstablishClientMessage
		 * which contains the Protocol version. */
		this.sendClientMessage(new ConnectionEstablishClientMessage());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IServerMessageReader getServerMessageReader() {
		return this.mServerMessageReader;
	}

	public IServerMessageHandler getServerMessageHandler() {
		return this.mServerMessageHandler;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void read(final DataInputStream pDataInputStream) throws IOException {
		final BaseServerMessage serverMessage = this.mServerMessageReader.readMessage(pDataInputStream);
		this.mServerMessageHandler.onHandleMessage(this, serverMessage);
		this.mServerMessageReader.recycleMessage(serverMessage);
	}

	@Override
	protected void onConnectionClosed() {
		try {
			this.sendClientMessage(new ConnectionCloseClientMessage());
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerServerMessage(final short pFlag, final Class<? extends BaseServerMessage> pServerMessageClass) {
		this.mServerMessageReader.registerMessage(pFlag, pServerMessageClass);
	}

	public void sendClientMessage(final BaseClientMessage pClientMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.getDataOutputStream();
		pClientMessage.transmit(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
