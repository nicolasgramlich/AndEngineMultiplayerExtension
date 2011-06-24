package org.anddev.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.anddev.andengine.extension.multiplayer.protocol.exception.WifiException;
import org.anddev.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer;
import org.anddev.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.anddev.andengine.util.SocketUtils;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 15:50:07 - 23.06.2011
 */
public class SocketServerDiscoveryClient {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int LOCALPORT_DEFAULT = 9998;

	protected static final int TIMEOUT_DEFAULT = 5000;

	// ===========================================================
	// Fields
	// ===========================================================

	protected AtomicBoolean mTerminated = new AtomicBoolean(false);

	private final InetAddress mDiscoveryBroadcastInetAddress;
	private final int mDiscoveryPort;
	private final int mLocalPort;
	private int mTimeout = TIMEOUT_DEFAULT;
	private final ISocketServerDiscoveryClientListener mSocketServerDiscoveryClientListener;

	private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

	private final byte[] mIncomingDiscoveryDatagramPacketBuffer = new byte[128];
	private final DatagramPacket mIncomingDiscoveryDatagramPacket = new DatagramPacket(this.mIncomingDiscoveryDatagramPacketBuffer, this.mIncomingDiscoveryDatagramPacketBuffer.length);

	private final DatagramPacket mOutgoingDiscoveryDatagramPacket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServerDiscoveryClient(final Context pContext, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException, WifiException {
		this(WifiUtils.getBroadcastIPAddressRaw(pContext), pSocketServerDiscoveryClientListener);
	}

	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this(pDiscoveryBroadcastIPAddress, SocketServerDiscoveryServer.DISCOVERYPORT_DEFAULT, LOCALPORT_DEFAULT, pSocketServerDiscoveryClientListener);
	}

	public SocketServerDiscoveryClient(final Context pContext, final int pDiscoveryPort, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException, WifiException {
		this(WifiUtils.getBroadcastIPAddressRaw(pContext), pDiscoveryPort, LOCALPORT_DEFAULT, pSocketServerDiscoveryClientListener);
	}

	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final int pDiscoveryPort, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this(pDiscoveryBroadcastIPAddress, pDiscoveryPort, LOCALPORT_DEFAULT, pSocketServerDiscoveryClientListener);
	}

	public SocketServerDiscoveryClient(final Context pContext, final int pDiscoveryPort, final int pLocalPort, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException, WifiException {
		this(WifiUtils.getBroadcastIPAddressRaw(pContext), pDiscoveryPort, pLocalPort, pSocketServerDiscoveryClientListener);
	}

	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final int pDiscoveryPort, final int pLocalPort, final ISocketServerDiscoveryClientListener pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this.mDiscoveryPort = pDiscoveryPort;
		this.mLocalPort = pLocalPort;
		this.mSocketServerDiscoveryClientListener = pSocketServerDiscoveryClientListener;

		this.mDiscoveryBroadcastInetAddress = InetAddress.getByAddress(pDiscoveryBroadcastIPAddress);

		final byte[] out = SocketServerDiscoveryServer.MAGIC_IDENTIFIER;
		this.mOutgoingDiscoveryDatagramPacket = new DatagramPacket(out, out.length, SocketServerDiscoveryClient.this.mDiscoveryBroadcastInetAddress, SocketServerDiscoveryClient.this.mDiscoveryPort);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDiscoveryPort() {
		return this.mDiscoveryPort;
	}

	public int getLocalPort() {
		return this.mLocalPort;
	}

	public int getTimeout() {
		return this.mTimeout;
	}

	public void setTimeout(final int pTimeout) {
		this.mTimeout = pTimeout;
	}

	public InetAddress getDiscoveryBroadcastInetAddress() {
		return this.mDiscoveryBroadcastInetAddress;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void terminate() {
		if(!this.mTerminated.getAndSet(true)) {
			this.onTerminate();
		}
	}

	private void onTerminate() {
		this.mExecutorService.shutdownNow();
	}

	public void discoverAsync() throws IllegalStateException {
		if(this.mTerminated.get()) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " was already terminated.");
		}

		this.mExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				SocketServerDiscoveryClient.this.discover();
			}
		});
	}

	private void discover() {
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(this.mLocalPort);
			datagramSocket.setBroadcast(true);

			this.sendOutgoingDiscovery(datagramSocket);

			this.receiveIncomingDiscovery(datagramSocket);
		} catch (final SocketTimeoutException t) {
			this.mSocketServerDiscoveryClientListener.onTimeout(this, t);
		} catch (final Throwable t) {
			this.mSocketServerDiscoveryClientListener.onException(this, t);
		} finally {
			SocketUtils.closeSocket(datagramSocket);
		}
	}

	private void sendOutgoingDiscovery(final DatagramSocket pDatagramSocket) throws SocketException, IOException {
		pDatagramSocket.send(this.mOutgoingDiscoveryDatagramPacket);
	}

	private void receiveIncomingDiscovery(final DatagramSocket datagramSocket) throws SocketException, IOException {
		datagramSocket.setSoTimeout(this.mTimeout);
		final DatagramPacket incomingDiscoveryDatagramPacket = this.mIncomingDiscoveryDatagramPacket;
		datagramSocket.receive(incomingDiscoveryDatagramPacket);

		final String result = new String(incomingDiscoveryDatagramPacket.getData(), incomingDiscoveryDatagramPacket.getOffset(), incomingDiscoveryDatagramPacket.getLength());

		/* Parse result. */
		final int separator = result.indexOf(':');
		final String ipAddress = result.substring(0, separator);
		final int port = Integer.parseInt(result.substring(separator + 1));

		this.mSocketServerDiscoveryClientListener.onDiscovery(SocketServerDiscoveryClient.this, ipAddress, port);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ISocketServerDiscoveryClientListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onDiscovery(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final String pIPAddress, final int pPort);
		public void onTimeout(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final SocketTimeoutException pSocketTimeoutException);
		public void onException(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final Throwable pThrowable);
	}
}
