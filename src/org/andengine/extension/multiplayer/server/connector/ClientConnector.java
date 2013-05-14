package org.andengine.extension.multiplayer.server.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.client.ServerMessagePool;
import org.andengine.extension.multiplayer.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.server.IClientMessageReader;
import org.andengine.extension.multiplayer.server.IClientMessageReader.ClientMessageReader;
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
public class ClientConnector<C extends Connection> extends Connector<C> {
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

	protected final IClientMessageReader<C> mClientMessageReader;

	protected final MessagePool<IServerMessage> mServerMessagePool;

	protected final MessageQueue<IServerMessage> mServerMessageQueue = new MessageQueue<IServerMessage>(true);

	private final ParameterCallable<IClientConnectorListener<C>> mOnStartedParameterCallable = new ParameterCallable<ClientConnector.IClientConnectorListener<C>>() {
		@Override
		public void call(final IClientConnectorListener<C> pClientConnectorListener) {
			pClientConnectorListener.onStarted(ClientConnector.this);
		}
	};

	private final ParameterCallable<IClientConnectorListener<C>> mOnTerminatedParameterCallable = new ParameterCallable<ClientConnector.IClientConnectorListener<C>>() {
		@Override
		public void call(final IClientConnectorListener<C> pClientConnectorListener) {
			pClientConnectorListener.onTerminated(ClientConnector.this);
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClientConnector(final C pConnection) throws IOException {
		this(pConnection, new ClientMessageReader<C>(), new ServerMessagePool());
	}

	public ClientConnector(final C pConnection, final IClientMessageReader<C> pClientMessageReader) throws IOException {
		this(pConnection, pClientMessageReader, new ServerMessagePool());
	}

	public ClientConnector(final C pConnection, final MessagePool<IServerMessage> pServerMessagePool) throws IOException {
		this(pConnection, new ClientMessageReader<C>(), pServerMessagePool);
	}

	public ClientConnector(final C pConnection, final IClientMessageReader<C> pClientMessageReader, final MessagePool<IServerMessage> pServerMessagePool) throws IOException {
		super(pConnection);

		this.mClientMessageReader = pClientMessageReader;
		this.mServerMessagePool = pServerMessagePool;

		this.initServerMessageQueue();
	}

	protected void initServerMessageQueue() {
		this.mServerMessageQueue.addQueue(PRIORITY_HIGH);
		this.mServerMessageQueue.addQueue(PRIORITY_MEDIUM);
		this.mServerMessageQueue.addQueue(PRIORITY_LOW);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IClientMessageReader<C> getClientMessageReader() {
		return this.mClientMessageReader;
	}

	public MessagePool<IServerMessage> getServerMessagePool() {
		return this.mServerMessagePool;
	}

	public IServerMessage obtainServerMessage(final short pFlag) {
		return this.mServerMessagePool.obtainMessage(pFlag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SmartList<IClientConnectorListener<C>> getConnectorListeners() {
		return (SmartList<IClientConnectorListener<C>>) super.getConnectorListeners();
	}

	public void addClientConnectorListener(final IClientConnectorListener<C> pClientConnectorListener) {
		super.addConnectorListener(pClientConnectorListener);
	}

	public void removeClientConnectorListener(final IClientConnectorListener<C> pClientConnectorListener) {
		super.removeConnectorListener(pClientConnectorListener);
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
		final IClientMessage clientMessage = this.mClientMessageReader.readMessage(pDataInputStream);
		this.mClientMessageReader.handleMessage(this, clientMessage);
		this.mClientMessageReader.recycleMessage(clientMessage);
	}

	@Override
	public void write(final DataOutputStream pDataOutputStream) throws IOException, InterruptedException {
		final IServerMessage serverMessage = this.mServerMessageQueue.take();
		serverMessage.write(pDataOutputStream);
		pDataOutputStream.flush();
		this.mServerMessagePool.recycleMessage(serverMessage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerClientMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass) {
		this.mClientMessageReader.registerMessage(pFlag, pClientMessageClass);
	}

	public void registerClientMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass, final IClientMessageHandler<C> pClientMessageHandler) {
		this.mClientMessageReader.registerMessage(pFlag, pClientMessageClass, pClientMessageHandler);
	}

	public void registerClientMessageHandler(final short pFlag, final IClientMessageHandler<C> pClientMessageHandler) {
		this.mClientMessageReader.registerMessageHandler(pFlag, pClientMessageHandler);
	}

	/**
	 * @deprecated Instead use {@link #sendServerMessage(int, IServerMessage)} or {@link #sendServerMessage(int, boolean, IServerMessage)()}.
	 */
	@Deprecated
	public void sendServerMessage(final IServerMessage pServerMessage) {
		this.sendServerMessage(PRIORITY_DEFAULT, pServerMessage);
	}

	public void sendServerMessage(final int pPriority, final IServerMessage pServerMessage) {
		this.sendServerMessage(pPriority, false, pServerMessage);
	}

	public void sendServerMessage(final int pPriority, final boolean pPreemptive, final IServerMessage pServerMessage) {
		try {
			if (pPreemptive) {
				this.mServerMessageQueue.clearAndPut(pPriority, pServerMessage);
			} else {
				this.mServerMessageQueue.put(pPriority, pServerMessage);
			}
		} catch (final InterruptedException e) {
			Debug.e(e);
		}
	}

	public void clearServerMessages() {
		this.mServerMessageQueue.clear();
	}

	public void clearServerMessages(final int pPriority) {
		this.mServerMessageQueue.clear(pPriority);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IClientConnectorListener<T extends Connection> extends IConnectorListener<ClientConnector<T>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onStarted(final ClientConnector<T> pClientConnector);

		@Override
		public void onTerminated(final ClientConnector<T> pClientConnector);
	}
}
