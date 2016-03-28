package com.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nio.util.ChannelHandler;
import com.nio.util.MessageType;
import com.nio.util.Serialization;
import com.nio.model.*;

public class nioClient {

	static List<Room> ll = new ArrayList<Room>();
	
	private static String host = "localhost";
    private static int port = 8881;
    private static SocketChannel socket;
    
	public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        String menu = "\t1. Connection\n\t2. Log In\n\t3. Send msg\n\t4. Disconnection\n";        
    	System.out.println(menu);
    	
        while(scanner.hasNext()) {
        	int choice = scanner.nextInt();
        	
        	switch (choice) {
            case 1:
            	connection();
            	System.out.println("1");
                break;
            case 2:
            	logIn();
            	System.out.println("2");
                break;
            case 3:
            	sendMessage();
            	System.out.println("3");
                break;
            case 4:
            	disconnection();
            	System.out.println("4");
            	break;
            default:

            	try {
					socket.finishConnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	return;
        	}
        }
    }

	//1. Connection
	private static void connection()
	{
        try {
        	
			socket = SocketChannel.open();
	        socket.configureBlocking(true);
	        socket.connect(new InetSocketAddress(host, port));
	        
	        //socket.register(selector, SelectionKey.OP_CONNECT);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//2. Log In
	private static void logIn()
	{
        try {
        	
        	Message msg = new Message(MessageType.LOG_IN, 1, "villimon:villimon", 2);
        	ChannelHandler.sendMsg(socket, msg);
             
             try {
				ll = ChannelHandler.readRooms(socket);
				
				System.out.println("Room nomber: " + ll.size());
				
				for (Room r : ll) {
					System.out.format("Room ID: %d\n", r.getId());
					System.out.format("Room name: %s\n", r.getName());
					
					System.out.println("User nomber: " + r.getUsers().size());
					for (User u : r.getUsers()) {
						System.out.format("User ID: %d\n", u.getId());
						System.out.format("User Username: %s\n", u.getUsername());
						System.out.format("User Password: %s\n", u.getPassword());
					}
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//3. Send Message
	private static void sendMessage()
	{
        try {
        	
        	Message msg = new Message(MessageType.SEND_MESSAGE, 1, " Client to Server ", 2);
        	ChannelHandler.sendMsg(socket, msg);
	        
        	msg = ChannelHandler.readMsg(socket);
        	
        	System.out.println(msg);
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//4. Disconnect
	private static void disconnection()
	{
        try {
        	
        	Message msg = new Message(MessageType.DISCONNECT, 1, "villimon:villimon", 2);
        	ChannelHandler.sendMsg(socket, msg);
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
