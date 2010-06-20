package org.anddev.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public abstract class BaseConnector<T extends IMessage> extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Socket mSocket;
	private final DataInputStream mDataInputStream;
	private final DataOutputStream mDataOutputStream;
	private final IMessageSwitch<T> mMessageSwitch;
	private final BaseConnectionListener<T, BaseConnector<T>> mConnectionListener;
	private boolean mConnectionCloseSent = false;
	private final BaseMessageExtractor<T> mMessageExtractor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseConnector(final Socket pSocket, final BaseConnectionListener<T, BaseConnector<T>> pConnectionListener, final BaseMessageExtractor<T> pMessageExtractor, final IMessageSwitch<T> pMessageSwitch) throws IOException {
		this.mSocket = pSocket;
		this.mConnectionListener = pConnectionListener;
		this.mMessageExtractor = pMessageExtractor;
		this.mMessageSwitch = pMessageSwitch;

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

	public IMessageSwitch<T> getMessageSwitch() {
		return this.mMessageSwitch;
	}

	public boolean hasConnectionListener(){
		return this.mConnectionListener != null;
	}

	public BaseConnectionListener<T, BaseConnector<T>> getConnectionListener() {
		return this.mConnectionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onSendConnectionClose();

	@Override
	public void run() {
		if(this.mConnectionListener != null) {
			this.mConnectionListener.onConnect(this);
		}

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // TODO What ThreadPriority makes sense here?
		try {
			while (!this.isInterrupted()) {
				try {

					final T message = this.mMessageExtractor.readMessage(this.mDataInputStream);
					this.mMessageSwitch.doSwitch(message);

				} catch (final SocketException se){
					this.interrupt();
				} catch (final EOFException eof){
					this.interrupt();
				} catch (final Throwable pThrowable) {
					Debug.e(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
		}finally{
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

	private void closeConnection() {
		if(!this.mConnectionCloseSent && this.mSocket != null && !this.mSocket.isClosed()) {
			this.mConnectionCloseSent = true;
			this.onSendConnectionClose();
		}

		/* Ensure Socket is really closed. */
		SocketUtils.closeSocket(this.mSocket);

		if(this.mConnectionListener != null) {
			this.mConnectionListener.onDisconnect(this);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
