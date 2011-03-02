package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public abstract class Connection extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Socket mSocket;
	private final DataInputStream mDataInputStream;
	private final DataOutputStream mDataOutputStream;

	private final IConnectionListener mConnectionListener;
	private boolean mConnectionCloses = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connection(final Socket pSocket, final IConnectionListener pConnectionListener) throws IOException {
		this.mSocket = pSocket;
		this.mConnectionListener = pConnectionListener;

		this.mDataInputStream = new DataInputStream(pSocket.getInputStream());
		this.mDataOutputStream = new DataOutputStream(pSocket.getOutputStream());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Socket getSocket() {
		return this.mSocket;
	}

	protected DataOutputStream getDataOutputStream() {
		return this.mDataOutputStream;
	}

	protected DataInputStream getDataInputStream() {
		return this.mDataInputStream;
	}

	public boolean hasConnectionListener(){
		return this.mConnectionListener != null;
	}

	public IConnectionListener getConnectionListener() {
		return this.mConnectionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void read(final DataInputStream pDataInputStream) throws IOException;
	protected abstract void onConnectionClosed();

	@Override
	public void run() {
		if(this.mConnectionListener != null) {
			this.mConnectionListener.onConnected(this);
		}

		//		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // TODO What ThreadPriority makes sense here?
		try {
			while(!this.isInterrupted()) {
				try {
					this.read(this.mDataInputStream);
				} catch (final SocketException se) {
					this.interrupt();
				} catch (final EOFException eof) {
					this.interrupt();
				} catch (final Throwable pThrowable) {
					Debug.e(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		} finally {
			this.closeConnection();
		}
	}

	@Override
	public void interrupt() {
		this.closeConnection();

		super.interrupt();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void closeConnection() {
		if(!this.mConnectionCloses && this.mSocket != null && !this.mSocket.isClosed()) {
			this.mConnectionCloses = true;
			this.onConnectionClosed();
		}

		/* Ensure Socket is really closed. */
		SocketUtils.closeSocket(this.mSocket);

		if(this.mConnectionListener != null) {
			this.mConnectionListener.onDisconnected(this);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
