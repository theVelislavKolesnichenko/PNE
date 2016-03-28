package com.nio.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.nio.model.*;
import com.nio.util.ChannelHandler;
import com.nio.util.MessageType;
import com.nio.util.Serialization;

public class nioServer implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    List<Room> ll = new ArrayList<Room>();
    
    void intiRooms()
    {
    	List<User> ll = new ArrayList<User>();
    	ll.add(new User(1, "User0", "Pass0"));
    	ll.add(new User(2, "User1", "Pass1"));
    	ll.add(new User(3, "User2", "Pass2"));
    	ll.add(new User(4, "User3", "Pass3"));
    	ll.add(new User(5, "User4", "Pass4"));
    	
    	this.ll.add(new Room(1, "Room0", ll));
    	this.ll.add(new Room(2, "Room1", ll));
    }
    
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
    	
    	intiRooms();
    	
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
            	
            	//ToDo: Valide log in
                connectedUsers.put(user, socketChannel);
                connectedChannels.put(socketChannel, user);

                //ToDo: invalid user
                ChannelHandler.sendRooms(socketChannel, ll);
                
            }
            else if(msg.getMessageType() == MessageType.DISCONNECT) {
                User userToDisconnect = connectedChannels.get(socketChannel);
                connectedChannels.remove(socketChannel);
                connectedUsers.remove(userToDisconnect);
                socketChannel.close();
            }
            else {
            	forwardMsg(msg, socketChannel);
            }
    }

	private User checkMsgForCredentials(Message msg) {
        
		if (msg.getMessageType() == MessageType.LOG_IN) {
            String[] credentials = msg.getText().split(":");
            return new User(1, credentials[0], credentials[1]);
        }

        return null;
	}
	
	 private void forwardMsg(Message msg, SocketChannel socketChannel) throws IOException {
			
		 	User currentUser = connectedChannels.get(socketChannel);
			SocketChannel clientSocketChannel = connectedUsers.get(currentUser);
			msg.setText(" Server to Client ");
			ChannelHandler.sendMsg(clientSocketChannel, msg);
			
			//Room rr = (Room)ll.stream().filter(r -> r.getId() == msg.getRoomID()).findFirst();
			
			System.out.format("Room ID %d", msg.getRoomID());
	    }

}
