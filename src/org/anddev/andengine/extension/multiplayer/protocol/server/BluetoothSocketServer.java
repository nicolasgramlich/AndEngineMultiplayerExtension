package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.util.UUID;

import org.anddev.andengine.extension.multiplayer.protocol.server.BluetoothSocketServer.IBluetoothSocketServerListener.DefaultBluetoothSocketServerListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.server.ClientConnector.IClientConnectorListener.DefaultClientConnectorListener;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BluetoothSocketConnection;
import org.anddev.andengine.util.Debug;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * @author Nicolas Gramlich
 * @since 15:41:31 - 03.03.2011
 */
public abstract class BluetoothSocketServer<CC extends ClientConnector<BluetoothSocketConnection>> extends Server<BluetoothSocketConnection, CC> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mUUID;
	private BluetoothServerSocket mBluetoothServerSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BluetoothSocketServer(final String pUUID) {
		this(pUUID, new DefaultClientConnectorListener<BluetoothSocketConnection>());
	}

	public BluetoothSocketServer(final String pUUID, final IClientConnectorListener<BluetoothSocketConnection> pClientConnectorListener) {
		this(pUUID, pClientConnectorListener, new DefaultBluetoothSocketServerListener<CC>());
	}

	public BluetoothSocketServer(final String pUUID, final IBluetoothSocketServerListener<CC> pBluetoothSocketServerListener) {
		this(pUUID, new DefaultClientConnectorListener<BluetoothSocketConnection>(), pBluetoothSocketServerListener);
	}

	public BluetoothSocketServer(final String pUUID, final IClientConnectorListener<BluetoothSocketConnection> pClientConnectorListener, final IBluetoothSocketServerListener<CC> pBluetoothSocketServerListener) {
		super(pClientConnectorListener, pBluetoothSocketServerListener);

		this.mUUID = pUUID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getUUID() {
		return this.mUUID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IBluetoothSocketServerListener<CC> getServerListener() {
		return (IBluetoothSocketServerListener<CC>)super.getServerListener();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract CC newClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException;

	@Override
	protected void onStart() throws IOException {
		this.mBluetoothServerSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(this.getClass().getName(), UUID.fromString(this.mUUID));
	}

	@Override
	protected CC acceptClientConnector() throws IOException {
		/* Wait for an incoming connection. */
		final BluetoothSocket clientBluetoothSocket = this.mBluetoothServerSocket.accept();

		/* Spawn a new ClientConnector, which send and receive data to and from the client. */
		return this.newClientConnector(new BluetoothSocketConnection(clientBluetoothSocket));
	}

	@Override
	public void onTerminate() {
		try {
			this.mBluetoothServerSocket.close(); // TODO Put to SocketUtils
		} catch (final IOException e) {
			Debug.e(e);
		}
		this.getServerListener().onTerminated(this);
	}

	@Override
	protected void onException(final Throwable pThrowable) {
		this.getServerListener().onException(this, pThrowable);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IBluetoothSocketServerListener<CC extends ClientConnector<BluetoothSocketConnection>> extends IServerListener<BluetoothSocketServer<CC>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onStarted(final BluetoothSocketServer<CC> pBluetoothSocketServer);

		@Override
		public void onTerminated(final BluetoothSocketServer<CC> pBluetoothSocketServer);

		@Override
		public void onException(final BluetoothSocketServer<CC> pBluetoothSocketServer, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultBluetoothSocketServerListener<CC extends ClientConnector<BluetoothSocketConnection>> implements IBluetoothSocketServerListener<CC> {
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

			@Override
			public void onStarted(final BluetoothSocketServer<CC> pBluetoothSocketServer) {
				Debug.d("Server started on port: " + pBluetoothSocketServer.getUUID());
			}

			@Override
			public void onTerminated(final BluetoothSocketServer<CC> pBluetoothSocketServer) {
				Debug.d("Server terminated on port: " + pBluetoothSocketServer.getUUID());
			}

			@Override
			public void onException(final BluetoothSocketServer<CC> pBluetoothSocketServer, final Throwable pThrowable) {
				Debug.e(pThrowable);
			}

			// ===========================================================
			// Methods
			// ===========================================================

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}
	}
}
