package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.anddev.andengine.util.Debug;

import android.bluetooth.BluetoothSocket;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class BluetoothSocketConnection extends Connection {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final BluetoothSocket mBluetoothSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BluetoothSocketConnection(final BluetoothSocket pBluetoothSocket) throws IOException {
		super(new DataInputStream(pBluetoothSocket.getInputStream()), new DataOutputStream(pBluetoothSocket.getOutputStream()));

		this.mBluetoothSocket = pBluetoothSocket;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BluetoothSocket getBluetoothSocket() {
		return this.mBluetoothSocket;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean close() {
		final boolean closed = super.close();

		/* Ensure Socket is really closed. */
		try {
			this.mBluetoothSocket.close(); // TODO Put to SocketUtils
		} catch (IOException e) {
			Debug.e(e);
		}
		
		return closed;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
