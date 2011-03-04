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
public abstract class BluetoothSocketServer extends Server<BluetoothSocketConnection, ClientConnector<BluetoothSocketConnection>> {
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
		this(pUUID, pClientConnectorListener, new DefaultBluetoothSocketServerListener());
	}

	public BluetoothSocketServer(final String pUUID, final IBluetoothSocketServerListener pBluetoothSocketServerListener) {
		this(pUUID, new DefaultClientConnectorListener<BluetoothSocketConnection>(), pBluetoothSocketServerListener);
	}

	public BluetoothSocketServer(final String pUUID, final IClientConnectorListener<BluetoothSocketConnection> pClientConnectorListener, final IBluetoothSocketServerListener pBluetoothSocketServerListener) {
		super(pClientConnectorListener, pBluetoothSocketServerListener);

		this.mUUID = pUUID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public String getUUID() {
		return this.mUUID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract ClientConnector<BluetoothSocketConnection> newClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException;

	@Override
	protected void onInit() throws IOException {
		this.mBluetoothServerSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(this.getClass().getName(), UUID.fromString(this.mUUID));
	}

	@Override
	protected ClientConnector<BluetoothSocketConnection> acceptClientConnector() throws IOException {
		/* Wait for an incoming connection. */
		final BluetoothSocket clientBluetoothSocket = this.mBluetoothServerSocket.accept();

		/* Spawn a new ClientConnector, which send and receive data to and from the client. */
		return this.newClientConnector(new BluetoothSocketConnection(clientBluetoothSocket));
	}

	@Override
	public void onClosed() {
		try {
			this.mBluetoothServerSocket.close(); // TODO Put to SocketUtils
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface IBluetoothSocketServerListener extends IServerListener<Server<BluetoothSocketConnection, ClientConnector<BluetoothSocketConnection>>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public static class DefaultBluetoothSocketServerListener implements IBluetoothSocketServerListener {
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
			public void onStarted(final Server<BluetoothSocketConnection, ClientConnector<BluetoothSocketConnection>> pBluetoothSocketServer) {
				Debug.d("Server started on port: " + ((BluetoothSocketServer)pBluetoothSocketServer).getUUID());
			}
			@Override
			public void onTerminated(final Server<BluetoothSocketConnection, ClientConnector<BluetoothSocketConnection>> pBluetoothSocketServer) {
				Debug.d("Server terminated on port: " + ((BluetoothSocketServer)pBluetoothSocketServer).getUUID());
			}
			@Override
			public void onException(final Server<BluetoothSocketConnection, ClientConnector<BluetoothSocketConnection>> pBluetoothSocketServer, final Throwable pThrowable) {
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
