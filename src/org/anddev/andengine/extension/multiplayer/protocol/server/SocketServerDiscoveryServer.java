package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.anddev.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer.ISocketServerDiscoveryServerListener.DefaultSocketServerDiscoveryServerListener;
import org.anddev.andengine.util.ArrayUtils;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SocketUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:08:20 - 23.06.2011
 */
public class SocketServerDiscoveryServer extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int DISCOVERYPORT_DEFAULT = 9999;

	public static final byte[] MAGIC_IDENTIFIER = new byte[]{
		(byte)'A',
		(byte)'n',
		(byte)'d',
		(byte)'E',
		(byte)'n',
		(byte)'g',
		(byte)'i',
		(byte)'n',
		(byte)'e',
		(byte)'-',
		(byte)'S',
		(byte)'o',
		(byte)'c',
		(byte)'k',
		(byte)'e',
		(byte)'t',
		(byte)'S',
		(byte)'e',
		(byte)'r',
		(byte)'v',
		(byte)'e',
		(byte)'r',
		(byte)'D',
		(byte)'i',
		(byte)'s',
		(byte)'c',
		(byte)'o',
		(byte)'v',
		(byte)'e',
		(byte)'r',
		(byte)'y'
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mDiscoveryPort;
	private final String mServerIP;
	private final int mServerPort;

	private DatagramSocket mDatagramSocket;

	private final byte[] mIncomingDiscoveryDatagramPacketBuffer = new byte[128];
	private final DatagramPacket mIncomingDiscoveryDatagramPacket = new DatagramPacket(this.mIncomingDiscoveryDatagramPacketBuffer, this.mIncomingDiscoveryDatagramPacketBuffer.length);

	private final byte[] mOutgoingDiscoveryDatagramPacketBuffer;

	protected ISocketServerDiscoveryServerListener mSocketServerDiscoveryServerListener;
	protected AtomicBoolean mRunning = new AtomicBoolean(false);
	protected AtomicBoolean mTerminated = new AtomicBoolean(false);

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServerDiscoveryServer(final String pServerIP, final int pServerPort) {
		this(DISCOVERYPORT_DEFAULT, pServerIP, pServerPort, new DefaultSocketServerDiscoveryServerListener());
	}

	public SocketServerDiscoveryServer(final int pDiscoveryPort, final String pServerIP, final int pServerPort) {
		this(pDiscoveryPort, pServerIP, pServerPort, new DefaultSocketServerDiscoveryServerListener());
	}

	public SocketServerDiscoveryServer(final int pDiscoveryPort, final String pServerIP, final int pServerPort, final ISocketServerDiscoveryServerListener pSocketServerDiscoveryServerListener) {
		this.mDiscoveryPort = pDiscoveryPort;
		this.mServerIP = pServerIP;
		this.mServerPort = pServerPort;
		this.mSocketServerDiscoveryServerListener = pSocketServerDiscoveryServerListener;

		this.mOutgoingDiscoveryDatagramPacketBuffer = new StringBuilder().append(pServerIP).append(':').append(pServerPort).toString().getBytes();

		this.initName();
	}

	private void initName() {
		this.setName(this.getClass().getName());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRunning() {
		return this.mRunning.get();
	}

	public boolean isTerminated() {
		return this.mTerminated.get();
	}

	public int getDiscoveryPort() {
		return this.mDiscoveryPort;
	}

	public String getServerIP() {
		return this.mServerIP;
	}

	public int getServerPort() {
		return this.mServerPort;
	}

	public boolean hasSocketServerDiscoveryServerListener() {
		return this.mSocketServerDiscoveryServerListener != null;
	}

	public ISocketServerDiscoveryServerListener getSocketServerDiscoveryServerListener() {
		return this.mSocketServerDiscoveryServerListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		try {
			this.onStart();
			this.mRunning.set(true);

			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);  // TODO What ThreadPriority makes sense here?

			while(!Thread.interrupted() && this.mRunning.get() && !this.mTerminated.get()) {
				try {
					final DatagramPacket incomingDiscoveryDatagramPacket = this.mIncomingDiscoveryDatagramPacket;

					this.mDatagramSocket.receive(incomingDiscoveryDatagramPacket);

					if(this.onVerifyIncomingDiscoveryDatagramPacket(incomingDiscoveryDatagramPacket)) {
						this.onDiscovered(incomingDiscoveryDatagramPacket);
					}
				} catch (final Throwable pThrowable) {
					this.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.onException(pThrowable);
		} finally {
			this.terminate();
		}
	}

	@Override
	public void interrupt() {
		this.terminate();

		super.interrupt();
	}

	@Override
	protected void finalize() throws Throwable {
		this.interrupt();
		super.finalize();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected boolean onVerifyIncomingDiscoveryDatagramPacket(final DatagramPacket pIncomingDiscoveryDatagramPacket) {
		return ArrayUtils.equals(MAGIC_IDENTIFIER, 0, pIncomingDiscoveryDatagramPacket.getData(), pIncomingDiscoveryDatagramPacket.getOffset(), MAGIC_IDENTIFIER.length);
	}

	protected void onDiscovered(final DatagramPacket pIncomingDiscoveryDatagramPacket) throws IOException {
		final InetAddress incomingDiscoveryIPAddress = pIncomingDiscoveryDatagramPacket.getAddress();
		final int incomingDiscoveryPort = pIncomingDiscoveryDatagramPacket.getPort();

		this.mSocketServerDiscoveryServerListener.onDiscovered(this, incomingDiscoveryIPAddress, incomingDiscoveryPort);

		final byte[] outgoingDiscoveryDatagramPacketBuffer = this.mOutgoingDiscoveryDatagramPacketBuffer;
		// TODO Can the DatagramPacket be safely reused/pooled?
		final DatagramPacket outgoingDiscoveryDatagramPacket = new DatagramPacket(outgoingDiscoveryDatagramPacketBuffer, outgoingDiscoveryDatagramPacketBuffer.length, incomingDiscoveryIPAddress, incomingDiscoveryPort);
		this.mDatagramSocket.send(outgoingDiscoveryDatagramPacket);
	}

	protected void onStart() throws SocketException {
		this.mDatagramSocket = new DatagramSocket(this.mDiscoveryPort);

		this.mSocketServerDiscoveryServerListener.onStarted(this);
	}

	protected void onTerminate() {
		SocketUtils.closeSocket(this.mDatagramSocket);
		this.mSocketServerDiscoveryServerListener.onTerminated(this);
	}

	private void onException(final Throwable pThrowable) {
		this.mSocketServerDiscoveryServerListener.onException(this, pThrowable);
	}

	private void terminate() {
		if(!this.mTerminated.getAndSet(true)) {
			this.mRunning.set(false);

			this.onTerminate();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISocketServerDiscoveryServerListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// TODO Refine naming
		public void onStarted(final SocketServerDiscoveryServer pSocketServerDiscoveryServer);
		public void onTerminated(final SocketServerDiscoveryServer pSocketServerDiscoveryServer);
		public void onDiscovered(final SocketServerDiscoveryServer pSocketServerDiscoveryServer, final InetAddress pInetAddress, final int pPort);
		public void onException(final SocketServerDiscoveryServer pSocketServerDiscoveryServer, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultSocketServerDiscoveryServerListener implements ISocketServerDiscoveryServerListener {
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
			public void onStarted(final SocketServerDiscoveryServer pSocketServerDiscoveryServer) {
				Debug.d("SocketServerDiscoveryServer started on discoveryPort: " + pSocketServerDiscoveryServer.getDiscoveryPort());
			}

			@Override
			public void onTerminated(final SocketServerDiscoveryServer pSocketServerDiscoveryServer) {
				Debug.d("SocketServerDiscoveryServer terminated on discoveryPort: " + pSocketServerDiscoveryServer.getDiscoveryPort());
			}

			@Override
			public void onDiscovered(final SocketServerDiscoveryServer pSocketServerDiscoveryServer, final InetAddress pInetAddress, final int pPort) {
				Debug.d("SocketServerDiscoveryServer discovered by: " + pInetAddress.getHostAddress() + ":" + pPort);
			}

			@Override
			public void onException(final SocketServerDiscoveryServer pSocketServerDiscoveryServer, final Throwable pThrowable) {
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