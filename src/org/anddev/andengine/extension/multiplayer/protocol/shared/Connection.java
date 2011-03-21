package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

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

	protected IConnectionListener mConnectionListener;
	protected AtomicBoolean mClosed = new AtomicBoolean(false);

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connection(final DataInputStream pDataInputStream, final DataOutputStream pDataOutputStream) throws IOException {
		this.mDataInputStream = pDataInputStream;
		this.mDataOutputStream = pDataOutputStream;
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

	public void setConnectionListener(final IConnectionListener pConnectionListener) {
		this.mConnectionListener = pConnectionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onClosed();

	@Override
	public void run() {
		if(this.mConnectionListener != null) {
			this.mConnectionListener.onConnected(this);
		}

		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);  // TODO What ThreadPriority makes sense here?

		try {
			while(!this.mClosed.get()) {
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

	@Override
	public void interrupt() {
		this.close();

		super.interrupt();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void close() {
		if(!this.mClosed.getAndSet(true)) {
			if(this.mConnectionListener != null) {
				this.mConnectionListener.onDisconnected(this);
			}

			this.onClosed();
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