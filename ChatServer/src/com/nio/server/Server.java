package com.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.nio.interfaces.ServerListener;
import com.nio.model.Message;
import com.nio.util.*;
import com.nio.util.MessageType;

public class Server implements Runnable {
	
	private Selector mSelector;
    private ServerSocketChannel mServerSocketChannel;
    
    private ServerListener mListener;
    
    public ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    public SocketChannel currentClient;
    public ConcurrentHashMap<String, SocketChannel> sockets = new ConcurrentHashMap<String, SocketChannel>();
    
    
    public Server(ServerListener listener) {
    	mListener = listener;
    }
    
    public void connectToServer(String host, int port) {
		try {
			mSelector = Selector.open();
			mServerSocketChannel = ServerSocketChannel.open();

			InetSocketAddress socketAddress = new InetSocketAddress(host, port);
			mServerSocketChannel.socket().bind(socketAddress);
			mServerSocketChannel.configureBlocking(false);
			mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
		} catch (IOException ex) {
			//ex.printStackTrace();
			mListener.onConnectionFailed(ex.getMessage());
			return;
		}
		
		mListener.onStarted(host, port);
    }
    
    /**
     * Listens to clients
     *
     */
    private void connect() {
        try {
            while (true) {
                Thread.sleep(1);

				int readyChannels = mSelector.select();
				if (readyChannels == 0) { continue; }
				
                for (Iterator<SelectionKey> i = mSelector.selectedKeys().iterator(); i.hasNext();) {
                    SelectionKey key = i.next();
                    i.remove();

                    if (key.isConnectable()) {
                        ((SocketChannel)key.channel()).finishConnect();
                    }

                    if (key.isAcceptable()) {
                        // accept connection
                        currentClient = mServerSocketChannel.accept();
                        currentClient.configureBlocking(false);
                        currentClient.socket().setTcpNoDelay(true);
                        currentClient.register(mSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        
                        mListener.onClientConnected(currentClient);
                    }

                    else if (key.isReadable()) {
                    	handleMsg(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets receive byte
     * @param buffer
     * @param length
     * @return
     */
    public static byte[] getReceivedBytes(ByteBuffer buffer, int length) {
        buffer.flip();
        byte[] receivedBytes = new byte[length];
        for (int j = 0; j < receivedBytes.length; j++) {
            receivedBytes[j] = buffer.get(j);
        }
        return receivedBytes;
    }
        
    /**
     * Sends via socket channel
     * @param sc
     * @param bytes
     * @throws IOException 
     */
    public void sendMessage(SocketChannel sc, String text) throws IOException {

    	Message mes = new Message(MessageType.SEND_MESSAGE, 10, "Response: Hello client!", 11);
    	ChannelHandler.sendMsg(sc, mes);
    	//send(currentClient, Serialization.serialize(mes));
    	
    	//send(currentClient, "Response: Hello client!".getBytes());
    }

    /**
     * Sends bytes via socket channel
     * @param sc
     * @param bytes
     */
    private boolean send(SocketChannel sc, byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            while (buffer.hasRemaining()) {
                sc.write(buffer);
            }

            return true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    private void handleMsg(SelectionKey key) throws IOException {
        currentClient = (SocketChannel) key.channel();

        buffer.clear();
        int numBytesRead = -1;

        try {
            numBytesRead = currentClient.read(buffer);
        } catch (Exception e) {
            mListener.onClientDisconnected(currentClient);
        }

        //if (numBytesRead == -1) {
            // No more bytes can be read from the channel
        //    currentClient.close();
        //} else {
            try {
                //byte[] receivedBytes = getReceivedBytes(buffer, numBytesRead);
                //mListener.onClientMessageReceived(currentClient, new String(receivedBytes));
            	sendMessage(currentClient, "test");
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		connect();
	}
}
