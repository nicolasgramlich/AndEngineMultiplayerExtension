package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import org.anddev.andengine.util.Debug;

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

	protected final DataInputStream mDataInputStream;
	protected final DataOutputStream mDataOutputStream;

	protected final IConnectionListener mConnectionListener;
	protected boolean mClosed = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connection(final DataInputStream pDataInputStream, final DataOutputStream pDataOutputStream, final IConnectionListener pConnectionListener) throws IOException {
		this.mDataInputStream = pDataInputStream;
		this.mDataOutputStream = pDataOutputStream;
		
		this.mConnectionListener = pConnectionListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public DataOutputStream getDataOutputStream() {
		return this.mDataOutputStream;
	}

	public DataInputStream getDataInputStream() {
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

	@Override
	public void run() {
		if(this.mConnectionListener != null) {
			this.mConnectionListener.onConnected(this);
		}

		//		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // TODO What ThreadPriority makes sense here?
		try {
			while(!this.isInterrupted()) {
				try {
					this.mConnectionListener.read(this.mDataInputStream);
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
			this.close();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public boolean close() {
		this.interrupt();
		
		if(!this.mClosed) {
			this.mClosed = true;

			if(this.mConnectionListener != null) {
				this.mConnectionListener.onDisconnected(this);
			}
			
			return true;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IConnectionListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		
		public void onConnected(final Connection pConnection); 
		public void onDisconnected(final Connection pConnection);
		
		public void read(final DataInputStream pDataInputStream) throws IOException;
	}
}