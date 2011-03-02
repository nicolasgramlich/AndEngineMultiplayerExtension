package org.anddev.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;

import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.BaseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionCloseClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionEstablishClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPingClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.client.connection.ConnectionPongClientMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionAcceptedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionPongServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.adt.message.server.connection.ConnectionRefusedServerMessage;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ClientMessageFlags;
import org.anddev.andengine.extension.multiplayer.protocol.util.constants.ProtocolConstants;

/**
 * @author Nicolas Gramlich
 * @since 21:05:06 - 18.09.2009
 */
