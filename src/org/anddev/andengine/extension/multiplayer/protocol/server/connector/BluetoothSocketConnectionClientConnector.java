package org.anddev.andengine.extension.multiplayer.protocol.server.connector;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.server.IClientMessageReader;
import org.anddev.andengine.extension.multiplayer.protocol.shared.BluetoothSocketConnection;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 15:44:42 - 04.03.2011
 */
public class BluetoothSocketConnectionClientConnector extends ClientConnector<BluetoothSocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException {
		super(pBluetoothSocketConnection);
	}

	private BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final IClientMessageReader<BluetoothSocketConnection> pClientMessageReader) throws IOException {
		super(pBluetoothSocketConnection, pClientMessageReader);
	}

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
	
	public static interface IBluetoothSocketConnectionClientConnectorListener extends IClientConnectorListener<BluetoothSocketConnection> {
		
	}

	public static class DefaultBluetoothSocketClientConnectorListener implements IBluetoothSocketConnectionClientConnectorListener {
		@Override
		public void onConnected(ClientConnector<BluetoothSocketConnection> pClientConnector) {
			Debug.d("Accepted Client-Connection from: '" + pClientConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}

		@Override
		public void onDisconnected(ClientConnector<BluetoothSocketConnection> pClientConnector) {
			Debug.d("Closed Client-Connection from: '" + pClientConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}
	}
}
