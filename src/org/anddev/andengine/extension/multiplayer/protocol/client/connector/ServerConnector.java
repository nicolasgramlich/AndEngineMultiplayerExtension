package org.anddev.andengine.extension.multiplayer.protocol.client.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.client.IServerMessageReader.ServerMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connection;
import org.anddev.andengine.extension.multiplayer.protocol.shared.Connector;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ServerConnector<C extends Connection> extends Connector<C> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IServerMessageReader<C> mServerMessageReader;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ServerConnector(final C pConnection, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		this(pConnection, new ServerMessageReader<C>(), pServerConnectorListener);
	}

	public ServerConnector(final C pConnection, final IServerMessageReader<C> pServerMessageReader, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		super(pConnection);
		this.mServerMessageReader = pServerMessageReader;
		this.addServerConnectorListener(pServerConnectorListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IServerMessageReader<C> getServerMessageReader() {
		return this.mServerMessageReader;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<IServerConnectorListener<C>> getConnectorListeners() {
		return (ArrayList<IServerConnectorListener<C>>) super.getConnectorListeners();
	}

	public void addServerConnectorListener(final IServerConnectorListener<C> pServerConnectorListener) {
		super.addConnectorListener(pServerConnectorListener);
	}

	public boolean removeServerConnectorListener(final IServerConnectorListener<C> pServerConnectorListener) {
		return super.removeConnectorListener(pServerConnectorListener);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onConnected(final Connection pConnection) {
		final ArrayList<IServerConnectorListener<C>> serverConnectorListeners = this.getConnectorListeners();
		final int connectorListenerCount = serverConnectorListeners.size();
		for(int i = 0; i < connectorListenerCount; i++) {
			serverConnectorListeners.get(i).onConnected(this);
		}
	}

	@Override
	public void onDisconnected(final Connection pConnection) {
		final ArrayList<IServerConnectorListener<C>> serverConnectorListeners = this.getConnectorListeners();
		final int connectorListenerCount = serverConnectorListeners.size();
		for(int i = 0; i < connectorListenerCount; i++) {
			serverConnectorListeners.get(i).onDisconnected(this);
		}
	}

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		final IServerMessage serverMessage = this.mServerMessageReader.readMessage(pDataInputStream);
		this.mServerMessageReader.handleMessage(this, serverMessage);
		this.mServerMessageReader.recycleMessage(serverMessage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerServerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass) {
		this.mServerMessageReader.registerMessage(pFlag, pServerMessageClass);
	}

	public void registerServerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass, final IServerMessageHandler<C> pServerMessageHandler) {
		this.mServerMessageReader.registerMessage(pFlag, pServerMessageClass, pServerMessageHandler);
	}

	public void registerServerMessageHandler(final short pFlag, final IServerMessageHandler<C> pServerMessageHandler) {
		this.mServerMessageReader.registerMessageHandler(pFlag, pServerMessageHandler);
	}

	public synchronized void sendClientMessage(final IClientMessage pClientMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.mConnection.getDataOutputStream();
		pClientMessage.transmit(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IServerConnectorListener<T extends Connection> extends IConnectorListener<ServerConnector<T>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onConnected(final ServerConnector<T> pServerConnector);

		@Override
		public void onDisconnected(final ServerConnector<T> pServerConnector);
	}
}
