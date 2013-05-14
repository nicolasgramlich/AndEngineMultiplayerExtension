package org.andengine.extension.multiplayer.client.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.client.IServerMessageReader;
import org.andengine.extension.multiplayer.client.IServerMessageReader.ServerMessageReader;
import org.andengine.extension.multiplayer.server.ClientMessagePool;
import org.andengine.extension.multiplayer.shared.Connection;
import org.andengine.extension.multiplayer.shared.Connector;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.extension.multiplayer.util.MessageQueue;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.call.ParameterCallable;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ServerConnector<C extends Connection> extends Connector<C> {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int PRIORITY_HIGH = 0;
	public static final int PRIORITY_MEDIUM = PRIORITY_HIGH + 1;
	public static final int PRIORITY_LOW = PRIORITY_MEDIUM + 1;

	public static final int PRIORITY_DEFAULT = PRIORITY_MEDIUM;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IServerMessageReader<C> mServerMessageReader;

	protected final MessagePool<IClientMessage> mClientMessagePool;

	protected final MessageQueue<IClientMessage> mClientMessageQueue = new MessageQueue<IClientMessage>(true);

	private final ParameterCallable<IServerConnectorListener<C>> mOnStartedParameterCallable = new ParameterCallable<ServerConnector.IServerConnectorListener<C>>() {
		@Override
		public void call(final IServerConnectorListener<C> pServerConnectorListener) {
			pServerConnectorListener.onStarted(ServerConnector.this);
		}
	};

	private final ParameterCallable<IServerConnectorListener<C>> mOnTerminatedParameterCallable = new ParameterCallable<ServerConnector.IServerConnectorListener<C>>() {
		@Override
		public void call(final IServerConnectorListener<C> pServerConnectorListener) {
			pServerConnectorListener.onTerminated(ServerConnector.this);
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public ServerConnector(final C pConnection, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		this(pConnection, new ServerMessageReader<C>(), new ClientMessagePool(), pServerConnectorListener);
	}

	public ServerConnector(final C pConnection, final IServerMessageReader<C> pServerMessageReader, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		this(pConnection, pServerMessageReader, new ClientMessagePool(), pServerConnectorListener);
	}

	public ServerConnector(final C pConnection, final MessagePool<IClientMessage> pClientMessagePool, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		this(pConnection, new ServerMessageReader<C>(), pClientMessagePool, pServerConnectorListener);
	}

	public ServerConnector(final C pConnection, final IServerMessageReader<C> pServerMessageReader, final MessagePool<IClientMessage> pClientMessagePool, final IServerConnectorListener<C> pServerConnectorListener) throws IOException {
		super(pConnection);

		this.mServerMessageReader = pServerMessageReader;
		this.mClientMessagePool = pClientMessagePool;

		this.addServerConnectorListener(pServerConnectorListener);
		this.initClientMessageQueue();
	}

	protected void initClientMessageQueue() {
		this.mClientMessageQueue.addQueue(PRIORITY_HIGH);
		this.mClientMessageQueue.addQueue(PRIORITY_MEDIUM);
		this.mClientMessageQueue.addQueue(PRIORITY_LOW);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IServerMessageReader<C> getServerMessageReader() {
		return this.mServerMessageReader;
	}

	public MessagePool<IClientMessage> getClientMessagePool() {
		return this.mClientMessagePool;
	}

	public IClientMessage obtainClientMessage(final short pFlag) {
		return this.mClientMessagePool.obtainMessage(pFlag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SmartList<IServerConnectorListener<C>> getConnectorListeners() {
		return (SmartList<IServerConnectorListener<C>>) super.getConnectorListeners();
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
	public void onStarted(final Connection pConnection) {
		this.getConnectorListeners().call(this.mOnStartedParameterCallable);
	}

	@Override
	public void onTerminated(final Connection pConnection) {
		this.getConnectorListeners().call(this.mOnTerminatedParameterCallable);
	}

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		final IServerMessage serverMessage = this.mServerMessageReader.readMessage(pDataInputStream);
		this.mServerMessageReader.handleMessage(this, serverMessage);
		this.mServerMessageReader.recycleMessage(serverMessage);
	}

	@Override
	public void write(final DataOutputStream pDataOutputStream) throws IOException, InterruptedException {
		final IClientMessage clientMessage = this.mClientMessageQueue.take();
		clientMessage.write(pDataOutputStream);
		pDataOutputStream.flush();
		this.mClientMessagePool.recycleMessage(clientMessage);
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

	/**
	 * @deprecated Instead use {@link #sendClientMessage(int, IServerMessage)} or {@link #sendClientMessage(int, boolean, IServerMessage)()}.
	 */
	@Deprecated
	public void sendClientMessage(final IClientMessage pClientMessage) {
		this.sendClientMessage(PRIORITY_DEFAULT, pClientMessage);
	}

	public void sendClientMessage(final int pPriority, final IClientMessage pClientMessage) {
		this.sendClientMessage(pPriority, false, pClientMessage);
	}

	public void sendClientMessage(final int pPriority, final boolean pPreemptive, final IClientMessage pClientMessage) {
		try {
			if (pPreemptive) {
				this.mClientMessageQueue.clearAndPut(pPriority, pClientMessage);
			} else {
				this.mClientMessageQueue.put(pPriority, pClientMessage);
			}
		} catch (final InterruptedException e) {
			Debug.e(e);
		}
	}

	public void clearClientMessages() {
		this.mClientMessageQueue.clear();
	}

	public void clearClientMessages(final int pPriority) {
		this.mClientMessageQueue.clear(pPriority);
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
		public void onStarted(final ServerConnector<T> pServerConnector);

		@Override
		public void onTerminated(final ServerConnector<T> pServerConnector);
	}
}
