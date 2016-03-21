package com.nio.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.nio.model.*;
import com.nio.util.ChannelHandler;
import com.nio.util.MessageType;

public class nioServer implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    // Container for connected users
    private Map<User, SocketChannel> connectedUsers;
    private Map<SocketChannel, User> connectedChannels;
    
    // Valid users
    private Map<String, String> registeredUsers;

    //private Map<SelectionKey, FileChannelWrapper> fileTransferChannels;
	
    public nioServer(int port) {
        connectedUsers = new ConcurrentHashMap<>();
        connectedChannels = new ConcurrentHashMap<>();
        registeredUsers = new ConcurrentHashMap<>();
        //fileTransferChannels = new ConcurrentHashMap<>();

        try {
            initServer(port);
            //loadUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initServer(int port) throws IOException {
        System.out.println("Starting Server");
        // Open Server Socket Channel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        // Register Selector
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    
	@Override
	public void run() {
		while (true) {
			try {
				
				int readyChannels = selector.select();
				if (readyChannels == 0) { continue; }
				
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                
                while (keyIterator.hasNext()) {
                	SelectionKey key = keyIterator.next();
                	
                	if (key.isAcceptable()) {
                		// TODO Logger
                        System.out.println("New Client Accepted");
               		
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        serverSocketChannel.configureBlocking(false);

                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                	}
                	
                	else if (key.isReadable()) {
                		handleMsg(key);
                	}
                	
                	keyIterator.remove();
                }
				
			} catch (IOException e) {
				// TODO Logger
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void handleMsg(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

            // Read data
            Message msg = ChannelHandler.readMsg(socketChannel);
            System.out.println("Received data : " + msg);

            User user = checkMsgForCredentials(msg);
            if (user != null) {
            	System.out.println("Handling new user : " + user);
            	
            	//Valide log in
                connectedUsers.put(user, socketChannel);
                connectedChannels.put(socketChannel, user);
                //invalid user
            }
            else {
                User currentUser = connectedChannels.get(socketChannel);
                SocketChannel clientSocketChannel = connectedUsers.get(currentUser);
                msg.setText(" Server to Client ");
            	ChannelHandler.sendMsg(clientSocketChannel, msg);
            }
    }

	private User checkMsgForCredentials(Message msg) {
        
		if (msg.getMessageType() == MessageType.LOG_IN) {
            String[] credentials = msg.getText().split(":");
            return new User(1,credentials[0], credentials[1]);
        }

        return null;
	}

}