package com.nio.main;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import com.nio.interfaces.ServerListener;
import com.nio.server.Server;
import com.nio.server.nioServer;

public class Main {

	private static Server mServer;
	
	public static void main(String[] args) {
        // TODO Auto-generated method stub
		
		String host = "localhost";
        int port = 8881;
        
        new Thread(new nioServer(port)).start();
        /*
        mServer = new Server(new ServerListener() {
			
        	@Override
        	public void onStarted(String address, int port) {
        		// TODO Auto-generated method stub
        		System.out.println("Server " + address + " start at port " + port + "...");
        	}

        	@Override
        	public void onConnectionFailed(String message) {
        		// TODO Auto-generated method stub
        		System.out.println("Server was unable to start. Reason: " + message);
        	}
        	
        	@Override
        	public void onClientConnected(SocketChannel client) {
        		String address = "";
        		
    			try {
					address = client.getRemoteAddress().toString();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
        		
        		System.out.println(address + " connected...");
        	}
        	
        	@Override
        	public void onClientDisconnected(SocketChannel client) {
    			String address = "";
        		
    			try {
					address = client.getRemoteAddress().toString();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
    			
        		System.out.println(address + " has been disconnected.");
        	}
        	
        	@Override
        	public void onClientMessageReceived(SocketChannel client, String message) {
    			String address = "";
        		
    			try {
					address = client.getRemoteAddress().toString();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
    			
        		System.out.println(address + " says: " + message);
        		try {
					mServer.sendMessage(client, "Response");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		});
        mServer.connectToServer(host, port);
        
        new Thread(mServer).start();*/
    }
}
