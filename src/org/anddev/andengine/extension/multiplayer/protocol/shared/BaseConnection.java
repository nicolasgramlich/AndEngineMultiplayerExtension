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
public abstract class BaseConnection<M extends IMessage> extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Socket mSocket;
	private final DataInputStream mDataInputStream;
	private final DataOutputStream mDataOutputStream;
	private final IMessageSwitch<M> mMessageSwitch;
	private final BaseConnectionListener<M, BaseConnection<M>> mConnectionListener;
	private boolean mConnectionCloseSent = false;
	private final BaseMessageExtractor<M> mMessageExtractor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseConnection(final Socket pSocket, final BaseConnectionListener<M, BaseConnection<M>> pConnectionListener, final BaseMessageExtractor<M> pMessageExtractor, final IMessageSwitch<M> pMessageSwitch) throws IOException {
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

	public IMessageSwitch<M> getMessageSwitch() {
		return this.mMessageSwitch;
	}

	public boolean hasConnectionListener(){
		return this.mConnectionListener != null;
	}

	public BaseConnectionListener<M, BaseConnection<M>> getConnectionListener() {
		return this.mConnectionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void handleMessage(final M pMessage) throws IOException;

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

					final short messageFlag = this.mMessageExtractor.readMessageFlag(this.mDataInputStream);
					final M message = this.mMessageExtractor.readMessage(messageFlag, this.mDataInputStream);
					this.handleMessage(message);

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
