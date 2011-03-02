package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.BaseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionCloseServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientMessageReader.DefaultClientMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.IConnectionListener;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ClientConnection extends Connection {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IClientMessageReader mClientMessageReader;
	private final IClientMessageHandler mClientMessageHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClientConnection(final Socket pSocket, final IClientConnectionListener pClientConnectionListener,final IClientMessageHandler pClientMessageHandler) throws IOException {
		this(pSocket, pClientConnectionListener, new DefaultClientMessageReader(), pClientMessageHandler);
	}

	public ClientConnection(final Socket pSocket, final IClientConnectionListener pClientConnectionListener, final IClientMessageReader pClientMessageReader, final IClientMessageHandler pClientMessageHandler) throws IOException {
		super(pSocket, pClientConnectionListener);
		this.mClientMessageReader = pClientMessageReader;
		this.mClientMessageHandler = pClientMessageHandler;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IClientMessageHandler getClientMessageHandler() {
		return this.mClientMessageHandler;
	}

	public IClientMessageReader getClientMessageReader() {
		return this.mClientMessageReader;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void read(final DataInputStream pDataInputStream) throws IOException {
		final BaseClientMessage clientMessage = this.mClientMessageReader.readMessage(pDataInputStream);
		this.mClientMessageHandler.onHandleMessage(this, clientMessage);
		this.mClientMessageReader.recycleMessage(clientMessage);
	}

	@Override
	protected void onConnectionClosed() {
		try {
			this.sendServerMessage(new ConnectionCloseServerMessage());
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerClientMessage(final short pFlag, final Class<? extends BaseClientMessage> pClientMessageClass) {
		this.mClientMessageReader.registerMessage(pFlag, pClientMessageClass);
	}

	public void sendServerMessage(final BaseServerMessage pServerMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.getDataOutputStream();
		pServerMessage.transmit(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IClientConnectionListener extends IConnectionListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultClientConnectionListener implements IClientConnectionListener {
			@Override
			public void onConnected(final Connection pConnection) {
				Debug.d("Accepted Client-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
			}

			@Override
			public void onDisconnected(final Connection pConnection) {
				Debug.d("Closed Client-Connection from: '" + pConnection.getSocket().getRemoteSocketAddress() + "'");
			}
		}
	}

}
