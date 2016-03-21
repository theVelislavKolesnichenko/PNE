package com.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.nio.model.Message;
import com.nio.util.*;

public class Client {
	private String host;
    private int port;
    private static SocketChannel socket;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        socket = SocketChannel.open();
        socket.configureBlocking(true);
        socket.connect(new InetSocketAddress(host, port));
    }

    public void disconnect() throws Exception {
        socket.close();
    }

    public void send(String s) {
        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
        try {
            socket.write(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Receives server response
     * */
    public String receive() throws IOException{
        ByteBuffer rpHeader= ByteBuffer.allocateDirect(1024);
        socket.read(rpHeader);
        rpHeader.flip();
        byte[] headerBytes = new byte[rpHeader.remaining()];
        rpHeader.get(headerBytes);
        Message m = null;
        try {
			m = Serialization.deserialize(headerBytes);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return m.toString();
        //return new String(headerBytes, "UTF-8");
    }
    
    public static void main(String[] args) {
    	String host = "localhost";
        int port = 8881;
    	
        Client client = new Client(host, port);
        try {
			client.connect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	int countdown = 1;
        while (countdown < 3){
            ++countdown;
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            sendMessage(host, port);
        }
        try {
			client.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void sendMessage(String host, int port) {
		try {
	        //client.send("Hello server");
	        ChannelHandler.sendMsg(socket, 
	        		new Message(MessageType.SEND_MESSAGE, 22, " Client to Server ", 23));
	        //String receivedMsg = client.receive();
	        Message receivedMsg = ChannelHandler.readMsg(socket);
	        System.out.println(receivedMsg);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
}
